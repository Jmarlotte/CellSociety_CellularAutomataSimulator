package cell;

import rule.ReproductionRule;

public class ReproductionCell extends Cell {

	public ReproductionCell(int value, ReproductionRule rule) {
		super(value, rule);
	}

	@Override
	public void prepareForUpdate() {
		updateReproduction();
	}
	
	private void updateReproduction() {
		int neighborCount = 0;
		for(Cell c : this.neighbors) {
			neighborCount += c.getValue().getVal();
		}
		if(this.value.getVal()==1) {
			if(((ReproductionRule)rule).getLiveNeighborCounts().contains(neighborCount)) {
				this.nextValue.setVal(1);
			} else {
				this.nextValue.setVal(0);
			}
		} else {
			if(((ReproductionRule)rule).getEmergeNeighborCounts().contains(neighborCount)) {
				this.nextValue.setVal(1);
			} else {
				this.nextValue.setVal(0);
			}
		}
	}
	
}
