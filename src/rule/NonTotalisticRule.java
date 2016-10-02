package rule;

import java.util.ArrayList;

import cell.Cell;
import cell.CellNeighbors;

/**
 * Non Totalistic Rule
 * Each rule is represented by an ArrayList of String representing 
 * allowed neighbor configurations for survival and birth
 * Each string is of length equal to number of neighbors of a cell. 
 * Each character of the string represents the requirement for each neighbor: 
 * 0 means must not exist, 1 means exist, and x means do not care. 
 * Invalid cell always satisfies condition. 
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
	
	/**
	 * Check if CellNeighbors of c is allowed by String s
	 * @param c
	 * @param s
	 * @return
	 */
	private boolean allowed(Cell c, String s) {
		ArrayList<Integer> values = c.getNeighbors().getValues();
		assert values.size()==s.length():"Length mismatch";
		for(int i=0; i<s.length();i++) {
			int v = values.get(i);
			char ch = s.charAt(i);
			if(v==-1 || ch=='x')
				continue;
			if(v!=Integer.parseInt(Character.toString(ch)))
				return false;
		}
		return true;
	}
	
}
