package global_simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import cell.Cell;
import cell.WaTorCell;
import rule.WaTorRule;

public class WaTorSimulator {

	private ArrayList<Cell> board;
	
	public WaTorSimulator(ArrayList<Cell> board) {
		this.board = board;
	}
	
	/**
	 * Stepping in WaTor
	 * 1. randomly shuffle the board to change update sequence
	 * 2. update fish. for each fish, move to a nearby position. reproduce as needed
	 * 3. update shark. for each shark, move to a nearby position. eat fish if possible. reproduce as needed
	 * 
	 */
	public void stepWaTor() {
		Collections.shuffle(board); // randomly shuffle the board
		updateFish();
		updateShark();
	}

	private void updateShark() {
		// shark update
		ArrayList<Integer> sharkIndices = getIndicesOfType(WaTorRule.SHARK_TYPE);
		for(int idx : sharkIndices) {
			WaTorCell thisCell = (WaTorCell) board.get(idx);
			thisCell.step();
			ArrayList<Integer> neighborIdxs = 
					this.getNeighborIndices(thisCell.getNeighbors(), WaTorRule.EMPTY_TYPE);
			neighborIdxs.addAll(this.getNeighborIndices(thisCell.getNeighbors(), WaTorRule.FISH_TYPE));
			if(neighborIdxs.isEmpty()) // no empty or fish cell
				continue;
			int targetIdx = neighborIdxs.get(new Random().nextInt(neighborIdxs.size()));
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
		ArrayList<Integer> fishIndices = getIndicesOfType(WaTorRule.FISH_TYPE);
		for(int idx : fishIndices) {
			WaTorCell thisCell = (WaTorCell) board.get(idx);
			thisCell.step();
			ArrayList<Integer> emptyNeighborIdxs = 
					this.getNeighborIndices(thisCell.getNeighbors(), WaTorRule.EMPTY_TYPE);
			if(emptyNeighborIdxs.isEmpty()) // no empty cell
				continue;
			int targetIdx = emptyNeighborIdxs.get(new Random().nextInt(emptyNeighborIdxs.size()));
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
	
	private ArrayList<Integer> getIndicesOfType(int type) {
		ArrayList<Integer> indices = new ArrayList<Integer>();
		int idx = 0;
		for(Cell c : board) {
			if(c.getValue().getVal()==type) {
				indices.add(idx);
			}
			idx++;
		}
		return indices;
	}
	
	private ArrayList<Integer> getNeighborIndices(ArrayList<Cell> neighbors, int type) {
		int idx = 0;
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for(Cell c : neighbors) {
			if(c.getValue().getVal()==type) {
				indices.add(idx);
			}
			idx++;
		}
		return indices;
	}
	
}
