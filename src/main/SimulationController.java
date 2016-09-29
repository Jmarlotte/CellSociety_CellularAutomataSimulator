package main;
import java.util.*;

//@authors: Andrew Bihl, James Marlotte

import cell.Cell;
import global_stepper.LocalStepper;
import cell.CellColors;
import global_stepper.BaseStepper;
import global_stepper.SegregationStepper;
import global_stepper.WaTorStepper;
import javafx.scene.paint.Color;
import rule.FireRule;
import rule.ReproductionRule;
import rule.SegregationRule;
import rule.WaTorRule;
import ui.SimulationDisplay;

public class SimulationController {

	private ArrayList<Cell> board;
	private SimulationDisplay display;
	private BaseStepper stepper; 

	public SimulationController(ArrayList<Cell> bd, SimulationDisplay sd) {
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
	
	public void step(){
		stepper.step();
		display.updateBoard(board);
	}

	public void setDisplay(SimulationDisplay d){
		display = d;
	}

	public SimulationDisplay getDisplay(){
		return display;
	}

}
