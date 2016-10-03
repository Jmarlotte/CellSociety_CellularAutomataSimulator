package ui;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;

public class TriangleGridDisplay extends GridDisplay {

	private final double LINE_SPACING = 0.2;
	
	public TriangleGridDisplay(int rows, int columns, double width, double height, List<CellDisplayInfo> cells) {
		super(rows, columns, width, height, cells);
	}

	@Override
	protected void createCellShapes(Pane board, List<CellDisplayInfo> cells) {
		List<Node> children = board.getChildren();
		for( CellDisplayInfo cell : cells){
			Polygon triangle = makeTriangle(cell.row, cell.column);
			triangle.setFill(cell.color);
			cellShapes[cell.row][cell.column] = triangle;
			children.add(triangle);
		}
	}

	
	protected void setCellDimensions(int rows, int columns, double gridWidth, double gridHeight){
		cellHeight = gridHeight / rows;
		//Proportions of a 30-60-90 triangle
		cellWidth = (2* cellHeight) / Math.sqrt(3);
	}

	private Polygon makeTriangle(int row, int column){
		int trianglePointsDown = 0;
		int trianglePointsUp = 1;
		if (column % 2 == row % 2){
			trianglePointsDown = 1;
			trianglePointsUp = 0;
		}
		//location of left-most point
		double xOffset = cellWidth*(column/2) + (column % 2)*(cellWidth/2);
		//Very small offset between rows
		double yOffset = row * LINE_SPACING;
		double x1 = xOffset;
		double y1 = cellHeight * (row + trianglePointsUp) + yOffset;
		double x2 = xOffset + cellWidth;
		double y2 = y1;
		double x3 = xOffset + cellWidth*0.5;
		double y3 = cellHeight*(row+trianglePointsDown) + yOffset;
		Polygon cell = new Polygon();
		cell.getPoints().addAll(new Double[]{
				x1,y1,
				x2,y2,
				x3,y3,
		});
		return cell;
	}
}
