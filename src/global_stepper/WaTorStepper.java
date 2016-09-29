package global_stepper;

import java.util.ArrayList;
import java.util.Collections;

import cell.Cell;
import cell.WaTorCell;
import rule.WaTorRule;

/**
 * Stepper for WaTor environment
 * @author ZYL
 *
 */
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
		ArrayList<Cell> sharkCells = getCellsOfType(board, WaTorRule.SHARK_TYPE);

		for(Cell thisC : sharkCells) {
			WaTorCell thisCell = (WaTorCell) thisC;
			thisCell.step();
			if(thisCell.getValue().getVal()==WaTorRule.EMPTY_TYPE) {
				continue;
			}
			ArrayList<Cell> neighborCells = 
					this.getCellsOfType(thisCell.getNeighbors(), WaTorRule.EMPTY_TYPE);
			neighborCells.addAll(this.getCellsOfType(thisCell.getNeighbors(), WaTorRule.FISH_TYPE));
			if(neighborCells.isEmpty()) {
				continue;
			}
			
			WaTorCell targetCell = (WaTorCell)randomAccess(neighborCells);
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
		ArrayList<Cell> fishCells = getCellsOfType(board, WaTorRule.FISH_TYPE);
		for(Cell thisC : fishCells) {
			WaTorCell thisCell = (WaTorCell) thisC;
			thisCell.step();
			ArrayList<Cell> emptyNeighborCells = 
					this.getCellsOfType(thisCell.getNeighbors(), WaTorRule.EMPTY_TYPE);
			if(emptyNeighborCells.isEmpty()) // no empty cell
				continue;
			WaTorCell targetCell = (WaTorCell) randomAccess(emptyNeighborCells);
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
			thisCell.setCurrentHealth(((WaTorRule)thisCell.getRule()).getInitialHealth());
		} else {
			// otherwise, empty the cell
			thisCell.changeType(WaTorRule.EMPTY_TYPE);
		}
	}
	
	
	
}
