package gui;

import java.util.*;

import cell.Cell;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
public class GUI {

	private Group root;
	private Scene scene;
	private ColorMap colorMap;
	
	// Screen is 800 x 600
	public static final double SCREEN_WIDTH = 800;
	public static final double SCREEN_HEIGHT = 600;
	
	// Board is 500 x 500
	public static final double BOARD_WIDTH = 500; 
	public static final double BOARD_HEIGHT = 500; 
	
	// Board upper left corner is (50, 50)
	public static final double BOARD_W_OFFSET = 50;
	public static final double BOARD_H_OFFSET = 50;
	
	private double cellWidth;
	private double cellHeight;
	private HashMap<Coordinate, Rectangle> cellViews;
	
	public Scene init(ColorMap cm, ArrayList<Cell> board, String title) {
		root = new Group();
		scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT, Color.WHITE);
		Text txt = new Text(SCREEN_WIDTH/2, BOARD_H_OFFSET/2, title);
		root.getChildren().add(txt);
		Rectangle border = new Rectangle(BOARD_W_OFFSET, BOARD_H_OFFSET, BOARD_WIDTH, BOARD_HEIGHT);
		border.setFill(Color.TRANSPARENT);
		border.setStroke(Color.BLACK);
		root.getChildren().add(border);
		colorMap = cm;
		cellViews = new HashMap<Coordinate, Rectangle>();
		int[] dim = getBoardDim(board);
		drawBoard(board, dim[0], dim[1]);
		return scene;
	}
	
	private int[] getBoardDim(ArrayList<Cell> board) {
		int r = 0;
		int c = 0;
		for(Cell cell : board) {
			if(r<cell.getX())
				r = cell.getX();
			if(c<cell.getY())
				c = cell.getY();
		}
		int[] dim = {r+1, c+1};
		return dim;
	}
	
	private void drawBoard(ArrayList<Cell> board, int wCount, int hCount) {
		cellWidth = BOARD_WIDTH / wCount;
		cellHeight = BOARD_HEIGHT / hCount;
		for(int i=0; i<hCount; i++) {
			for(int j=0; j<wCount; j++) {
				double yPos = i*cellHeight + BOARD_H_OFFSET;
				double xPos = j*cellWidth + BOARD_W_OFFSET;
				Rectangle rect = new Rectangle(xPos, yPos, cellWidth, cellHeight);
				cellViews.put(new Coordinate(i, j), rect);
				root.getChildren().add(rect);
			}
		}
		updateScreen(board);
	}
	
	public void updateScreen(ArrayList<Cell> board) {
		for(Cell c : board) {
			int x = c.getX();
			int y = c.getY();
			Rectangle rect = cellViews.get(new Coordinate(x, y));
			rect.setFill(colorMap.getColor(c.getValue().getVal()));
		}
	}
	
	public void step(double secondDelay) {
		// don't need to do anything now
	}
	
}
