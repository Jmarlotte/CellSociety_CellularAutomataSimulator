package cell;

import rule.Rule;

public class CellFactory {

	public static Cell newCell(String type, Rule rule) {
		if(type.equals("Reproduction")) {
			return new ReproductionCell();
		} else if(type.equals("Fire")) {
			return new FireCell();
		} else if(type.equals("WaTor")) {
			return new WaTorCell(rule);
		}
		return null;
	}
	
}
