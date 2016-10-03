package ui;

import java.util.ArrayList;
import java.util.List;

import cell.Cell;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class GridDisplay {
	private Pane board;
	private Shape[][] cellShapes;
	private double cellWidth;
	private double cellHeight;
	
	public GridDisplay(int rows, int columns, double width, double height, List<CellDisplayInfo> cells){
		cellShapes = new Shape[rows][columns];
		cellWidth = getCellWidth(width, columns);
		cellHeight = getCellHeight(height, rows);
		board = new Pane();
		List<Node> children = board.getChildren();
		createCellShapes(board, cells);
		for( CellDisplayInfo cell : cells){
			double offX = getCellOffsetX(cell.x);
			double offY = getCellOffsetY(cell.y);
			Rectangle newCell = new Rectangle(offX, offY, cellWidth, cellHeight);
			newCell.setFill(cell.color);
			cellShapes[cell.x][cell.y] = newCell;
			children.add(newCell);
		}
	}
	
	private void createCellShapes(Pane board, List<CellDisplayInfo> cells){
		List<Node> children = board.getChildren();
		for( CellDisplayInfo cell : cells){
			double offX = getCellOffsetX(cell.x);
			double offY = getCellOffsetY(cell.y);
			Rectangle newCell = new Rectangle(offX, offY, cellWidth, cellHeight);
			newCell.setFill(cell.color);
			cellShapes[cell.x][cell.y] = newCell;
			children.add(newCell);
		}
	}
	
	/**
	 * Update the screen for current board
	 * @param board
	 */
	public void updateBoard(List<CellDisplayInfo> changedCells) {
		for (CellDisplayInfo cell : changedCells){
			Shape s = cellShapes[cell.x][cell.y];
			s.setFill(cell.color);
		}
	}
	
	private double getCellWidth(double gridWidth, int columns){
		System.out.println("gridWidth: "+gridWidth+"columns"+columns);
		return gridWidth / columns;
	}
	
	private double getCellHeight(double gridHeight, int rows){
		return gridHeight / rows;
	}
	
	private double getCellOffsetX(int column){
		return column*cellWidth;
	}
	
	private double getCellOffsetY(int row){
		return row * cellHeight;
	}
	
	public Pane getBoard(){
		return board;
	}
}
