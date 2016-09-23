package rule;

/**
 * This rule represents a ternary board pattern in which a cell 
 * can be unoccupied or occupied by a specific type of person. 
 * Cell value of 0 is unoccupied, 1 is X person, and 2 is O person. 
 * @author ZYL
 */
public class SegregationRule extends Rule {

	public static final int EMPTY_TYPE = 0;
	public static final int X_TYPE = 1;
	public static final int O_TYPE = 2;
	
	private double satisfactionThreshold = 0.5;
	
	public SegregationRule(double threshold) {
		satisfactionThreshold = threshold;
	}

	public double getSatisfactionThreshold() {
		return satisfactionThreshold;
	}

	public void setSatisfactionThreshold(double satisfactionThreshold) {
		this.satisfactionThreshold = satisfactionThreshold;
	}
	
	
}
