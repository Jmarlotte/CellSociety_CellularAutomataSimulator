package cellsociety_team06;
import java.util.*;

import javax.swing.JComboBox;

import cell.Cell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
public class SimulationDisplay {

private final String RESOURCE_PATH = "resources/DisplaySettings";
private final String UIElements_Path = "resources/UIElements";
private ResourceBundle myResources;
private ResourceBundle myUIElements;
private ComboBox simSetter;
private Scene myScene;
private Button stepButton;
private Button stopButton;
private Button startButton;
private Button resetButton;
private ComboBox speedSetter;



private double gridWidth;
private double gridHeight;
private int rowCount;
private int columnCount;
private int numCells = 10000;
private Shape[][] cellShapes;

private Color[] colors;
double windowSize;
private BorderPane root; 
	
	public SimulationDisplay(int rows, int columns){
		myResources = ResourceBundle.getBundle(RESOURCE_PATH);
		myUIElements = ResourceBundle.getBundle(UIElements_Path);
		root = new BorderPane();
		root.setTop(makeUIPanel());
		
		myScene = createScene(root);
		rowCount = rows;
		columnCount = columns;
		windowSize = myScene.getWidth();
	//	createGrid(50, 50, windowSize, root);
		//	root.getChildren().add(grid);
		
	}
	
	private Scene createScene(Parent root){
		int width = Integer.parseInt(myResources.getString("WindowWidth"));
		int height = Integer.parseInt(myResources.getString("WindowHeight"));
		Scene scene = new Scene(root, width, height, Color.WHITE);
		return scene;
	}
	
//	private Shape[][] createGrid(int rows, int columns, double windowSize, BorderPane root){
//		gridWidth = Integer.parseInt(myResources.getString("GridWidth"));
//		gridHeight = gridWidth;
//		double offsetX = (windowSize - gridWidth)/2;
//		double offsetY = (windowSize-gridHeight)/2;
//		double cellSize = (double)gridWidth / rows;
//		Shape[][] cells = new Shape[rows][columns];
//		for (int i = 0; i < rows; i++){
//			double y = offsetY + cellSize*i;
//			for (int j = 0; j < columns; j++){
//				double x = offsetX + cellSize*j;
//				Rectangle cell = new Rectangle(x, y, cellSize, cellSize);
//				cells[i][j] = cell;
//				if (i%2 == j % 2)
//					cell.setFill(Color.WHITE);
//				root.getChildren().add(cell);
//			}
//		}
//		return cells;
//	}
	
/*	private GridPane createGridPane(double windowSize){
		GridPane grid = new GridPane();
		
		gridWidth = Integer.parseInt(myResources.getString("GridWidth"));
		gridHeight = Integer.parseInt(myResources.getString("GridHeight"));
		grid.setLayoutX((windowSize - gridWidth)/2);
		grid.setLayoutY((windowSize-gridHeight)/2);
		grid.setGridLinesVisible(true);
		grid.setHgap(0);
		grid.setVgap(0);
		for (int i = 0; i < 1; i++){
			Rectangle rect = new Rectangle(100, 100);
			rect.setFill(Color.RED);
			grid.add(rect, i, i);
		}
		return grid;
	} */
	
	public Scene getScene(){
		return myScene;
	}
	
	public void setColors(Color[] simColors){
		colors = simColors;
	}
	
	private double getCellWidth(){
		//TODO: Calculate size of cell based on size of grid/# of cell
		double gridArea = gridWidth*gridHeight;
		double sizeRatio = gridArea/numCells; 
		return Math.sqrt(sizeRatio); 
		
		//return 0.0;
	}
	
	private double getCellOffsetX(int column){
		return column*getCellWidth();
		
		//return 0.0;
	}
	
	private double getCellOffsetY(int row){
		return row *getCellWidth();
		
		//return 0.0;
	}
	
	/**
	 * Update the screen for current board
	 * Each cell's position is cell.getX() and cell.getY()
	 * Each cell's value is cell.getValue().getVal()
	 * @param board
	 */
	public void updateBoard(ArrayList<Cell> changedCells) {
		System.out.println("Updating "+changedCells.size()+" cells");
		for (Cell cell : changedCells){
			Shape s = cellShapes[cell.getX()][cell.getY()];
			Color newColor = colors[cell.getValue().getVal()];
			s.setFill(newColor);
		}
	}
	
	public void createBoard(ArrayList<Cell> board) {
		
		// TODO: Implement this
//		int numCellstoUpdate = board.size();
		gridWidth = Integer.parseInt(myResources.getString("GridWidth"));
		gridHeight = Integer.parseInt(myResources.getString("GridHeight"));
		int rowCount = (int)Math.sqrt(board.size());
		cellShapes = new Shape[rowCount][columnCount];
		ObservableList<Node> children = root.getChildren();
//		numCellstoUpdate -=1;
//		System.out.print(numCellstoUpdate);
		double offsetX = (windowSize - gridWidth)/2;
		double offsetY = (windowSize-gridHeight)/2;
		System.out.println("OFFSET: "+offsetX);
		for( Cell cell : board){
//			double OffY = getCellOffsetY(cell.getY()) + (myScene.getHeight()- gridHeight)/2; 
//			double OffX = getCellOffsetX(cell.getX()) + (myScene.getWidth()- gridWidth)/2;
			double offX = offsetX + getCellOffsetX(cell.getX());
			double offY = offsetY + getCellOffsetY(cell.getY());
			//change color assignment
//			Color cellFill = new Color(cell.getValue().getVal()/2.0, cell.getValue().getVal()/2.0, cell.getValue().getVal()/2.0, 1 );
			Color cellFill = colors[cell.getValue().getVal()];
			Rectangle newCell = new Rectangle( offX, offY, getCellWidth(), getCellWidth() );
			newCell.setFill(cellFill);
			cellShapes[cell.getX()][cell.getY()] = newCell;
//			cells[numCellstoUpdate] = cellVisElement;
//			numCellstoUpdate -= 1; 
			children.add(newCell);
		}
		
//		root.getChildren().addAll(cellShapes);
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
	
	private ComboBox<String> createComboBox(String description, EventHandler<ActionEvent> handler){
		
		ArrayList<String> options = new ArrayList<String>();
		
		for (int i = 1; i < 5 ; i++){
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
		
		stopButton = createButton("stopButton", new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
            	stopButtonHandler();
            }});
		
		stepButton = createButton("stepButton", new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
            	stepButtonHandler();
            }});
		
		resetButton = createButton("resetButton", new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
            	resetButtonHandler();
          
            }});
		
		simSetter = createComboBox("simSetter", new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
            	simSetterHandler();
          
            }});
		
		speedSetter = createComboBox("speedSetter", new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
            	speedSetterHandler();
          
            }});
		
		panel.getChildren().add(startButton);
		panel.getChildren().add(stopButton);
		panel.getChildren().add(resetButton);
		panel.getChildren().add(stepButton);
		panel.getChildren().addAll(new Label("     Simulation:  "), new Text());
		panel.getChildren().add(simSetter);
		panel.getChildren().addAll(new Label("     Speed:  "), new Text());
		panel.getChildren().add(speedSetter);
		
		return panel; 
		}
		
	private void startButtonHandler(){
		//TODO:
	}
		
	private void stopButtonHandler(){
		//TODO:
	}
	
	private void resetButtonHandler(){
		//TODO:
	}
	
	private void stepButtonHandler(){
		//TODO:
	}
		
	private void simSetterHandler(){
		//TODO:
	}
	
	private void speedSetterHandler(){
		//TODO:
	}
	
}