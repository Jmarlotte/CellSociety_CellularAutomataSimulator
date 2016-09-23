package global_stepper;

import java.util.ArrayList;

import cell.Cell;
import rule.SegregationRule;

public class SegregationStepper extends BaseStepper {

	public static final int NUM_NEIGHBORS = 8;
	private double satisfactionThreshold;
	
	public SegregationStepper(ArrayList<Cell> board) {
		super(board);
		this.satisfactionThreshold = ((SegregationRule) board.get(0).getRule()).getSatisfactionThreshold();
	}

	@Override
	public void step() {
		ArrayList<Integer> xUnhappyIdxs = getUnhappyIdxs(SegregationRule.X_TYPE, SegregationRule.O_TYPE);
		ArrayList<Integer> oUnhappyIdxs = getUnhappyIdxs(SegregationRule.O_TYPE, SegregationRule.X_TYPE);
		relocate(xUnhappyIdxs);
		relocate(oUnhappyIdxs);
	}
	
	private void relocate(ArrayList<Integer> unhappyIdxs) {
		for(int idx : unhappyIdxs) {
			Cell thisCell = board.get(idx);
			Cell targetCell = board.get(randomAccess(getIndicesOfType(board, SegregationRule.EMPTY_TYPE)));
			int type = thisCell.getValue().getVal();
			thisCell.getValue().setVal(SegregationRule.EMPTY_TYPE);
			targetCell.getValue().setVal(type);
		}
	}
	
	private ArrayList<Integer> getUnhappyIdxs(int thisType, int otherType) {
		ArrayList<Integer> thisTypeIdxs = this.getIndicesOfType(board, thisType);
		ArrayList<Integer> unhappyIdxs = new ArrayList<Integer>();
		for(int idx : thisTypeIdxs) {
			int numDiff = this.getIndicesOfType(board.get(idx).getNeighbors(), otherType).size();
			if(numDiff/(float)NUM_NEIGHBORS < this.satisfactionThreshold) {
				unhappyIdxs.add(idx);
			}
		}
		return unhappyIdxs;
	}

}
