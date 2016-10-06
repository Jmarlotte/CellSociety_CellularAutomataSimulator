// This entire file is part of my masterpiece.
// Yilun Zhou

package cell;

import rule.*;

/**
 * Game of life cell
 * @author ZYL
 *
 */
public class ReproductionCell extends Cell {

	public ReproductionCell(int value, Rule rule) {
		super(value, rule);
	}

	@Override
	public void prepareForUpdate() {
		if(rule instanceof ReproductionRule)
			updateReproduction();
		else if(rule instanceof NonTotalisticRule)
			updateNonTotalistic();
	}

	private void updateReproduction() {
		int neighborCount = 0;
		for(Cell c : this.neighbors.toList()) {
			if(c instanceof InvalidCell)
				continue;
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

	private void updateNonTotalistic() {
		this.nextValue.setVal(((NonTotalisticRule)rule).nextValue(this));
	}

}
