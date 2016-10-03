package ui;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class CellDisplayInfo {
	public int x;
	public int y;
	public Paint color;
	
	public CellDisplayInfo(int xCoordinate, int yCoordinate, Color fillColor){
		x = xCoordinate;
		y = yCoordinate;
		color = fillColor; 
	}
}
