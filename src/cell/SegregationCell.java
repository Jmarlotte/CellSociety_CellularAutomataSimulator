package cell;

import rule.Rule;

public class SegregationCell extends Cell {

	public SegregationCell(int value, Rule rule) {
		super(value, rule);
	}

	@Override
	public void prepareForUpdate() { 
		// do nothing because segregation update rule is global
	}

}
