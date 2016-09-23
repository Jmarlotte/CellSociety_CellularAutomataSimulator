package cellsociety_team06;
import java.util.*;

import cell.Cell;
import cell.WaTorCell;
import rule.FireRule;
import rule.ReproductionRule;
import rule.WaTorRule;

public class SimulationController {

	private ArrayList<Cell> board;
	private SimulationDisplay display;
	private Timer timer;
	public int interval = 1000;
	
	public SimulationController(ArrayList<Cell> bd, SimulationDisplay sd) {
		timer = new Timer();
		board = bd;
		display = sd; 
	}
	
	public void startTask() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				// cell can locally make decisions if game rule is fire or reproduction
				if(board.get(0).getRule() instanceof FireRule || board.get(0).getRule() instanceof ReproductionRule) {
					stepLocal();
				} else if(board.get(0).getRule() instanceof WaTorRule) {
					stepWaTor();
				}
			}
		}, 0, interval);
	}
	
	/**
	 * Stepping in WaTor
	 * 1. randomly shuffle the board to change update sequence
	 * 2. update fish. for each fish, move to a nearby position. reproduce as needed
	 * 3. update shark. for each shark, move to a nearby position. eat fish if possible. reproduce as needed
	 * 
	 */
	private void stepWaTor() {
		Collections.shuffle(board); // randomly shuffle the board
		// fish update
		ArrayList<Integer> fishIndices = getIndicesOfType(1);
		for(int idx : fishIndices) {
			WaTorCell thisCell = (WaTorCell) board.get(idx);
			thisCell.step();
			ArrayList<Integer> idxs = this.getEmptyNeighborIndices(thisCell.getNeighbors());
			if(idxs.isEmpty()) // no empty cell
				continue;
			int targetIdx = idxs.get(new Random().nextInt(idxs.size()));
			WaTorCell targetCell = (WaTorCell)board.get(targetIdx);
			// change type of the neighbor cell, but keep reproduce timer
			targetCell.changeType(1, thisCell.getTimeToReproduce());
			// check reproduction
			if(thisCell.getTimeToReproduce()<=0) {
				// do not change cell type. just reset the timer to represent new fish
				thisCell.resetReproduceTimer();
				targetCell.resetReproduceTimer();
			} else {
				// otherwise, empty the cell
				thisCell.changeType(0);
			}
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
	
	private ArrayList<Integer> getEmptyNeighborIndices(ArrayList<Cell> neighbors) {
		int idx = 0;
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for(Cell c : neighbors) {
			if(c.getValue().getVal()==0) {
				indices.add(idx);
			}
			idx++;
		}
		return indices;
	}

	/**
	 * Update once locally. 
	 */
	private void stepLocal() {
		for(Cell c : board) {
			c.prepareForUpdate();
		}
		for(Cell c : board) {
			c.update();
		}
		display.updateScreen(board);
	}
	
	
	
}
