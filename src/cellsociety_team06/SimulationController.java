package cellsociety_team06;
import java.util.*;

import cell.Cell;
import global_stepper.SegregationStepper;
import global_stepper.WaTorStepper;
import rule.FireRule;
import rule.ReproductionRule;
import rule.SegregationRule;
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
		if(board.get(0).getRule() instanceof FireRule || board.get(0).getRule() instanceof ReproductionRule) {
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					stepLocal();
				}
			}, 0, interval);
		} else if(board.get(0).getRule() instanceof WaTorRule) {
			WaTorStepper sim = new WaTorStepper(board);
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					sim.step();
				}
			}, 0, interval);
		} else if(board.get(0).getRule() instanceof SegregationRule) {
			SegregationStepper sim = new SegregationStepper(board);
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					sim.step();
				}
			}, 0, interval);
		}
	}

	public void step(){
		stepLocal();
	}
	
	/**
	 * Update once locally. 
	 */
	private void stepLocal() {
		for(Cell c : board) {
			c.prepareForUpdate();
		}
		ArrayList<Cell>  changedCells = new ArrayList<Cell>();
		for(Cell c : board) {
			if (c.update()){
				changedCells.add(c);
			}
		}
		display.updateScreen(changedCells);
	}

	public void setDisplay(SimulationDisplay d){
		display = d;
	}


}
