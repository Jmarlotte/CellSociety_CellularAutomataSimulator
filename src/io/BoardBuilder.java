package io;

import java.util.ArrayList;

import cell.Cell;
import cell.CellFactory;
import rule.Rule;

/**
 * Build board from different types
 * @author ZYL
 *
 */
public class BoardBuilder {

	public static ArrayList<Cell> buildFullBoard(
			String type, int width, int height, Rule rule, 
			int connection, String[] cellStrs) 
					throws FileParsingException {
		Cell[][] boardArr = new Cell[height][width];
		for(String s : cellStrs) {
			Cell c = CellFactory.newCellFromString(type, s, rule);
			boardArr[c.getX()][c.getY()] = c;
		}
		// Set up neighbor connection
		setNeighborConnection(width, height, connection, boardArr);
		// Build ArrayList
		ArrayList<Cell> cellList = boardArrToList(width, height, boardArr);
		return cellList;
	}


	public static ArrayList<Cell> buildRandomBoard(
			String type, int width, int height, Rule rule, 
			int connection, ArrayList<Double> ratio) 
					throws FileParsingException {
		Cell[][] boardArr = RandomBoardInitializer.bernoulliRandomInitialize(
				type, rule, height, width, ratio);
		// Set up neighbor connection
		setNeighborConnection(width, height, connection, boardArr);
		// Build ArrayList
		ArrayList<Cell> cellList = boardArrToList(width, height, boardArr);
		return cellList;
	}

	public static ArrayList<Cell> buildDefaultNonDefaultBoard(
			String type, int width, int height, Rule rule, 
			int connection, int defaultCellVal, String nonDefaultCellValStr) 
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
		// Overwrite non-default cell values
		for(String s : nonDefaultCellValArr) {
			String[] info = s.split(",");
			int x = Integer.parseInt(info[0]);
			int y = Integer.parseInt(info[1]);
			int v = Integer.parseInt(info[2]);
			boardArr[x][y].setAllValue(v);
		}
		// Add neighbors
		setNeighborConnection(width, height, connection, boardArr);
		ArrayList<Cell> boardList = boardArrToList(width, height, boardArr);
		return boardList;
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

	private static void setNeighborConnection(int width, int height, int connection, Cell[][] board) {
		for(int h=0; h<height; h++) {
			for(int w=0; w<width; w++) {
				ArrayList<Cell> neighbors = new ArrayList<Cell>();
				if(h!=0)
					neighbors.add(board[h-1][w]);
				if(w!=0)
					neighbors.add(board[h][w-1]);
				if(h!=height-1)
					neighbors.add(board[h+1][w]);
				if(w!=width-1)
					neighbors.add(board[h][w+1]);
				if(connection==8) {
					if(h!=0 && w!=0)
						neighbors.add(board[h-1][w-1]);
					if(h!=0 && w!=width-1)
						neighbors.add(board[h-1][w+1]);
					if(h!=height-1 && w!=0)
						neighbors.add(board[h+1][w-1]);
					if(h!=height-1 && w!=width-1)
						neighbors.add(board[h+1][w+1]);
				}
				board[h][w].setNeighbors(neighbors);
			}
		}
	}

}
