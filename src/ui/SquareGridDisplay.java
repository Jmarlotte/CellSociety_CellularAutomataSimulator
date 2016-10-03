package ui;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class SquareGridDisplay extends GridDisplay {

	public SquareGridDisplay(int rows, int columns, double width, double height, List<CellDisplayInfo> cells) {
		super(rows, columns, width, height, cells);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void createCellShapes(Pane board, List<CellDisplayInfo> cells){
		List<Node> children = board.getChildren();
		for( CellDisplayInfo cell : cells){
			double offX = getCellOffsetX(cell.column);
			double offY = getCellOffsetY(cell.row);
			Rectangle newCell = new Rectangle(offX, offY, cellWidth, cellHeight);
			newCell.setFill(cell.color);
			cellShapes[cell.row][cell.column] = newCell;
			children.add(newCell);
		}
	}
	
	protected void setCellDimensions(int rows, int columns, double gridWidth, double gridHeight){
		cellWidth = gridWidth / columns;
		cellHeight = gridHeight / rows;
	}
	
	private double getCellOffsetX(int column){
		return column*cellWidth;
	}
	
	private double getCellOffsetY(int row){
		return row * cellHeight;
	}
	
//	protected double getCellWidth(int columns){
//		return gridWidth / columns;
//	}
//	
//	protected double getCellHeight(int rows){
//		return gridHeight / rows;
//	}
	
}
