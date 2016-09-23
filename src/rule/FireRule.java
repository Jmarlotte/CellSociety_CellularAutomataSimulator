package rule;

public class FireRule extends Rule {

	private double probCatch;

	public FireRule(double prob) {
		probCatch = prob;
	}

	public double getProbCatch() {
		return probCatch;
	}

	public void setProbCatch(double probCatch) {
		this.probCatch = probCatch;
	}
	
}
