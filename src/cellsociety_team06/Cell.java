package cellsociety_team06;

import java.util.*;

public class Cell {

	private ArrayList<Cell> neighbors;
	private CellValue value;
	private CellValue nextValue;
	private Rule rule;
	
	/**
	 * Prepare for update. Change nextValue based on neighbors.
	 * Needs to consider rule. 
	 */
	public void prepareForUpdate() {
		
	};
	
	/**
	 * update. Replace value with nextValue.
	 */
	public void update() {
		value = nextValue;
	}
	
}
