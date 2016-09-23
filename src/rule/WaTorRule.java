package rule;

public class WaTorRule extends Rule {

	private int initialHealth;
	private int minReproductionHealth;
	private int sharkReproductionInterval;
	private int fishReproductionInterval;
	private int fishEnergy; // amount of energy earned by eating fish
	
	public WaTorRule() {
		initialHealth = 100;
		minReproductionHealth = 0;
		sharkReproductionInterval = 10; 
		fishReproductionInterval = 5;
		fishEnergy = 5;
	}

	@Override
	public String toString() {
		return "WaTorRule [initialHealth=" + initialHealth + ", minReproductionHealth=" + minReproductionHealth
				+ ", sharkReproductionInterval=" + sharkReproductionInterval + ", fishReproductionInterval="
				+ fishReproductionInterval + ", fishEnergy=" + fishEnergy + "]";
	}

	public int getInitialHealth() {
		return initialHealth;
	}

	public void setInitialHealth(int initialHealth) {
		this.initialHealth = initialHealth;
	}

	public int getMinReproductionHealth() {
		return minReproductionHealth;
	}

	public void setMinReproductionHealth(int minReproductionHealth) {
		this.minReproductionHealth = minReproductionHealth;
	}

	public int getSharkReproductionInterval() {
		return sharkReproductionInterval;
	}

	public void setSharkReproductionInterval(int sharkReproductionInterval) {
		this.sharkReproductionInterval = sharkReproductionInterval;
	}

	public int getFishReproductionInterval() {
		return fishReproductionInterval;
	}

	public void setFishReproductionInterval(int fishReproductionInterval) {
		this.fishReproductionInterval = fishReproductionInterval;
	}

	public int getFishEnergy() {
		return fishEnergy;
	}

	public void setFishEnergy(int fishEnergy) {
		this.fishEnergy = fishEnergy;
	}
	
	
	
}
