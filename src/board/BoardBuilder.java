package board;

import java.util.ArrayList;

import com.sun.org.apache.bcel.internal.classfile.ConstantNameAndType;

import cell.Cell;
import cell.CellFactory;
import io.FileParsingException;
import rule.Rule;

/**
 * Build board from different types
 * @author ZYL
 *
 */
public class BoardBuilder {

	public static ArrayList<Cell> buildFullBoard(
			String type, int width, int height, Rule rule, 
			NeighborConnection nc, String[] cellStrs) 
					throws FileParsingException {
		Cell[][] boardArr = new Cell[height][width];
		for(String s : cellStrs) {
			Cell c = CellFactory.newCellFromString(type, s, rule);
			boardArr[c.getX()][c.getY()] = c;
		}
		// Set up neighbor connection
		setNeighborConnection(nc, boardArr);
		// Build ArrayList
		ArrayList<Cell> cellList = boardArrToList(width, height, boardArr);
		return cellList;
	}

	public static ArrayList<Cell> buildRandomBoard(
			String type, int width, int height, Rule rule, 
			NeighborConnection nc, ArrayList<Double> ratio) 
					throws FileParsingException {
		Cell[][] boardArr = RandomBoardInitializer.bernoulliRandomInitialize(
				type, rule, height, width, ratio);
		// Set up neighbor connection
		setNeighborConnection(nc, boardArr);
		// Build ArrayList
		ArrayList<Cell> cellList = boardArrToList(width, height, boardArr);
		return cellList;
	}

	public static ArrayList<Cell> buildDefaultNonDefaultBoard(
			String type, int width, int height, Rule rule, 
			NeighborConnection nc, int defaultCellVal, String nonDefaultCellValStr) 
					throws FileParsingException {
		Cell[][] boardArr = new Cell[height][width];
		// add cell and append rule
		for(int h=0; h<height; h++) {
			for(int w=0; w<width; w++) {
				boardArr[h][w] = CellFactory.newCell(type, defaultCellVal, rule);
				boardArr[h][w].setX(h);
				boardArr[h][w].setY(w);
			}
		}
		String[] nonDefaultCellValArr = nonDefaultCellValStr.split(";");
		writeNonDefaultVal(boardArr, nonDefaultCellValArr);
		// Add neighbors
		setNeighborConnection(nc, boardArr);
		ArrayList<Cell> boardList = boardArrToList(width, height, boardArr);
		return boardList;
	}

	private static void writeNonDefaultVal(Cell[][] boardArr, String[] nonDefaultCellValArr) {
		// Overwrite non-default cell values
		for(String s : nonDefaultCellValArr) {
			String[] info = s.split(",");
			int x = Integer.parseInt(info[0]);
			int y = Integer.parseInt(info[1]);
			int v = Integer.parseInt(info[2]);
			boardArr[x][y].setAllValue(v);
		}
	}

	private static ArrayList<Cell> boardArrToList(int width, int height, Cell[][] board) {
		ArrayList<Cell> cellList = new ArrayList<Cell>();
		for(int h=0; h<height; h++) {
			for(int w=0; w<width; w++) {
				cellList.add(board[h][w]);
			}
		}
		return cellList;
	}

	private static void setNeighborConnection(NeighborConnection nc, Cell[][] board) {
		if(nc.getType().equals(NeighborConnectionType.CUSTOM)) {
			System.out.println("Building custom connection");
			boolean[] connect = ((CustomNeighborConnection) nc).getConnect();
			setCustomNeighborConnection(connect, board);
		} else {
			setFourEightNeighborConnection(nc, board);
		}
	}

	private static void setFourEightNeighborConnection(NeighborConnection nc,
			Cell[][] board) {
		int height = board.length;
		int width = board[0].length;
		for(int h=0; h<height; h++) {
			for(int w=0; w<width; w++) {
				board[h][w].setNeighbors(new ArrayList<Cell>());
				connectAdjNeighbors(board, h, w, nc.isWrap());
				if(nc.getType().equals(NeighborConnectionType.EIGHT_NEIGHBOR))
					connectDiagNeighbors(board, h, w, nc.isWrap());
			}
		}
	}

	/**
	 * Set custom neighbor connection
	 * connect is an array of boolean of 8 numbers. 
	 * each entry in connect represents existence/non-existence of connection with a neighbor, 
	 * starting from upper-left corner and rotating clockwise. 
	 */
	private static void setCustomNeighborConnection(boolean[] connect, Cell[][] board) {
		int height = board.length;
		int width = board[0].length;
		assert connect.length==8;
		for(int h=0; h<height; h++) {
			for(int w=0; w<width; w++) {
				ArrayList<Cell> neighbors = new ArrayList<Cell>();
				if(connect[0] && h!=0 && w!=0)
					neighbors.add(board[h-1][w-1]);
				if(connect[1] && h!=0)
					neighbors.add(board[h-1][w]);
				if(connect[2] && h!=0 && w!=width-1)
					neighbors.add(board[h-1][w+1]);
				if(connect[3] && w!=width-1)
					neighbors.add(board[h][w+1]);
				if(connect[4] && h!=height-1 && w!=width-1)
					neighbors.add(board[h+1][w+1]);
				if(connect[5] && h!=height-1)
					neighbors.add(board[h+1][w]);
				if(connect[6] && h!=height-1 && w!=0)
					neighbors.add(board[h+1][w-1]);
				if(connect[7] && w!=width-1)
					neighbors.add(board[h][w-1]);
				board[h][w].setNeighbors(neighbors);
			}
		}
	}

	private static void connectAdjNeighbors(Cell[][] board, int x, int y, boolean wrap) {
		int height = board.length;
		int width = board[0].length;
		int[] diff = new int[] {-1,1};
		for(int dx : diff) {
			int newx = height+dx;
			if(newx<0 && wrap)
				newx = height-1;
			if(newx>height-1 && wrap)
				newx = 0;
			try {
				board[x][y].getNeighbors().add(board[newx][width]);
			} catch(IndexOutOfBoundsException e) {
				continue;
			}
		}
		for(int dy : diff) {
			int newy = width+dy;
			if(newy<0 && wrap)
				newy = width-1;
			if(newy>width-1 && wrap)
				newy = 0;
			try {
				board[x][y].getNeighbors().add(board[height][newy]);
			} catch(IndexOutOfBoundsException e) {
				continue;
			}
		}
	}

	private static void connectDiagNeighbors(Cell[][] board, int x, int y, boolean wrap) {
		int height = board.length;
		int width = board[0].length;
		int[] diff = new int[] {-1,1};
		for(int dx : diff) {
			for(int dy : diff) {
				int newx = height+dx;
				int newy = width+dy;
				if(newx<0 && wrap)
					newx = height-1;
				if(newx>height-1 && wrap)
					newx = 0;
				if(newy<0 && wrap)
					newy = width-1;
				if(newy>width-1 && wrap)
					newy = 0;
				try {
					board[x][y].getNeighbors().add(board[newx][newy]);
				} catch(IndexOutOfBoundsException e) {
					continue;
				}
			}
		}
	}

}
