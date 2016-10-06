package ui;

import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class UIPanel {
	private final String UIELEMENTS_RESOURCE_PATH = "resources/UIElements";	
	private final int NUMBER_OF_AVAILABLE_SIMULATIONS = 5;
	private final int NUMBER_OF_AVAILABLE_SPEEDS = 5;
	private final int NUMBER_OF_AVAILABLE_CELL_SHAPES = 2;
	
	private SimulationDisplayDelegate delegate;
	private ResourceBundle myUIElementsResource;

	private Button stepButton;
	private Button stopButton;
	private Button startButton;
	private Button resetButton;
	private Button saveBoardButton;
	private HBox panel;
	
	private ComboBox<String> speedSetter;
	private ComboBox<String> cellShapeSetter;
	private ComboBox<String> simSetter;
	
	public UIPanel(){
		myUIElementsResource = ResourceBundle.getBundle(UIELEMENTS_RESOURCE_PATH);
		panel = makePanel();
	}
	
	private HBox makePanel(){
		panel = new HBox();
		
		startButton = createButton("startButton", new EventHandler<ActionEvent>() {
            public void handle (ActionEvent event) {
            	startButtonHandler();
            }});
		startButton.setDisable(true);
		
		stopButton = createButton("stopButton", new EventHandler<ActionEvent>() {
            public void handle (ActionEvent event) {
            	stopButtonHandler();
            }});
		stopButton.setDisable(true);
		
		stepButton = createButton("stepButton", new EventHandler<ActionEvent>() {
            public void handle (ActionEvent event) {
            	stepButtonHandler();
            }});
		stepButton.setDisable(true);
		
		resetButton = createButton("resetButton", new EventHandler<ActionEvent>() {
            public void handle (ActionEvent event) {
            	resetButtonHandler();
          
            }});
		resetButton.setDisable(true);
		
		saveBoardButton = createButton("saveBoardButton", new EventHandler<ActionEvent>() {
            public void handle (ActionEvent event) {
            	saveBoardButtonHandler();
            }});
		saveBoardButton.setDisable(true);

		simSetter = createComboBox("simSetter", NUMBER_OF_AVAILABLE_SIMULATIONS, new EventHandler<ActionEvent>() {
            public void handle (ActionEvent event) {
            	simSetterHandler();
            }});
		simSetter.setPromptText(myUIElementsResource.getString("simSetterLabel"));
		
		speedSetter = createComboBox("speedSetter", NUMBER_OF_AVAILABLE_SPEEDS, new EventHandler<ActionEvent>() {
            public void handle (ActionEvent event) {
            	speedSetterHandler();
          
            }});
		speedSetter.setPromptText(myUIElementsResource.getString("speedSetterLabel"));
		//Set to 100% to start
		speedSetter.getSelectionModel().select(2);
		
		cellShapeSetter = createComboBox("cellShapeSetter", NUMBER_OF_AVAILABLE_CELL_SHAPES, new EventHandler<ActionEvent>() {
            public void handle (ActionEvent event) {
            	cellShapeSetterHandler();
            }});
		cellShapeSetter.setPromptText(myUIElementsResource.getString("cellShapeLabel"));
		
		panel.getChildren().add(startButton);
		panel.getChildren().add(stopButton);
		panel.getChildren().add(resetButton);
		panel.getChildren().add(stepButton);
		panel.getChildren().add(simSetter);
		panel.getChildren().add(speedSetter);
		panel.getChildren().add(cellShapeSetter);
		panel.getChildren().add(saveBoardButton);
		return panel; 
	}
	
	private Button createButton(String description, EventHandler<ActionEvent> handler ){
		
		Button button = new Button(); 
		String buttonText = myUIElementsResource.getString(description);
		button.setText(buttonText);
		button.setOnAction(handler);
		
		DropShadow effect = new DropShadow();
		button.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>(){
			@Override public void handle(MouseEvent e){
				button.setEffect(effect);}});
		
		button.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
			@Override public void handle(MouseEvent e){
				button.setEffect(null);}});
		
		return button; 
		
	}
	
	private ComboBox<String> createComboBox(String description, int numberOfOptions, EventHandler<ActionEvent> handler){
		
		ArrayList<String> options = new ArrayList<String>();
		for (int i = 1; i < numberOfOptions + 1 ; i++){
			String temp = myUIElementsResource.getString(description + Integer.toString(i));
			if (! temp.equals(null)){
				options.add(temp);
			}
		} 
		ComboBox<String> box = new ComboBox<String>();	
		box.getItems().addAll(options.toArray(new String[options.size()]));
		box.setOnAction(handler);
		return box;
	}
	
	private void startButtonHandler(){
		startButton.setDisable(true);
		stopButton.setDisable(false);
		stepButton.setDisable(true);
		resetButton.setDisable(false);
		saveBoardButton.setDisable(false);
		delegate.resumeSimulation();
	}
		
	private void stopButtonHandler(){
		stopButton.setDisable(true);
		startButton.setDisable(false);
		stepButton.setDisable(false);
		delegate.pauseSimulation();
	}
	
	private void resetButtonHandler(){
		stopButton.setDisable(true);
		startButton.setDisable(false);
		stepButton.setDisable(false);
		delegate.resetSimulation();
	}
	
	private void stepButtonHandler(){
		delegate.stepSimulation();
	}
		
	private void simSetterHandler(){
		delegate.setSimulationFileName(simSetter.getSelectionModel().getSelectedItem().toString());
		resetButtonHandler();
	}
	
	private void speedSetterHandler(){
		String newRatePercentage = speedSetter.getSelectionModel().getSelectedItem().toString();
		double newRate = Double.parseDouble(myUIElementsResource.getString(newRatePercentage));
		delegate.changeSimulationSpeed(newRate);
	}
	
	private void saveBoardButtonHandler(){
		delegate.saveBoard();
	}
	
	private void cellShapeSetterHandler(){
		//Does nothing. Current shape selection is retrieved when needed through getShapeSelection()
	}
	
	/**
	 * Get the current cell shape selection.
	 * @return
	 */
	public String getShapeSelection(){
		return cellShapeSetter.getSelectionModel().getSelectedItem();
	}
	
	/**
	 * Set the delegate to receive user action events.
	 * @param d is the class that implements the delegate interface to handle events.
	 */
	public void setDelegate(SimulationDisplayDelegate d){
		delegate = d;
	}
	
	/**
	 * Get the UIPanel as a Node
	 * @return Node with all panel elements
	 */
	public Node getUIPanelNode(){
		return panel;
	}
}
