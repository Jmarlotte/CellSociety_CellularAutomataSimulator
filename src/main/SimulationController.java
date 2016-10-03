package main;
import java.util.*;

import board.BoardConfigurationSaver;

//@authors: Andrew Bihl, James Marlotte

import cell.Cell;
import global_stepper.LocalStepper;
import cell.CellColors;
import global_stepper.BaseStepper;
import global_stepper.SegregationStepper;
import global_stepper.WaTorStepper;
import javafx.scene.paint.Color;
import rule.FireRule;
import rule.NonTotalisticRule;
import rule.ReproductionRule;
import rule.SegregationRule;
import rule.WaTorRule;
import ui.CellDisplayInfo;
import ui.GridDisplay;
import ui.SimulationDisplay;
import ui.SquareGridDisplay;
import ui.TriangleGridDisplay;

/**
 * Simulation controller
 * @author ZYL
 *
 */
public class SimulationController {

	private final String RESOURCE_PATH = "resources/DisplaySettings";

	private ArrayList<Cell> board;
	private SimulationDisplay display;
	private GridDisplay boardDisplay;
	private BaseStepper stepper; 
	private ResourceBundle myResources;
	private Color[] simulationColors;

	public SimulationController(ArrayList<Cell> bd, SimulationDisplay sd) {
		board = bd;
		display = sd;
		myResources = ResourceBundle.getBundle(RESOURCE_PATH);
	}

	public void setSimType() {
		Object rule = board.get(0).getRule();
		
		if (rule instanceof FireRule){
			simulationColors = CellColors.fireColors();
			stepper = new LocalStepper(board);
		} 
		else if ((rule instanceof ReproductionRule) || (rule instanceof NonTotalisticRule)){
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
	}
	
	/**
	 * Adds a grid to the display based on the current set simulation.
	 */
	public void createBoard(List<Cell> board){
		
		double windowWidth = display.getWindowWidth();
		double windowHeight = display.getWindowHeight();
		int gridWidth = Integer.parseInt(myResources.getString("GridWidth"));
		int gridHeight = Integer.parseInt(myResources.getString("GridHeight"));
		
		double offsetX = (windowWidth - gridWidth)/2;
		double offsetY = (windowHeight-gridHeight)/2;
		int rowCount = (int)Math.sqrt(board.size());
		List<CellDisplayInfo> cells = makeCellDisplayList(board);
		GridDisplay gridDisplay = new TriangleGridDisplay(rowCount, rowCount, gridWidth, gridWidth, cells);
		boardDisplay = gridDisplay;
		System.out.println("Offset x: "+offsetX+", offset y: "+offsetY);
		display.addBoard(gridDisplay, offsetX, offsetY);
	}
	
	private List<CellDisplayInfo> makeCellDisplayList(List<Cell> cells){
		ArrayList<CellDisplayInfo> cellDisplayList = new ArrayList<CellDisplayInfo>();
		for (Cell cell : cells){
			Color color = simulationColors[cell.getValue().getVal()];
			cellDisplayList.add(new CellDisplayInfo(cell.getY(), cell.getX(), color));
		}
		return cellDisplayList;
	}
	
	public void updateBoard(List<Cell> board){
		List<CellDisplayInfo> cells = makeCellDisplayList(board);
		boardDisplay.updateBoard(cells);
	}
	
	public void step(){
		stepper.step();
		updateBoard(board);
	}
	
	public void setDisplay(SimulationDisplay d){
		display = d;
	}

	public SimulationDisplay getDisplay(){
		return display;
	}
	
	/**
	 * Save current board configuration
	 * @param fileName
	 */
	//TODO: add a "Save" button to UI and call this function when appropriate
	public void saveBoard(String fileName) {
		BoardConfigurationSaver.saveBoard(board, fileName);
	}

}
