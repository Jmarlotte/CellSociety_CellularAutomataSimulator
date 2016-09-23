package rule;

/**
 * This rule represents a ternary board pattern in which a cell 
 * can be empty, tree, or burning. 
 * Cell value of 0 is unoccupied, 1 is tree, and 2 is burning. 
 * @author ZYL
 */
public class FireRule extends Rule {

	public static final int EMPTY_TYPE = 0; 
	public static final int TREE_TYPE = 1; 
	public static final int BURNING_TYPE = 2; 
	
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
