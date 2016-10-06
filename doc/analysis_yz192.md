Cell Society Analysis
=
Status
-------------

In general, I think our code is well-designed. The code is consistent in styles and key pieces of information are well-commented. All methods do not have any hidden side-effects and the names are as descriptive as possible. In addition, function overload is kept to a minimum to avoid possible confusions. 

The class structures are clear, and all subclass namings are suggestive. For example, `FireCell` is naturally a subclass for `Cell` and `WaTorRule` is naturally a subclass for `Rule`. There are some static methods, but in general, if a class has a static method, then all of its methods are static. These classes are used as utility classes. For example, the class `BoardBuilder` is a utility class, which has many methods to create board by various specifications such as full board description, select entry description, and random initialization. Thus, I think that there are no convoluted dependencies. 

####Feature Extensibility

I tried to make features as easy to extend as possible. Some features that *I personally* worked on while keeping extension in mind is

 1. Cell Type: Each cell class subclasses `Cell`, and any subclass in the `Cell` can be used in any cell type-agnostic place. For example, when creating a board, most of the board layout (such as width and height of the board, number of neighbors, connection of neighbors, etc.) is generic to all cells. In fact, the whole `BoardBuilder` class is designed to handle the superclass `Cell`, and the only connection to type-specific information is handled through `CellFactory`, which is a factory class that instantiates desired cell subclass instance using provided information. 
 2. Rule Types: Each rule class subclasses `Rule`. Rules and cells are separate and not necessarily a one-to-one relationship. This design enables maximal extendability by allowing a `Cell` to have multiple `Rule` types or a `Rule` to be applied on multiple `Cell` types. One use case is already demonstrated in the code: the `ReproductionCell` (our name for game of life, since it is mainly about reproduction) can handle both a traditional rule that is based only on number of neighbors (`ReproductionRule`) and a non-totalistic rule that is based on the exact location of neighbors (`NonTotalisticRule`). 
 3. Stepper: Each stepper subclasses `BaseStepper`. Each `Stepper` has a method called `step()`, which changes the board, represented by an `ArrayList` of `Cell`s. Again, the `Cell` is the superclass name, which is agnostic of specific cell type. According to specific rules, each `Stepper` class implements the stepping function differently, and this is the only place in which specific type of `Cell`s and `Rule`s need to be known. 
 4. Board Geometry: The board is represented by an `ArrayList` of `Cell`s. Each `Cell` has a `CellNeighbor`, which stores its neighbor information. In addition, Extending the board into triangle and hexagon shapes are easy. Specifically, each `Cell` separates the model from the view: 
   - the model part consists of `CellNeighbor`, cell value, cell specific parameters (such as health for Shark in WaTor), and cell rule; 
   - the view part consists of `x`, and `y`, which are instance variables of a `Cell`. These variables control the rendering of the cell (i.e. where specifically should the cell be placed). 
 
 Thus, to extend the cell shape into other shapes, only a new convention of `x` and `y` mapping to screen is needed. All the model parts do not need to change. Although the shape extension is not done, I did implement the non-totalistic rule custom neighbor connection pattern, and custom board wrapping patterns. 
  - In non-totalistic rule, the rule directly operates on `CellNeighbor`, and the `CellNeighbor` class distinguishes each specific neighbor, so that a top neighbor is distinguishable from a left neighbor, for example. In addition, `CellNeighbor` distinguishes non-existent neighbors (for cells on border) versus illegal neighbors (for example, the upper-left neighbor in a 4-neighbor connection type is illegal), to allow full customization of non-totalistic rules. 
  - In custom neighbor connection type, two built-in types are allowed. 4-neighbor pattern connects each neighbor to four adjacent neighbors, and 8-neighbor pattern adds connections to 4 corner cells on top of 4-neighbor connection. In addition, a user can also specify with a string of 0s and 1s which neighbor(s) out of the 8 neighbors to connect. 
  - In custom board wrapping, two types of wrapping are designed. The default is no wrapping, in which cells on border has fewer neighbors than cells in the center. The other one is torsoidal, in which the top neighbor of a top cell is the bottom cell, and similarly for left/right border. To implement this feature, the only classes involved are `BoardBuilder` and `CellNeighbor`: the former sets up the wrapping pattern through the latter. Again, this change is totally agnostic of cell type, rule type, and stepper type. 

####Class Comment

 1. `MainController`: This class is, as its name suggests, the main controller of the program. It keeps the GUI timing, responds for all UI call back events, and controls the simulation through `SimulationController`. One thing good is that it directly uses the `TimeLine` built-in for `JavaFX`, and thus speed control and pause/start are very easy to implement. However, by the project deadline, my teammates failed to implement two methods `showErrorMsg` and `showWarningMessage` for error handling for XML parsing, and thus the error or warning messages are only printed to console. Since this class is pretty general, I think the only changes necessary to make to be suitable for a different project is to change the model and the view it connects. 
 2. `LineChartManager`: This class is used to create a line chart for tracking the number of cells. I choose this class because there are several problems with it: 
   - The constructor does nothing. There are still many parameters left uninitialized after the constructor. Instead, the method `chartCreator` does most of the initialization. Thus, it makes more sense for this the constructor to take `ArrayBoard<Cell>` and do everything that `chartCreator` is currently doing. 
   - Both of the methods (except for the getter) are very long. The longer one is nearly 60 lines of code, with a lot of seemingly duplicate code that can very likely be extracted. 
   - This class directly uses the `Cell` type information. A better design would be handle type-specific code in a factory class, similar to the role of `CellFactory` in `BoardBuilder`. 

 Since the class is creating a line chart, which is very specific to this project, it is not going to be very useful in another project, which does not need line chart. If enough refactoring is done, however, this class can be made into a more general UI element controller that can be useful in other projects with line chart. 

Design 
---
####Design Description
The overall design separates the three components (configuration, simulation, and visualization) well. I implemented all of Configuration and Simulation, and thus these two sections are described in much detail. My teammates implemented Visualization, and thus that section is described at a high-level. 

 1. Configuration: Configuration functionalities are mostly summarized in `io` package. 
  - For input, a `SpecificationFileParser` parses board and rule configuration builds a board of cells which contain the rule. In the process, it uses `BoardBuilder` to handle board-specific operations, such as instantiating cells and connecting neighbors. In addition, `SpecificationFileParser` requires a delegate that conforms to `SpecFileParserDelegate` interface, which is used to show warning and error messages during parsing. A custom exception, `FileParsingException` is created and thrown each time a fatal error occurred. This exception is caught in the `SpecificationFileParser` and redirected to an error message popup in the GUI (which is unfortunately unimplemented in GUI, of which my teammates are in charge). 
  - For output, `BoardConfigurationSaver` handles saving entire board in a file, which can be read by the `SpecificationFileParser` at a later time to reconstruct the board state. 
 2. Simulation: The simulation is broken into three major parts: Cell, Rule, and Stepper. A detailed discussion is in the Feature Extensibility section, which focuses on how the design enables flexible extension to new cells and rules. A (still quite extensive) summary is provided here. 
  - Cell: Each specific cell inherits from a common superclass `Cell`. Each cell has a rule, and a value, at the bare minimum. It may also contain task-specific parameters such as current health for Shark in WaTor simulation. In addition, a cell also stores its neighborhood information, which is stored in a `CellNeighbor` class and distinguishes each neighbor separately (so that a top neighbor is distinguishable from a left neighbor). Notably, all cells are initialized by `CellFactory`, which is a factory class that handles different types and chooses appropriate subclass to instantiate. 
  - Rule: Each rule inherits from a common superclass `Rule`. Each rule holds relevant rule-specific parameters that is used in simulation. The rule is used either in cell in which local update is possible or in stepper in which global update is necessary (discussed in more detail below). 
  - Stepper: Each stepper inherits from a common superclass `BaseStepper`. The functionality of a stepper is to update `board` (in `ArrayList<Cell>` representation). There are two types of update (it should be noted that these two types of update provide a unified interface method `step` that only differs in implementation): 
   1. Local update: in Game of Life and Fire, each cell has complete information from its neighbor to update itself. In this case, each cell locally implements `prepareForUpdate` and `update` function, and the `LocalStepper` just calls these two functions on each cell in the board. 
   2. Global update: in Segregation and WaTor, a global algorithm needs to step in to make the update (e.g. deciding how to relocate in segregation). In this case, the stepper uses whole board information to make changes. 
 3. Visualization: 

####Add a Simulation
To add a new simulation, a new `Cell` subclass needs to be created and a new `Rule` class needs to be created. In addition, `CellFactory` needs to be modified to handle initialization of the new cell type. If the simulation can be done locally, then `LocalStepper` can be reused in `SimulationController`. Otherwise, a new `Stepper` needs to be created to handle global update rule and `SimulationController` needs to b modified to handle this new type of `Stepper`. Other than this, IO and rendering do not need any change. The simplicity of requirement illustrates the flexibility of our design. 

####Design Justification
I make the XML parsing a relatively independent module rather than a sub-procedure in the `main` method because of the need for robust error handling. In the current design, the parser can output custom error message and through a delegate, notify the user by popup dialog or warning text on the GUI. In addition, this independence allows independent modification of parsing algorithm that does not directly affect the `main` method or some other critical methods directly on the program flow. 

Another big design consideration is the separation of `Cell`, `Rule`, and `Stepper`. This consideration is extensively discussed earlier in text, mainly in the Feature Extensibility Section. 

####Features
1. Non-Totalistic Rule: This feature implements non-totalistic rule for cells. A non-totalistic rule is a rule in which not only number of neighbor cells per type matters, but also locations of those cell. For example, a cell may survive when the only surviving cell is on its left, while dead when the only surviving cell is elsewhere. 
 The rule is parsed from a string of length equal to number of neighbors. Each character is one of 0, 1, or x, representing the requirement for the corresponding neighbor. The (maximally 8) neighbors are sorted from upper-left position and rotating clockwise. Thus, the first character of the rule string represents the requirement for upper-left neighbor, if it exists (or closest clockwise, if it doesn't). 0 means empty cell, 1 means occupied cell, and x means don't care. 
 Rule parsing is done in `SpecificationFileParser` class, and the `NonTotalisticRuleClass` has a update function that tells what the next step value for a cell should be given its current value. In other words, this rule is a local rule and a `LocalStepper` is sufficient for the board simulation. 
 The implementation of update rule is encapsulated and abstracted away. Thus, when each cell update its value, it does not need to know how the rule application works.
 To extend the feature, one direction is to add more flexible representation because currently a exhaustive enumeration of neighbor conditions is required. The only change is a new parsing method, and everything else should be compatible. Thus, I consider this feature to be very easy to extend. 

2. Simulation Control (which I did not implement): The control is done in `MainController`, It uses `JavaFX` native `TimeLine` class for "playback" control. In addition, it interfaces with `SimulationController`, which does the backend computation, and `SimulationDisplay`, which renders the GUI on to the screen. The `MainController` implements two delegate interfaces, one for simulation and one for XML file parsing. The first one notifies the controller of the user action on the GUI, while the second one notifies the controller of parsing errors or warnings. This interface design is very extensible because new duties of the controller are needed, they can be added by having the it to `implements` new interfaces. 

Alternate Designs
---
In general, our design handles feature extensions well. There are several notable points: 

 - Board geometry and separation of `Cell`, `Rule`, and `Stepper`: this part is extensively described in the "Feature Extensibility" section. To summarize, since I use `CellNeighbor` to represent neighbor rather than relying on indexing within a 2D grid-like array, features such as torsoidal wrapping, non-totalistic rules, different cell shapes, and custom neighbor connectivity patterns are easy to implement. Moreover, I am able to implement all of the above-mentioned except for different cell shape. 
 - XML error handling: this part is extensively described in the "Design Description" section. To summarize, since we separate out file parsing in an independent class (with a corresponding exception class and an error-notifying interface), robust error handling and comprehensive condition checking can be done without over-burdening the main class. In addition, new error checking can be added without affecting other classes. 

####Design Decisions
Two design decisions are stepper and board size: 
 
 - Stepper: we discussed if we want to have a stepper for all simulations or only global simulations. Previously I implemented only global steppers for Segregation and WaTor (and hence the package name) while it occurred to me that a local stepper is just a special case of a global stepper. Thus, I then implemented a local stepper for Game of Life and Fire. 
 This design has a bit of overhead in terms of a new class, but in general is an improvement since it makes the stepping interface much neater. After this change, the `SimulationController` no longer needs to manually update each cell. Instead, it just need to call `step` on the relevant stepper for all simulation types. 
 - Board size: we considered varying or fixed board size. For a varying board size, it requires less memory when the size is small and require more space on the fly as it increases in size. In addition, simulation is more efficient on smaller sizes, and thus is preferred if a large board is not necessary. 
 However, dynamically change the size of the board is a big algorithmic challenge, and we decide to go for fixed board size, but varying rendering window. In other words, the board is kept at same size, which is specified by the XML file, while in the GUI, if the use needs to zoom in, only a part of the board will be shown. 

Conclusion
---

####Best Features
 1. Separation of `Cell`, `Rule`, and `Stepper`. As discussed earlier, this separation enables 
  - the creation of new types of cells with same rule; 
  - the creation of new rules for an old type of cell; 
  - the creation of new local simulation without adding anything to stepper; and
  - the creation of new global simulation that is easy to incorporated in the main simulator. 
 
 These easy extensions show the flexibility of the design. 
 2. Built-in `TimeLine` control within `MainController`. The timing is done by using the native timeline support of `JavaFX`. This method avoids any multi-threading or `TimerTask`, which is typically used for repeated event. 

####Worst Features
1. Unpolished UI: The UI design is crude in general. There are many details that can be improved. For example, each selection box should be pre-filled with the default value rather than black, and more instructive title and text labels. Also, there are some unimplemented features in UI, especially as part of `SpecFileParserDelegeate` interface. 
2. LineChart: The line chart is not well made. First, there are a lot of long functions and duplicate code in the `LineChartManager` class. In addition, for the current visual effect, the line chart often changes its scale abruptly, and thus it is hard to get a constant sense of scaling and proportion. 

####Being a Better Designer
I think the following aspects are critical: 
 1. Be general: I benefit greatly from inheritance relationship, which makes adding new features easy. Thus, I think in future design, I should always think of commonalities between class and most functionally similar classes can be grouped into superclass/subclass relationship. 
 2. Comment frequently: I did not include too much comment in the project. However, I do think comment is necessary, and sometimes even I would wish myself adding some more comment to some particular methods. 
 3. Reduce reliance on Collection: I use Java Collection (e.g. `ArrayList`) mostly individually, but it may be good for some of them to be in class. For example, I spent quite a long time refactor cell neighbors into a separate `CellNeighbor` class, rather than `ArrayList<Cell>`, after I realize the importance of distinguishing cell neighbors from each other in non-totalistic rule. 