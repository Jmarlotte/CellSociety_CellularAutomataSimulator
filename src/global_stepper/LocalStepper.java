package global_stepper;

import java.util.ArrayList;

import cell.Cell;

public class LocalStepper extends BaseStepper {

	public LocalStepper(ArrayList<Cell> board) {
		super(board);
	}

	@Override
	public void step() {
		for(Cell c : board) {
			c.prepareForUpdate();
		}
		for(Cell c : board) {
			c.update();
		}
	}
}
