package board;

import java.util.ArrayList;

import cell.Cell;
import cell.CellFactory;
import cell.CellNeighbors;
import io.FileParsingException;
import rule.Rule;

/**
 * Build board from different types
 * @author ZYL
 *
 */
public class BoardBuilder {

	public static ArrayList<Cell> fullBoard(
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
	
	public static ArrayList<Cell> randomCountsBoard(
			String type, int width, int height, Rule rule, 
			NeighborConnection nc, ArrayList<Integer> counts) 
					throws FileParsingException {
		Cell[][] boardArr = RandomBoardInitializer.fixedCountRandomInitialize(
				type, rule, height, width, counts);
		// Set up neighbor connection
		setNeighborConnection(nc, boardArr);
		// Build ArrayList
		ArrayList<Cell> cellList = boardArrToList(width, height, boardArr);
		return cellList;
	}

	public static ArrayList<Cell> randomBernoulliBoard(
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

	public static ArrayList<Cell> defaultNonDefaultBoard(
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
				connectAdjNeighbors(board, h, w, nc.isWrap());
				if(nc.getType().equals(NeighborConnectionType.EIGHT_NEIGHBOR))
					connectCornerNeighbors(board, h, w, nc.isWrap());
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
				CellNeighbors neighbors = board[h][w].getNeighbors();
				if(connect[0] && h!=0 && w!=0)
					neighbors.setTl(board[h-1][w-1]);
				if(connect[1] && h!=0)
					neighbors.setT(board[h-1][w]);
				if(connect[2] && h!=0 && w!=width-1)
					neighbors.setTr(board[h-1][w+1]);
				if(connect[3] && w!=width-1)
					neighbors.setR(board[h][w+1]);
				if(connect[4] && h!=height-1 && w!=width-1)
					neighbors.setBr(board[h+1][w+1]);
				if(connect[5] && h!=height-1)
					neighbors.setB(board[h+1][w]);
				if(connect[6] && h!=height-1 && w!=0)
					neighbors.setBl(board[h+1][w-1]);
				if(connect[7] && w!=width-1)
					neighbors.setL(board[h][w-1]);
			}
		}
	}

	private static void connectAdjNeighbors(Cell[][] board, int i, int j, boolean wrap) {
		int height = board.length;
		int width = board[0].length;
		connectT(board, i, j, wrap, height);
		connectB(board, i, j, wrap, height);
		connectL(board, i, j, wrap, width);
		connectR(board, i, j, wrap, width);
		
	}

	private static void connectR(Cell[][] board, int i, int j, boolean wrap, int width) {
		int newj = j + 1;
		if(newj<0 && wrap)
			newj = width-1;
		if(newj>width-1 && wrap)
			newj = 0;
		try {
			board[i][j].getNeighbors().setR(board[i][newj]);
		} catch(IndexOutOfBoundsException e) { }
	}

	private static void connectL(Cell[][] board, int i, int j, boolean wrap, int width) {
		int newj = j - 1;
		if(newj<0 && wrap)
			newj = width-1;
		if(newj>width-1 && wrap)
			newj = 0;
		try {
			board[i][j].getNeighbors().setL(board[i][newj]);
		} catch(IndexOutOfBoundsException e) { }
	}

	private static void connectB(Cell[][] board, int i, int j, boolean wrap, int height) {
		int newi = i + 1;
		if(newi<0 && wrap)
			newi = height-1;
		if(newi>height-1 && wrap)
			newi = 0;
		try {
			board[i][j].getNeighbors().setB(board[newi][j]);
		} catch(IndexOutOfBoundsException e) { }
	}

	private static void connectT(Cell[][] board, int i, int j, boolean wrap, int height) {
		int newi = i - 1;
		if(newi<0 && wrap)
			newi = height-1;
		if(newi>height-1 && wrap)
			newi = 0;
		try {
			board[i][j].getNeighbors().setT(board[newi][j]);
		} catch(IndexOutOfBoundsException e) { }
	}
	
	private static void connectTl(Cell[][] board, int i, int j, boolean wrap, int height, int width) {
		int newi = i - 1;
		int newj = j - 1;
		if(newi<0 && wrap)
			newi = height-1;
		if(newi>height-1 && wrap)
			newi = 0;
		if(newj<0 && wrap)
			newj = width-1;
		if(newj>width-1 && wrap)
			newj = 0;
		try {
			board[i][j].getNeighbors().setTl(board[newi][newj]);
		} catch(IndexOutOfBoundsException e) { }
	}
	
	private static void connectTr(Cell[][] board, int i, int j, boolean wrap, int height, int width) {
		int newi = i - 1;
		int newj = j + 1;
		if(newi<0 && wrap)
			newi = height-1;
		if(newi>height-1 && wrap)
			newi = 0;
		if(newj<0 && wrap)
			newj = width-1;
		if(newj>width-1 && wrap)
			newj = 0;
		try {
			board[i][j].getNeighbors().setTr(board[newi][newj]);
		} catch(IndexOutOfBoundsException e) { }
	}
	
	private static void connectBr(Cell[][] board, int i, int j, boolean wrap, int height, int width) {
		int newi = i + 1;
		int newj = j + 1;
		if(newi<0 && wrap)
			newi = height-1;
		if(newi>height-1 && wrap)
			newi = 0;
		if(newj<0 && wrap)
			newj = width-1;
		if(newj>width-1 && wrap)
			newj = 0;
		try {
			board[i][j].getNeighbors().setBr(board[newi][newj]);
		} catch(IndexOutOfBoundsException e) { }
	}
	
	private static void connectBl(Cell[][] board, int i, int j, boolean wrap, int height, int width) {
		int newi = i + 1;
		int newj = j - 1;
		if(newi<0 && wrap)
			newi = height-1;
		if(newi>height-1 && wrap)
			newi = 0;
		if(newj<0 && wrap)
			newj = width-1;
		if(newj>width-1 && wrap)
			newj = 0;
		try {
			board[i][j].getNeighbors().setBl(board[newi][newj]);
		} catch(IndexOutOfBoundsException e) { }
	}

	private static void connectCornerNeighbors(Cell[][] board, int i, int j, boolean wrap) {
		int height = board.length;
		int width = board[0].length;
		connectTl(board, i, j, wrap, height, width);
		connectTr(board, i, j, wrap, height, width);
		connectBr(board, i, j, wrap, height, width);
		connectBl(board, i, j, wrap, height, width);
	}

}
