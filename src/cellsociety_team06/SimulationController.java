package cellsociety_team06;
import java.util.*;

import cell.Cell;
import global_stepper.SegregationStepper;
import global_stepper.WaTorStepper;
import gui.GUI;
import rule.FireRule;
import rule.ReproductionRule;
import rule.SegregationRule;
import rule.WaTorRule;

public class SimulationController {

	private ArrayList<Cell> board;
	private GUI display;
	private Timer timer;

	public SimulationController(ArrayList<Cell> bd, GUI sd) {
		timer = new Timer();
		board = bd;
		display = sd;
	}

	public void startTask(int interval) {
		if(board.get(0).getRule() instanceof FireRule || board.get(0).getRule() instanceof ReproductionRule) {
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					stepLocal();
				}
			}, interval, interval);
		} else if(board.get(0).getRule() instanceof WaTorRule) {
			WaTorStepper sim = new WaTorStepper(board);
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					double s = new Random().nextDouble();
					System.out.println("run "+s);
					long start = System.currentTimeMillis();
					sim.step();
					long end = System.currentTimeMillis();
					System.out.println("done "+ s + " " + (end - start));
					display.updateScreen(board);
				}
			}, interval, interval);
		} else if(board.get(0).getRule() instanceof SegregationRule) {
			SegregationStepper sim = new SegregationStepper(board);
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					sim.step();
					display.updateScreen(board);
				}
			}, interval, interval);
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
