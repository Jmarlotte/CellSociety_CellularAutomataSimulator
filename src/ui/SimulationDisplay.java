package ui;
import java.util.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import main.MainController;

//This entire file is part of my masterpiece.
//Andrew Bihl (atb26)

//I believe this is good code because it has a single and clear responsibility. It represents the 
//display window as a whole, but does not contain the logic or code to create and configure its
//component elements. It provides an easy interface with which other parts of the project can communicate
// and provides an easy avenue for future UI modification.

public class SimulationDisplay {

private final String RESOURCE_PATH = "resources/DisplaySettings";

private ResourceBundle myResources;

private Scene myScene;
private Pane board;
private BorderPane root; 
private LineChart myChart;
private UIPanel panel;
private SimulationDisplayDelegate delegate;

	public SimulationDisplay(){
		myResources = ResourceBundle.getBundle(RESOURCE_PATH);
		root = new BorderPane();
		panel = new UIPanel();
		root.setTop(panel.getUIPanelNode());
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
		board = gridDisplay.getBoard();
	}
	
	public void addChart(LineChart lineChart){
		root.getChildren().remove(myChart);
		root.setRight(lineChart);
		myChart = lineChart;
	}
	
	public void setDelegate(MainController controller){
		delegate = controller;
		panel.setDelegate(controller);
	}
	
	public double getWindowWidth(){
		return myScene.getWindow().getWidth();
	}
	
	public double getWindowHeight(){
		return myScene.getWindow().getHeight();
	}
	
	public String getShapeSelection(){
		return panel.getShapeSelection();
	}
		
}