// This entire file is part of my masterpiece.
// Yilun Zhou

/**
 * This is an instance for a Cell subclass. This class
 * does not afford local update since cell "moving" is a "global"
 * behavior. Thus, the prepareForUpdate() is left empty and the 
 * update is handled by SegregationStepper in global_stepper package.
 * 
 */

package cell;

import rule.SegregationRule;

/**
 * Segregation cell
 * @author ZYL
 *
 */
public class SegregationCell extends Cell {

	public SegregationCell(int value, SegregationRule rule) {
		super(value, rule);
	}

	@Override
	public void prepareForUpdate() { 
		// do nothing because segregation update rule is global
	}

}
