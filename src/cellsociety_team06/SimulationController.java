package cellsociety_team06;
import java.util.*;

import cell.Cell;
import cell.CellColors;
import global_stepper.BaseStepper;
import global_stepper.SegregationStepper;
import global_stepper.WaTorStepper;
import javafx.scene.paint.Color;
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

	public void setSimType() {
		Color[] simulationColors;
		BaseStepper stepper; 
		Object rule = board.get(0).getRule();
		
		//TODO: Set the stepper in all instances
		if (rule instanceof FireRule){
			simulationColors = CellColors.fireColors();
			
		} else if (rule instanceof ReproductionRule){
			simulationColors = CellColors.reproductionColors();
			
		} else if (rule instanceof WaTorRule){
			simulationColors = CellColors.waTorColors();
			stepper = new WaTorStepper(board);
		} else if (rule instanceof SegregationRule){
			simulationColors = CellColors.segregationColors();
			stepper = new SegregationStepper(board);
		} else{
			System.out.println("COULD NOT DETECT RULE");
			simulationColors = new Color[0];
		}
		display.setColors(simulationColors);
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
		display.updateBoard(changedCells);
//		display.updateScreen(changedCells);
	}

	public void setDisplay(SimulationDisplay d){
		display = d;
	}


}
