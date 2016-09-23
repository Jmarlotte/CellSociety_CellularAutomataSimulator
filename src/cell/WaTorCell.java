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
		// fish: prepare to reproduce
		if(this.value.getVal()==1) {
			timeToReproduce--;
		} 
		// shark: prepare to reproduce if healthy, reset reproduce timer if unhealthy, decrement health
		if(this.value.getVal()==2) {
			if(this.currentHealth>=((WaTorRule)this.rule).getMinReproductionHealth()) {
				timeToReproduce--;
			} else {
				resetReproduceTimer();
			}
			this.currentHealth--;
		}
	}
	
	public void eat() {
		assert this.value.getVal()==2; // only shark can eat
		this.currentHealth += ((WaTorRule)this.rule).getFishEnergy();
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
		currentHealth = ((WaTorRule)this.rule).getInitialHealth();
		this.resetReproduceTimer();
	}
	
	public void changeType(int type, int ttr) {
		this.value.setVal(type);
		currentHealth = ((WaTorRule)this.rule).getInitialHealth();
		this.timeToReproduce = ttr;
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
