package cellsociety_team06;
import java.util.*;
import cell.Cell;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
public class SimulationDisplay {

private final String RESOURCE_PATH = "resources/DisplaySettings";
private ResourceBundle myResources;
private GridPane grid;
private ComboBox speedBox;
private Scene myScene;
private Button stepButton;
private Button pauseButton;
private double gridWidth;
private double gridHeight;
private int rowCount;
private int columnCount;
	
	public SimulationDisplay(int width, int height){
		myResources = ResourceBundle.getBundle(RESOURCE_PATH);
		BorderPane root = new BorderPane();
		myScene = createScene(root);
		double windowSize = myScene.getWidth();
		createGrid(50, 50, windowSize, root);
//		root.getChildren().add(grid);
	}
	
	private Scene createScene(Parent root){
		int width = Integer.parseInt(myResources.getString("WindowWidth"));
		int height = Integer.parseInt(myResources.getString("WindowHeight"));
		Scene scene = new Scene(root, width, height, Color.BLACK);
		return scene;
	}
	
	private Shape[][] createGrid(int rows, int columns, double windowSize, BorderPane root){
		gridWidth = Integer.parseInt(myResources.getString("GridWidth"));
		gridHeight = gridWidth;
		double offsetX = (windowSize - gridWidth)/2;
		double offsetY = (windowSize-gridHeight)/2;
		double cellSize = (double)gridWidth / rows;
		Shape[][] cells = new Shape[rows][columns];
		for (int i = 0; i < rows; i++){
			double y = offsetY + cellSize*i;
			for (int j = 0; j < columns; j++){
				double x = offsetX + cellSize*j;
				Rectangle cell = new Rectangle(x, y, cellSize, cellSize);
				cells[i][j] = cell;
				if (i%2 == j % 2)
					cell.setFill(Color.WHITE);
				root.getChildren().add(cell);
			}
		}
		return cells;
	}
	
	private GridPane createGridPane(double windowSize){
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
	}
	
	public Scene getScene(){
		return myScene;
	}
	
	private double getCellSize(){
		//TODO: Calculate size of cell based on size of grid/# of cells
		return 0.0;
	}
	
	private double getCellOffsetX(int column){
		return 0.0;
	}
	
	private double getCellOffsetY(int row){
		return 0.0;
	}
	
	/**
	 * Update the screen for current board
	 * Each cell's position is cell.getX() and cell.getY()
	 * Each cell's value is cell.getValue().getVal()
	 * @param board
	 */
	public void updateScreen(ArrayList<Cell> board) {
		// TODO: Implement this
	}
	
}