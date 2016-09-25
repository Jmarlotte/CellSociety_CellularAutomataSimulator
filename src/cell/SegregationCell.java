package cell;

import rule.SegregationRule;

public class SegregationCell extends Cell {

	public SegregationCell(int value, SegregationRule rule) {
		super(value, rule);
	}

	@Override
	public void prepareForUpdate() { 
		// do nothing because segregation update rule is global
	}

}
