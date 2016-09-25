package global_stepper;

import java.util.ArrayList;
import java.util.Collections;

import cell.Cell;
import cell.WaTorCell;
import rule.WaTorRule;

public class WaTorStepper extends BaseStepper {

	public WaTorStepper(ArrayList<Cell> board) {
		super(board);
	}
	
	/**
	 * Stepping in WaTor
	 * 1. randomly shuffle the board to change update sequence
	 * 2. update fish. for each fish, move to a nearby position. reproduce as needed
	 * 3. update shark. for each shark, move to a nearby position. eat fish if possible. reproduce as needed
	 * 
	 */
	@Override
	public void step() {
		Collections.shuffle(board); // randomly shuffle the board
		updateFish();
		updateShark();
	}

	private void updateShark() {
		// shark update
		ArrayList<Integer> sharkIndices = getIndicesOfType(board, WaTorRule.SHARK_TYPE);
		for(int idx : sharkIndices) {
			WaTorCell thisCell = (WaTorCell) board.get(idx);
			thisCell.step();
			ArrayList<Integer> neighborIdxs = 
					this.getIndicesOfType(thisCell.getNeighbors(), WaTorRule.EMPTY_TYPE);
			neighborIdxs.addAll(this.getIndicesOfType(thisCell.getNeighbors(), WaTorRule.FISH_TYPE));
			if(neighborIdxs.isEmpty()) // no empty or fish cell
				continue;
			int targetIdx = randomAccess(neighborIdxs);
			WaTorCell targetCell = (WaTorCell)board.get(targetIdx);
			if(targetCell.getValue().getVal()==WaTorRule.FISH_TYPE) {
				// eating a fish
				thisCell.eat();
			}
			// change type of neighbor cell, but keep reproduce timer and current health
			targetCell.changeTypeAndKeepReprTimerAndKeepHealth(WaTorRule.SHARK_TYPE, 
					thisCell.getTimeToReproduce(), thisCell.getCurrentHealth());
			checkReproduction(thisCell, targetCell);
		}
	}

	private void updateFish() {
		// fish update
		ArrayList<Integer> fishIndices = getIndicesOfType(board, WaTorRule.FISH_TYPE);
		for(int idx : fishIndices) {
			WaTorCell thisCell = (WaTorCell) board.get(idx);
			thisCell.step();
			ArrayList<Integer> emptyNeighborIdxs = 
					this.getIndicesOfType(thisCell.getNeighbors(), WaTorRule.EMPTY_TYPE);
			if(emptyNeighborIdxs.isEmpty()) // no empty cell
				continue;
			int targetIdx = randomAccess(emptyNeighborIdxs);
			WaTorCell targetCell = (WaTorCell)board.get(targetIdx);
			// change type of the neighbor cell, but keep reproduce timer
			targetCell.changeTypeAndKeepReprTimer(WaTorRule.FISH_TYPE, thisCell.getTimeToReproduce());
			checkReproduction(thisCell, targetCell);
		}
	}

	private void checkReproduction(WaTorCell thisCell, WaTorCell targetCell) {
		// check reproduction
		if(thisCell.getTimeToReproduce()<=0) {
			// do not change cell type. just reset the timer to represent new shark
			thisCell.resetReproduceTimer();
			targetCell.resetReproduceTimer();
		} else {
			// otherwise, empty the cell
			thisCell.changeType(WaTorRule.EMPTY_TYPE);
		}
	}
	
	
	
}
