package main;

import java.util.ArrayList;


//@authors: Andrew Bihl, James Marlotte

import cell.Cell;
import io.FileParsingException;
import io.SpecFileParserDelegate;
import io.SpecificationFileParser;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import ui.SimulationDisplay;
import ui.SimulationDisplayDelegate;

// This class coordinates the game loop and handles user actions. 
// It is the master controller that handles the running loop 
public class MainController implements SimulationDisplayDelegate, SpecFileParserDelegate {
    private static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final int BASE_RATE = MILLISECOND_DELAY * 15;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private SimulationController simulator;
    private String simulationFileName;
    private SimulationDisplay display;
    
	Timeline loop;
	
	public MainController(){
        KeyFrame frame = new KeyFrame(Duration.millis(BASE_RATE),
                e -> step(SECOND_DELAY));
        loop = new Timeline();
        loop.setCycleCount(Timeline.INDEFINITE);
        loop.getKeyFrames().add(frame);
        loop.setDelay(new Duration(SECOND_DELAY));
		display = new SimulationDisplay();
		display.setDelegate(this);
	}
	
	private void step(double elapsedTime){
		simulator.step();
	}
	

	//User events will be delegated to these methods.
	public void pauseSimulation(){
		loop.stop();
	}
	
	public void resumeSimulation(){
		if (simulator == null)
			resetSimulation();
		else
			loop.play();
	}
	
	public void changeSimulationSpeed(double newRate){
		loop.setRate(newRate);
	}
	
	public void resetSimulation(){
		loop.stop();
		SpecificationFileParser sfp = new SpecificationFileParser(this);
		while(true) {
			try {
				sfp.readFile(simulationFileName);
				break;
			} catch (FileParsingException e) {
				e.printStackTrace();
			}	
		}
		ArrayList<Cell> board = sfp.getBoard();
		simulator = new SimulationController(board, display);
		simulator.setSimType();
		display.createBoard(board);
		// System.out.println(simulationFileName);
	}
	
	public void setSimulationFileName(String newSim){
		simulationFileName = "data/"+newSim+".xml";
	}
	
	public void stepSimulation(){
		simulator.step();
	}
	
	public void setSimulator(SimulationController s){
		simulator = s;
	}
	
	public SimulationDisplay getDisplay(){
		return display;
	}
	
	public void start(){
		loop.play();
	}

	@Override
	public void showErrorMsg(String s) {
		// TODO Add code to show error message as pop-up on GUI here
	}

	@Override
	public void showWarningMessage(String msg) {
		// TODO Add code to show warning message on display, but do NOT block UI flow
		
	}
	
}
