// This entire file is part of my masterpiece.
// Yilun Zhou

/**This is class shows a Factory design pattern. Specifically, two functions
 * newCell() and newCellFromString() creates a cell from either a parameter 
 * specification or a String specification. 
 * 
 * The former method is called when a cell needs to be created from scratch, and 
 * all details of the cell is provided. 
 * 
 * The latter method is called when a board from a text description file is created. 
 * A text description file contains all information necessary to create a cell, and 
 * this method parses the string create cell. 
 * 
 * The factory pattern hides all details of Cell initialization, whose most remarkable 
 * feature is selecting different subclasses of Cell for different types. Thus, 
 * it when creating a new cell, the only part that needs to change for accomondating new 
 * cell is this class, rather than every place where a Cell is instantiated. 
*/

package cell;

import java.util.ArrayList;
import java.util.Arrays;

import io.FileParsingException;
import rule.*;

/**
 * Factory class to return cell according to type
 * @author ZYL
 *
 */
public class CellFactory {

	public static Cell newCell(String type, int value, Rule rule) throws FileParsingException {
		if(type.equals("Reproduction") || type.equals("NonTotalistic")) {
			return new ReproductionCell(value, rule);
		} else if(type.equals("Fire")) {
			return new FireCell(value, (FireRule)rule);
		} else if(type.equals("WaTor")) {
			return new WaTorCell(value, (WaTorRule)rule);
		} else if(type.equals("Segregation")) {
			return new SegregationCell(value, (SegregationRule)rule);
		}
		throw new FileParsingException("Unrecognized Cell Type");
	}

	public static Cell newCellFromString(String type, String cellStr, Rule rule) throws FileParsingException {
		checkType(type);
		try {
			String[] cellStrArr = cellStr.trim().split(" ");
			ArrayList<Integer> cellData = new ArrayList<Integer>();
			for(String s : cellStrArr) {
				cellData.add(Integer.parseInt(s));
			}
			int x = cellData.get(0);
			int y = cellData.get(1);
			int value = cellData.get(2);
			Cell c = null;
			if(type.equals("Reproduction")) {
				c = new ReproductionCell(value, (ReproductionRule)rule);
			} else if(type.equals("Fire")) {
				c = new FireCell(value, (FireRule)rule);
			} else if(type.equals("WaTor")) {
				c = new WaTorCell(value, (WaTorRule)rule);
				((WaTorCell)c).setCurrentHealth(cellData.get(3));
				((WaTorCell)c).setTimeToReproduce(cellData.get(4));
			} else if(type.equals("Segregation")) {
				c = new SegregationCell(value, (SegregationRule)rule);
			}
			c.setX(x);
			c.setY(y);
			return c;
		} catch (Exception e) {
			throw new FileParsingException("Wrong Cell String Format: "+cellStr);
		}
	}
	
	private static void checkType(String type) throws FileParsingException {
		ArrayList<String> allTypes = new ArrayList<String>(
				Arrays.asList(new String[] {"Reproduction", "Fire", "WaTor", "Segregation"}));
		if(!allTypes.contains(type)) {
			throw new FileParsingException("Unrecognized Cell Type");
		}
	}

}
