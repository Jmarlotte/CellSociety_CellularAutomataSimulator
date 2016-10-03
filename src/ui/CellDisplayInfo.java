package ui;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class CellDisplayInfo {
	public int row;
	public int column;
	public Paint color;
	
	public CellDisplayInfo(int xCoordinate, int yCoordinate, Color fillColor){
		column = xCoordinate;
		row = yCoordinate;
		color = fillColor; 
	}
}
