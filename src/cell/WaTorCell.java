package cell;

import rule.WaTorRule;

/**
 * WaTor cell
 * @author ZYL
 *
 */
public class WaTorCell extends Cell {

	private int currentHealth;
	private int timeToReproduce;

	public WaTorCell(int value, WaTorRule rule) {
		super(value, rule);
		currentHealth = ((WaTorRule)this.rule).getInitialHealth();
		this.resetReproduceTimer();
	}
	
	@Override
	public String getSaveStr() {
		return String.format("%i %i %i %i %i", this.getX(), this.getY(), this.getValue().getVal(), 
				this.currentHealth, this.timeToReproduce);
	}

	public void step() {
		// fish: prepare to reproduce
		if(this.value.getVal()==WaTorRule.FISH_TYPE) {
			timeToReproduce--;
		} 
		// shark: prepare to reproduce if healthy, reset reproduce timer if unhealthy, decrement health
		if(this.value.getVal()==WaTorRule.SHARK_TYPE) {
			if(this.currentHealth>=((WaTorRule)this.rule).getMinReproductionHealth()) {
				timeToReproduce--;
			} else {
				resetReproduceTimer();
			}
			this.currentHealth--;
			if(this.currentHealth<=0) {
				this.changeType(WaTorRule.EMPTY_TYPE);
			}
		}
	}
	
	public void eat() {
		assert this.value.getVal()==WaTorRule.SHARK_TYPE; // only shark can eat
		this.currentHealth += ((WaTorRule)this.rule).getFishEnergy();
	}
	
	public void resetReproduceTimer() {
		if(this.value.getVal()==WaTorRule.FISH_TYPE) {
			timeToReproduce = ((WaTorRule)this.rule).getFishReproductionInterval();
		} else if(this.value.getVal()==WaTorRule.SHARK_TYPE) {
			timeToReproduce = ((WaTorRule)this.rule).getSharkReproductionInterval();
		}
	}
	
	public void changeType(int type) {
		this.value.setVal(type);
		currentHealth = ((WaTorRule)this.rule).getInitialHealth();
		this.resetReproduceTimer();
	}
	
	public void changeTypeAndKeepReprTimer(int type, int ttr) {
		this.value.setVal(type);
		currentHealth = ((WaTorRule)this.rule).getInitialHealth();
		this.timeToReproduce = ttr;
	}

	public void changeTypeAndKeepReprTimerAndKeepHealth(int type, int ttr, int health) {
//		System.out.println(String.format("\tChanging (%d,%d) to shark type", this.getX(), this.getY()));
		assert type==WaTorRule.SHARK_TYPE; // only shark has health
		this.value.setVal(type);
		this.currentHealth = health;
		this.timeToReproduce = ttr;
	}

	@Override
	public void prepareForUpdate() {
		// do not do anything since in WaTor the update rule is "global"
	}
	
	@Override
	public boolean update() {
		// do not do anything since in WaTor the update rule is "global"
		assert false; // crash the program now, since this function is not supposed to be called. 
		return false;
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
