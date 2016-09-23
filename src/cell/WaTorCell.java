package cell;

import rule.WaTorRule;

public class WaTorCell extends Cell {

	private int currentHealth;
	private int timeToReproduce;

	public WaTorCell(int value, WaTorRule rule) {
		super(value, rule);
		currentHealth = ((WaTorRule)this.rule).getInitialHealth();
		this.resetReproduceTimer();
	}

	public void step() {
		// prepare to reproduce if fish OR (shark AND healthy)
		if(this.value.getVal()==1 || 
				(this.value.getVal()==2 && 
				this.currentHealth>=((WaTorRule)this.rule).getMinReproductionHealth())) {
			timeToReproduce--;
		}
		// decrement health if fish OR shark
		if(this.value.getVal()==1 || this.value.getVal()==2) {
			this.currentHealth--;
		}
	}
	
	public void resetReproduceTimer() {
		if(this.value.getVal()==1) {
			timeToReproduce = ((WaTorRule)this.rule).getFishReproductionInterval();
		} else if(this.value.getVal()==2) {
			timeToReproduce = ((WaTorRule)this.rule).getSharkReproductionInterval();
		}
	}
	
	public void changeType(int type) {
		this.value.setVal(type);
		this.resetReproduceTimer();
	}

	@Override
	public void prepareForUpdate() {
		// do not do anything since in WaTor the update rule is "global"
	}
	
	@Override
	public void update() {
		// do not do anything since in WaTor the update rule is "global"
		assert false; // crash the program now, since this function is not supposed to be called. 
	}


	public int getCurrentHealth() {
		return currentHealth;
	}


	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
	}

	public int getTimeToReproduce() {
		return timeToReproduce;
	}

	public void setTimeToReproduce(int timeToReproduce) {
		this.timeToReproduce = timeToReproduce;
	}

}
