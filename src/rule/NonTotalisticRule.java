package rule;

import java.util.ArrayList;

import cell.Cell;

/**
 * Non Totalistic Rule
 * Each rule is represented by an ArrayList of String representing 
 * allowed neighbor configurations for survival and birth
 * Each string is of length equal to number of neighbors of a cell. 
 * Each character of the string represents the requirement for each neighbor: 
 * 0 means must not exist, 1 means exist, and x means do not care. 
 * The neighbors are sorted in a clockwise direction starting from upper-left corner. 
 * @author ZYL
 *
 */
public class NonTotalisticRule extends Rule {

	private ArrayList<String> surviveAllowed;
	private ArrayList<String> birthAllowed;
	
	public NonTotalisticRule(ArrayList<String> sAllowed, ArrayList<String> bAllowed) {
		surviveAllowed = sAllowed;
		birthAllowed = bAllowed;
	}
	
	public int nextValue(Cell c) {
		if(surviveAllowed.size()==0 && c.getValue().getVal()==ReproductionRule.OCCUPIED_TYPE) {
			return 0; // cannot survive
		}
		if(birthAllowed.size()==0 && c.getValue().getVal()==ReproductionRule.EMPTY_TYPE) {
			return 0; // cannot be born
		}
		
		return 0;
	}
	
	private ArrayList<Cell> sortNeighbors(Cell c) {
		return null;
	}
	
}
