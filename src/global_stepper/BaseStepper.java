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
	
	protected ArrayList<Cell> getCellsOfType(ArrayList<Cell> cellList, int type) {
		ArrayList<Cell> cells = new ArrayList<Cell>();
		for(Cell c : cellList) {
			if(c.getValue().getVal()==type) {
				cells.add(c);
			}
		}
		return cells;
	}
	
	/*protected ArrayList<Integer> getIndicesOfType(ArrayList<Cell> cellList, int type) {
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for(int i=0; i<cellList.size(); i++) {
			Cell c = cellList.get(i);
			if(c.getValue().getVal()==type) {
				indices.add(i);
			}
		}
		return indices;
	}*/
	
	protected <T> T randomAccess(ArrayList<T> list) {
		return list.get(new Random().nextInt(list.size()));
	}
	
}
