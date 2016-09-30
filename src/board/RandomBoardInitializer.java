package board;

import java.util.*;
import cell.*;
import io.FileParsingException;
import rule.*;

/**
 * Random Board Initializer
 * @author ZYL
 *
 */
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
				makeCellAndAssignToBoard(cellType, value, rule, board, i, j);
			}
		}
		return board;
	}

	public static Cell[][] fixedCountRandomInitialize(
			String cellType, Rule rule, int height, int width, 
			ArrayList<Integer> counts) throws FileParsingException {
		Cell[][] board = new Cell[height][width];
		ArrayList<Integer> values = new ArrayList<Integer>();
		int idx = 0;
		for(int c : counts)  {
			for(int i=0; i<c; i++) 
				values.add(idx);
			idx++;
		}
		if(height*width!=values.size())
			throw new FileParsingException("Sum of counts do not equal to number of cells on board");
		Collections.shuffle(values);
		int count = 0;
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				int value = values.get(count);
				makeCellAndAssignToBoard(cellType, value, rule, board, i, j);
				count++;
			}
		}
		return board;
	}

	private static void makeCellAndAssignToBoard(String cellType, int value, Rule rule, Cell[][] board, int i, int j)
			throws FileParsingException {
		Cell c = CellFactory.newCell(cellType, value, rule);
		c.setX(i);
		c.setY(j);
		board[i][j] = c;
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
