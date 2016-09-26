package cellsociety_team06;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

// This class coordinates the game loop and handles user actions. 
//It is the master controller that handles the running loop 
public class MainController {
    private static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final int BASE_RATE = MILLISECOND_DELAY * 15;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private SimulationController simulator;
    
	Timeline loop;
	
	public MainController(){
        KeyFrame frame = new KeyFrame(Duration.millis(BASE_RATE),
                e -> step(SECOND_DELAY));
        loop = new Timeline();
        loop.setCycleCount(Timeline.INDEFINITE);
        loop.getKeyFrames().add(frame);
        loop.setDelay(new Duration(SECOND_DELAY));
	}
	
	private void step(double elapsedTime){
		simulator.step();
		System.out.println("TEST");
		System.out.println("RATE: "+loop.getRate());
	}
	

	//User events will be delegated to these methods.
	public void pauseSimulation(){
		loop.stop();
	}
	
	public void resumeSimulation(){
		loop.play();
	}
	
	public void changeSimulationSpeed(double newRate){
		loop.setRate(newRate);
	}
	
	public void resetSimulation(String newSim){
		newSim = "src/"+newSim+".xml";
	}
	
	public void stepSimulation(){
		
	}
	
	public void setSimulator(SimulationController s){
		simulator = s;
		s.getDisplay().setDelegate(this);
	}
	
	public void start(){
		loop.play();
	}
	
}
