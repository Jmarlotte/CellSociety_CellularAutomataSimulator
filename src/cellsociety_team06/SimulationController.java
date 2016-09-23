package cellsociety_team06;
import java.util.*;

import cell.Cell;
import global_simulation.WaTorSimulator;
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
		if(board.get(0).getRule() instanceof FireRule || board.get(0).getRule() instanceof ReproductionRule) {
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					stepLocal();
				}
			}, 0, interval);
		} else if(board.get(0).getRule() instanceof WaTorRule) {
			WaTorSimulator sim = new WaTorSimulator(board);
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					sim.stepWaTor();
				}
			}, 0, interval);
			
		}
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
