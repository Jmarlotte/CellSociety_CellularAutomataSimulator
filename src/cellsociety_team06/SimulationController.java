package cellsociety_team06;
import java.util.*;

import cell.Cell;
import global_stepper.LocalStepper;
import cell.CellColors;
import global_stepper.BaseStepper;
import global_stepper.SegregationStepper;
import global_stepper.WaTorStepper;
import gui.GUI;
import javafx.scene.paint.Color;
import rule.FireRule;
import rule.ReproductionRule;
import rule.SegregationRule;
import rule.WaTorRule;

public class SimulationController {

	private ArrayList<Cell> board;
//	private GUI display;
	private SimulationDisplay display;
	private Timer timer;
	private int interval;
	private BaseStepper stepper; 

	public SimulationController(ArrayList<Cell> bd, SimulationDisplay sd) {
		interval = 1000;
		timer = new Timer();
		board = bd;
		display = sd;
	}

	public void setSimType() {
		Color[] simulationColors;
		Object rule = board.get(0).getRule();
		
		//TODO: Set the stepper in all instances
		if (rule instanceof FireRule){
			simulationColors = CellColors.fireColors();
			stepper = new LocalStepper(board);
		} 
		else if (rule instanceof ReproductionRule){
			simulationColors = CellColors.reproductionColors();
			stepper = new LocalStepper(board);
		} 
		else if (rule instanceof WaTorRule){
			simulationColors = CellColors.waTorColors();
			stepper = new WaTorStepper(board);
		} 
		else if (rule instanceof SegregationRule){
			simulationColors = CellColors.segregationColors();
			stepper = new SegregationStepper(board);
		} 
		else{
			System.out.println("COULD NOT DETECT RULE");
			simulationColors = new Color[0];
		}
		display.setColors(simulationColors);
		
	}
	
	public void startTask() {
		if(board.get(0).getRule() instanceof FireRule || board.get(0).getRule() instanceof ReproductionRule) {
			LocalStepper sim = new LocalStepper(board);
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					sim.step();
//					display.updateScreen(board);
					display.updateBoard(board);
				}
			}, interval, interval);
		} else if(board.get(0).getRule() instanceof WaTorRule) {
			WaTorStepper sim = new WaTorStepper(board);
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					sim.step();
//					display.updateScreen(board);
					display.updateBoard(board);
				}
			}, interval, interval);
		} else if(board.get(0).getRule() instanceof SegregationRule) {
			SegregationStepper sim = new SegregationStepper(board);
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					sim.step();
//					display.updateScreen(board);
					display.updateBoard(board);
				}
			}, interval, interval);
		}
	}

	public void step(){
		stepper.step();
		display.updateBoard(board);
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
		display.updateBoard(board);
//		display.updateBoard(changedCells);
//		display.updateScreen(changedCells);
	}

	public void setDisplay(SimulationDisplay d){
		display = d;
	}


}
