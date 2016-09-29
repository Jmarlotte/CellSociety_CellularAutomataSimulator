package global_stepper;

import java.util.ArrayList;

import cell.Cell;
import rule.SegregationRule;

/**
 * Stepper for segregation environment
 * @author ZYL
 *
 */
public class SegregationStepper extends BaseStepper {

	public static final int NUM_NEIGHBORS = 8;
	private double satisfactionThreshold;

	public SegregationStepper(ArrayList<Cell> board) {
		super(board);
		this.satisfactionThreshold = ((SegregationRule) board.get(0).getRule()).getSatisfactionThreshold();
	}

	@Override
	public void step() {
		ArrayList<Cell> xUnhappyCells = getUnhappyIdxs(SegregationRule.X_TYPE, SegregationRule.O_TYPE);
		ArrayList<Cell> oUnhappyCells = getUnhappyIdxs(SegregationRule.O_TYPE, SegregationRule.X_TYPE);
		relocate(xUnhappyCells);
		relocate(oUnhappyCells);
	}

	private void relocate(ArrayList<Cell> unhappyCells) {
		ArrayList<Cell> emptyCells = getCellsOfType(board, SegregationRule.EMPTY_TYPE);
		for(Cell thisCell : unhappyCells) {
			Cell targetCell = randomAccess(emptyCells);
			emptyCells.remove(targetCell);
			int type = thisCell.getValue().getVal();
			thisCell.getValue().setVal(SegregationRule.EMPTY_TYPE);
			targetCell.getValue().setVal(type);
			emptyCells.add(thisCell);
		}
	}

	private ArrayList<Cell> getUnhappyIdxs(int thisType, int otherType) {
		ArrayList<Cell> thisTypeCells = this.getCellsOfType(board, thisType);
		ArrayList<Cell> unhappyCells = new ArrayList<Cell>();
		for(Cell c : thisTypeCells) {
			int numDiff = this.getCellsOfType(c.getNeighbors(), otherType).size();
			if(numDiff/(float)NUM_NEIGHBORS < this.satisfactionThreshold) {
				unhappyCells.add(c);
			}
		}
		return unhappyCells;
	}

}
