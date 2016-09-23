package cellsociety_team06;

import java.util.Random;

public class FireCell extends Cell {

	@Override
	public void prepareForUpdate() {
		updateFire();
	}
	
	private void updateFire() {
		if(this.getValue().getVal()==1) {
			boolean neighborBurning = false;
			for(Cell c : this.neighbors) {
				if(c.getValue().getVal()==2) {
					neighborBurning = true;
					break;
				}
			}
			if(neighborBurning) {
				if(new Random().nextDouble()<((FireRule)rule).getProbCatch()) {
					this.nextValue.setVal(2);
				}
			}
		} else { // regardless of empty or burning
			this.nextValue.setVal(0);
		}
	}
	
}
