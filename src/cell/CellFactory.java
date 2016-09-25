package cell;

import rule.FireRule;
import rule.ReproductionRule;
import rule.Rule;
import rule.WaTorRule;

public class CellFactory {

	public static Cell newCell(String type, int value, Rule rule) {
		if(type.equals("Reproduction")) {
			return new ReproductionCell(value, (ReproductionRule)rule);
		} else if(type.equals("Fire")) {
			return new FireCell(value, (FireRule)rule);
		} else if(type.equals("WaTor")) {
			return new WaTorCell(value, (WaTorRule)rule);
		}
		return null;
	}
	
}
