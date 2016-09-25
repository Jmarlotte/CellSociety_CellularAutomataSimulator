package cell;

import rule.*;

public class CellFactory {

	public static Cell newCell(String type, int value, Rule rule) {
		if(type.equals("Reproduction")) {
			return new ReproductionCell(value, (ReproductionRule)rule);
		} else if(type.equals("Fire")) {
			return new FireCell(value, (FireRule)rule);
		} else if(type.equals("WaTor")) {
			return new WaTorCell(value, (WaTorRule)rule);
		} else if(type.equals("Segregation")) {
			return new SegregationCell(value, (SegregationRule)rule);
		}
		throw new RuntimeException("Unrecognized Cell Type");
	}
	
}
