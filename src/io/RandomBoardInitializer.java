package io;

import java.util.*;
import cell.*;
import rule.*;

public class RandomBoardInitializer {

	/**
	 * Randomly initialize a board with independently random cell type. 
	 * This function does NOT connect neighbors. 
	 * @param height
	 * @param width
	 * @param numTypes
	 * @param ratios
	 * @return
	 */
	public static Cell[][] bernoulliRandomInitialize(
			String cellType, Rule rule, int height, int width, 
			ArrayList<Double> ratios) throws FileParsingException {
		Cell[][] board = new Cell[height][width];
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				int value = sampleDiscrete(ratios);
				Cell c = CellFactory.newCell(cellType, value, rule);
				c.setX(i);
				c.setY(j);
				board[i][j] = c;
			}
		}
		return board;
	}
	
	private static int sampleDiscrete(ArrayList<Double> pdf) {
		double p = new Random().nextDouble();
		double sum = 0;
		for(int i=0; i<pdf.size(); i++) {
			sum += pdf.get(i);
			if(p<sum) {
				return i;
			}
		}
		// this should not happen, except for numerical error
		return pdf.size()-1;
	}
	
}
