package cellsociety_team06;

public class WaTorCell extends Cell {

	private int currentHealth;
	
	public WaTorCell() {
		currentHealth = ((WaTorRule)this.rule).getInitialHealth();
	}
	
	@Override
	public void prepareForUpdate() {
		// do not do anything since in WaTor the update rule is "global"
	}


	public int getCurrentHealth() {
		return currentHealth;
	}


	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
	}

}
