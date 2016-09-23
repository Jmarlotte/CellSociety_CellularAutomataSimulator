package global_stepper;

import java.util.ArrayList;

import cell.Cell;

public abstract class BaseStepper {

	protected ArrayList<Cell> board;
	
	public abstract void step();
	
	protected ArrayList<Integer> getIndicesOfType(int type) {
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
	
	protected ArrayList<Integer> getNeighborIndices(ArrayList<Cell> neighbors, int type) {
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
