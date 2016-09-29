package cell;

import java.util.Random;

import rule.FireRule;

public class FireCell extends Cell {

	public FireCell(int value, FireRule rule) {
		super(value, rule);
	}
	

	@Override
	public void prepareForUpdate() {
		updateFire();
	}
	
	private void updateFire() {
		if(this.getValue().getVal()==FireRule.TREE_TYPE) {
			boolean neighborBurning = false;
			for(Cell c : this.neighbors) {
				if(c.getValue().getVal()==FireRule.BURNING_TYPE) {
					neighborBurning = true;
					break;
				}
			}
			if(neighborBurning) {
				if(new Random().nextDouble()<((FireRule)rule).getProbCatch()) {
					this.nextValue.setVal(FireRule.BURNING_TYPE);
				}
			}
		} else { // regardless of empty or burning
			this.nextValue.setVal(FireRule.EMPTY_TYPE);
		}
	}
	
}
