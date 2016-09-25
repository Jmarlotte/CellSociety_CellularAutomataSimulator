package global_stepper;

import java.util.ArrayList;
import java.util.Random;

import cell.Cell;

public abstract class BaseStepper {

	protected ArrayList<Cell> board;
	
	public BaseStepper(ArrayList<Cell> board) {
		this.board = board;
	}

	public abstract void step();
	
	protected ArrayList<Integer> getIndicesOfType(ArrayList<Cell> neighbors, int type) {
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
	
	protected <T> T randomAccess(ArrayList<T> list) {
		return list.get(new Random().nextInt(list.size()));
	}
	
}
