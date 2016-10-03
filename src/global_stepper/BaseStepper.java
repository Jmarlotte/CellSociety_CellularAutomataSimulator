package global_stepper;

import java.util.ArrayList;
import java.util.Random;

import cell.*;

/**
 * Base stepper
 * @author ZYL
 *
 */
public abstract class BaseStepper {

	protected ArrayList<Cell> board;
	
	public BaseStepper(ArrayList<Cell> board) {
		this.board = board;
	}

	public abstract void step();
	
	protected ArrayList<Cell> getCellsOfType(CellNeighbors cellList, int type) {
		return getCellsOfType(cellList.toList(), type);
	}
	
	protected ArrayList<Cell> getCellsOfType(ArrayList<Cell> cellList, int type) {
		ArrayList<Cell> cells = new ArrayList<Cell>();
		for(Cell c : cellList) {
			if(c instanceof InvalidCell)
				continue;
			if(c.getValue().getVal()==type) {
				cells.add(c);
			}
		}
		return cells;
	}
	
	protected <T> T randomAccess(ArrayList<T> list) {
		return list.get(new Random().nextInt(list.size()));
	}
	
}
