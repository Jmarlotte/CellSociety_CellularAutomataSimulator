CompSci 308: Cell Society Design

### Introduction
In this project, we plan to build a program that can simulate a cellular automaton (CA) process. This program has a couple of components:
- Specification File Reading: A specification file is an XML file that contains an initial configuration of the environment (i.e. which entry of a grid environment is occupied by what cell) and rules of the simulation (i.e. how a cell respond according to its neighbors),
- Simulation Progression: There should be a class controlling the update of each cell in response to its neighbor at every time interval,
- GUI Rendering: A GUI rendering class is responsible for drawing the current environment configuration onto screen, and
- Run-Time User Input: One or more classes are responsible for monitoring the simulation parameter change made by the user at run-time. These include changing board size, changing simulation speed, and changing board configuration using mouse or keyboard input.

### Overview

There should be several essential classes.
- SpecificationFileParser: This class is responsible for reading the input XML file to internal data structures. It should create two data structures: one for holding the rules of simulation and the other for holding the board configuration.
- Cell: This class represents an individual cell. A cell is aware of its neighbors by storing an ArrayList of Cells. The cell also make decisions locally and independently. Since each decision is independent and simultaneous, the update method cannot be implemented atomically. Instead, it is broken down to two methods, prepareForUpdate(), which computes its next step value but do not modify its actual value, and update(), which substitutes its planned update value into its current value.
- Simulator: This class is the main controller for the program. It stores the board configuration as an ArrayList of Cells. It is also responsible for autonomously updating the board by calling prepaeForUpdate() and update() function on each of the cells. It should also keep time by itself using TimerTask.
- Rule: This class holds all information about a rule that is used in simulation.
- Renderer: This class is responsible for rendering to GUI. On initialization, it should receive parameters about the board (e.g. dimension of the board). It should provide public function of updateBoard(), which is called by Simulator at appropriate time to update cell appearance on the screen.
- InputHandler: This class is responsible for handling user input at run time. Depending on functionality, this method may include methods such as (a). changeSimulationSpeed(), which changes speed of simulation; and (b). changeBoardSize(), which changes the size of the board. These methods should notify Simulator and Renderer to make appropriate change.
A UML diagram is shown below:

![UML Diagram](diagram.png "UML Diagram")

### User Interface

The UI will allow the User to interact with the simulation by changing the speed of the simulation, percentages of some parameters, and size of grid. The UI itself will just mainly appear to be a grid of cells with options to start, stop, and reset the simulation as well as change some parameters.

### Design Details

 1. Configuration: Configuration functionalities are mostly summarized in `io` package.
  - For input, a `SpecificationFileParser` parses board and rule configuration builds a board of cells which contain the rule. In the process, it uses `BoardBuilder` to handle board-specific operations, such as instantiating cells and connecting neighbors. In addition, `SpecificationFileParser` requires a delegate that conforms to `SpecFileParserDelegate` interface, which is used to show warning and error messages during parsing. A custom exception, `FileParsingException` is created and thrown each time a fatal error occurred. This exception is caught in the `SpecificationFileParser` and redirected to an error message popup in the GUI (which is unfortunately unimplemented in GUI, of which my teammates are in charge).
  - For output, `BoardConfigurationSaver` handles saving entire board in a file, which can be read by the `SpecificationFileParser` at a later time to reconstruct the board state.
 2. Simulation: The simulation is broken into three major parts: Cell, Rule, and Stepper.
  - Cell: Each specific cell inherits from a common superclass `Cell`. Each cell has a rule, and a value, at the bare minimum. It may also contain task-specific parameters such as current health for Shark in WaTor simulation. In addition, a cell also stores its neighborhood information, which is stored in a `CellNeighbor` class and distinguishes each neighbor separately (so that a top neighbor is distinguishable from a left neighbor). Notably, all cells are initialized by `CellFactory`, which is a factory class that handles different types and chooses appropriate subclass to instantiate.
  - Rule: Each rule inherits from a common superclass `Rule`. Each rule holds relevant rule-specific parameters that is used in simulation. The rule is used either in cell in which local update is possible or in stepper in which global update is necessary (discussed in more detail below).
  - Stepper: Each stepper inherits from a common superclass `BaseStepper`. The functionality of a stepper is to update `board` (in `ArrayList<Cell>` representation). There are two types of update (it should be noted that these two types of update provide a unified interface method `step` that only differs in implementation):
   1. Local update: in Game of Life and Fire, each cell has complete information from its neighbor to update itself. In this case, each cell locally implements `prepareForUpdate` and `update` function, and the `LocalStepper` just calls these two functions on each cell in the board.
   2. Global update: in Segregation and WaTor, a global algorithm needs to step in to make the update (e.g. deciding how to relocate in segregation). In this case, the stepper uses whole board information to make changes.
 3. Control: The program is controlled by `SimulationController`, which handles simulation-specific control, and `MainController`, which handles more general program control. `ApplicationLauncher` initiates the program.
  - The program control flow begins by creating an instance of `MainController` and a `SimulationDisplay`, which initially only has a window with UI control elements (e.g. buttons). The `Main Controller` handles user actions (start, stop, step, etc.) through the `SimulationDisplayDelegate` interface.

  - `SimulationController` instantiates the `Stepper` object to carry out the simulation model logic, and `GridDisplay` to update. It then facilitates the running of the simulation through its `step()` method which tells the model to update and then tells the view which cells to update.
 4. User Interface
  - `SimulationDisplay` represents the general display–the window, ui elements, and any other components. It passes user actions along to its delegate of type `SimulationDisplayDelegate`.

  - `GridDisplay` represents the visual board. It is implemented through subclasses according to what shape of cell is to be used. This class knows how to draw itself given dimensions and number of rows/columns, and how to update a cell at a given location to a new color.

### Design Considerations

We mainly discussed how to represent a rule. A rule can be “local” or “global”. A local rule means that each cell can make its own decision and covers most of the case. In a global rule however, a cell must communicate with nearby cells to make decision. For example, in segregation model, a movement of a cell is equivalent to two cells switching value, and thus is “global”.

For local rules, without stochasticity, it can be exhaustively enumerated by a tabular function from current neighbor configuration to next state self configuration. With stochasticity, the function is replaced with a probability mass function.

For global rules, a global controller seems necessary to control the simulation at a level higher than individual cell. We have not worked out exact details and decide to leave this part later to implement.

In addition, we talked about how the board size change should work. We decide to make an underlying fixed-sized board (e.g. 100x100). Then board size change effectively changes the visible part of the board, rather than creating new board region or destroying existing board region.

### Adding Features

I tried to make features as easy to extend as possible. Adding a new simulation require new subclasses to `Cell`, `Rule`, and/or `Stepper`.

 1. Cell Type: Each cell class subclasses `Cell`, and any subclass in the `Cell` can be used in any cell type-agnostic place. For example, when creating a board, most of the board layout (such as width and height of the board, number of neighbors, connection of neighbors, etc.) is generic to all cells. In fact, the whole `BoardBuilder` class is designed to handle the superclass `Cell`, and the only connection to type-specific information is handled through `CellFactory`, which is a factory class that instantiates desired cell subclass instance using provided information.
 2. Rule Types: Each rule class subclasses `Rule`. Rules and cells are separate and not necessarily a one-to-one relationship. This design enables maximal extendability by allowing a `Cell` to have multiple `Rule` types or a `Rule` to be applied on multiple `Cell` types. One use case is already demonstrated in the code: the `ReproductionCell` (our name for game of life, since it is mainly about reproduction) can handle both a traditional rule that is based only on number of neighbors (`ReproductionRule`) and a non-totalistic rule that is based on the exact location of neighbors (`NonTotalisticRule`).
 3. Stepper: Each stepper subclasses `BaseStepper`. Each `Stepper` has a method called `step()`, which changes the board, represented by an `ArrayList` of `Cell`s. Again, the `Cell` is the superclass name, which is agnostic of specific cell type. According to specific rules, each `Stepper` class implements the stepping function differently, and this is the only place in which specific type of `Cell`s and `Rule`s need to be known.

To add a new simulation, a new `Cell` subclass needs to be created and a new `Rule` class needs to be created. In addition, `CellFactory` needs to be modified to handle initialization of the new cell type. If the simulation can be done locally, then `LocalStepper` can be reused in `SimulationController`. Otherwise, a new `Stepper` needs to be created to handle global update rule and `SimulationController` needs to b modified to handle this new type of `Stepper`. Other than this, IO and rendering do not need any change. The simplicity of requirement illustrates the flexibility of our design.

To change the grid display, one would just make a new subclass of GridDisplay with the desired traits and then modify SimulationController and the resource file which lists available simulations to make it a selectable option.

To add UI control elements, the user would add code to the `makeUIPanel()` method in `SimulationDisplay`, add the handler, and add a delegate method to `SimulationDisplayDelegate` to send the event to the main controller. The user would then modify `MainController` and any other necessary classes to implement the response to the user action.

To add multiple simulations, the user would simply add code to instantiate another SimulationController, and then the MainController would hold a collection of SimulationControllers. Instead of calling `simulator.step()`, it would call `step()` on each of the simulators in its collection. The challenging part would be making design decisions about how to organize the visual appearance of multiple simulators, and how to add and organize UI elements specific to each simulation. This would likely mean adding/modifying delegated methods for user actions so that they indicate which simulation is being affected.


### Team Responsibilities

Yilun implemented simulation logic and file parsing. All extensions in Configuration and Simulation parts are also implemented by Yilun. James and Andrew implemented UI, extensions in Visualization, and controllers linking the UI and model.
