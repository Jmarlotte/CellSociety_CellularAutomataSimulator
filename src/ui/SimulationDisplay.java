package ui;
import java.util.*;
import cell.Cell;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import main.MainController;

//@authors: Andrew Bihl, James Marlotte

public class SimulationDisplay {

private final String RESOURCE_PATH = "resources/DisplaySettings";
private final String UIElements_Path = "resources/UIElements";
private final int NUMBER_OF_AVAILABLE_SIMULATIONS = 5;
private final int NUMBER_OF_AVAILABLE_SPEEDS = 5;
private final int NUMBER_OF_AVAILABLE_SIZES = 5;
private final int NUMBER_OF_AVAILABLE_CELL_SHAPES = 3;

private ResourceBundle myResources;
private ResourceBundle myUIElements;

private Scene myScene;
private Button stepButton;
private Button stopButton;
private Button startButton;
private Button resetButton;
private ComboBox<String> speedSetter;
private SimulationDisplayDelegate delegate;
private ComboBox<String> gridSizeSetter;
private ComboBox<String> cellShapeSetter;
private ComboBox<String> simSetter;

private GridDisplay board;
private BorderPane root; 
private LineChart myChart;

	public SimulationDisplay(){
		myResources = ResourceBundle.getBundle(RESOURCE_PATH);
		myUIElements = ResourceBundle.getBundle(UIElements_Path);
		root = new BorderPane();
		root.setTop(makeUIPanel());
		myScene = createScene(root);
	}
	
	private Scene createScene(Parent root){
		int width = Integer.parseInt(myResources.getString("WindowWidth"));
		int height = Integer.parseInt(myResources.getString("WindowHeight"));
		Scene scene = new Scene(root, width, height, Color.WHITE);
		return scene;
	}
	
	public Scene getScene(){
		return myScene;
	}

	public void addBoard(GridDisplay gridDisplay, double x, double y){
		root.getChildren().remove(board);
		gridDisplay.getBoard().setLayoutX(x);
		gridDisplay.getBoard().setLayoutY(y);
		root.getChildren().add(gridDisplay.getBoard());
		board = gridDisplay;
	}
	
	public void addChart(LineChart lineChart, double x, double y){
		//root.getChildren().remove(myChart);
		//gridDisplay.getBoard().setLayoutX(x);
		//gridDisplay.getBoard().setLayoutY(y);
		root.setLeft(lineChart);
		myChart = lineChart;
	}
	
	private Button createButton(String description, EventHandler<ActionEvent> handler ){
		
		Button ret = new Button(); 
		String retText = myUIElements.getString(description);
		ret.setText(retText);
		ret.setOnAction(handler);
		
		DropShadow effect = new DropShadow();
		ret.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>(){
			@Override public void handle(MouseEvent e){
				ret.setEffect(effect);}});
		
		ret.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
			@Override public void handle(MouseEvent e){
				ret.setEffect(null);}});
		
		return ret; 
		
	}
	
	private ComboBox<String> createComboBox(String description, int numberOfOptions, EventHandler<ActionEvent> handler){
		
		ArrayList<String> options = new ArrayList<String>();
		
		for (int i = 1; i < numberOfOptions + 1 ; i++){
			String temp = myUIElements.getString(description + Integer.toString(i));
			if (! temp.equals(null)){
				options.add(temp);
			}
		} 
		
		ComboBox<String> box = new ComboBox<String>();	
		box.getItems().addAll(options.toArray(new String[options.size()]));
		box.setOnAction(handler);
		return box;
	}
	
	
	private Node makeUIPanel(){
		HBox panel = new HBox();
		
		startButton = createButton("startButton", new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
            	startButtonHandler();
            }});
		startButton.setDisable(true);
		stopButton = createButton("stopButton", new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
            	stopButtonHandler();
            }});
		stopButton.setDisable(true);
		stepButton = createButton("stepButton", new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
            	stepButtonHandler();
            }});
		stepButton.setDisable(true);
		resetButton = createButton("resetButton", new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
            	resetButtonHandler();
          
            }});
		resetButton.setDisable(true);
		simSetter = createComboBox("simSetter", NUMBER_OF_AVAILABLE_SIMULATIONS, new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
            	simSetterHandler();
            }});
		
		speedSetter = createComboBox("speedSetter", NUMBER_OF_AVAILABLE_SPEEDS, new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
            	speedSetterHandler();
          
            }});
		speedSetter.getSelectionModel().select(2);

		
		gridSizeSetter = createComboBox("gridSizeSetter", NUMBER_OF_AVAILABLE_SIZES, new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
            	gridSizeSetterHandler();
          
            }});
		
		cellShapeSetter = createComboBox("cellShapeSetter", NUMBER_OF_AVAILABLE_CELL_SHAPES, new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
            	cellShapeSetterHandler();
          
            }});
		panel.getChildren().add(startButton);
		panel.getChildren().add(stopButton);
		panel.getChildren().add(resetButton);
		panel.getChildren().add(stepButton);
		panel.getChildren().addAll(new Label("     " + myUIElements.getString("simSetterLabel") + "  "), new Text());
		panel.getChildren().add(simSetter);
		panel.getChildren().addAll(new Label("     " + myUIElements.getString("speedSetterLabel") + "  "), new Text());
		panel.getChildren().add(speedSetter);
		panel.getChildren().addAll(new Label("     " + myUIElements.getString("cellShapeLabel") + "  "), new Text());
		panel.getChildren().add(cellShapeSetter);
		panel.getChildren().addAll(new Label("     " + myUIElements.getString("gridSizeLabel") + "  "), new Text());
		panel.getChildren().add(gridSizeSetter);
		
		return panel; 
	}
	
	public void setDelegate(MainController controller){
		delegate = controller;
	}
		
	private void startButtonHandler(){
		startButton.setDisable(true);
		stopButton.setDisable(false);
		stepButton.setDisable(true);
		resetButton.setDisable(false);
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
		double newRate = Double.parseDouble(myUIElements.getString(newRatePercentage));
		delegate.changeSimulationSpeed(newRate);
	}
	
	private void gridSizeSetterHandler(){
		
	}
	
	private void cellShapeSetterHandler(){
		
	}
	
	public double getWindowWidth(){
		return myScene.getWindow().getWidth();
	}
	
	public double getWindowHeight(){
		return myScene.getWindow().getHeight();
	}
		
}