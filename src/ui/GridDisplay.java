package ui;

import java.util.List;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

public abstract class GridDisplay {
	private Pane board;
	protected Shape[][] cellShapes;
	protected double gridWidth;
	protected double gridHeight;
	protected double cellWidth;
	protected double cellHeight;
	
	public GridDisplay(int rows, int columns, double width, double height, List<CellDisplayInfo> cells){
		cellShapes = new Shape[rows][columns];
		gridWidth = width;
		gridHeight = height;
		setCellDimensions(rows, columns, width, height);
		System.out.println(cellWidth);
		System.out.println(cellHeight);
		board = new Pane();
		createCellShapes(board, cells);
	}
	
	protected abstract void createCellShapes(Pane board, List<CellDisplayInfo> cells);
//	{
//		List<Node> children = board.getChildren();
//		for( CellDisplayInfo cell : cells){
//			double offX = getCellOffsetX(cell.x);
//			double offY = getCellOffsetY(cell.y);
//			Rectangle newCell = new Rectangle(offX, offY, cellWidth, cellHeight);
//			newCell.setFill(cell.color);
//			cellShapes[cell.x][cell.y] = newCell;
//			children.add(newCell);
//		}
//	}
//	
	/**
	 * Update the screen for current board
	 * @param board
	 */
	public void updateBoard(List<CellDisplayInfo> changedCells) {
		for (CellDisplayInfo cell : changedCells){
			Shape s = cellShapes[cell.row][cell.column];
			s.setFill(cell.color);
		}
	}
	
	protected abstract void setCellDimensions(int rows, int columns, double gridWidth, double gridHeight);

	
	public Pane getBoard(){
		return board;
	}
}
