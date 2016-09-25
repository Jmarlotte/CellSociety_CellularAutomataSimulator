package cellsociety_team06;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

// NOT SURE WHAT THIS CLASS DOES
public class MainController {
    private static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private SimulationController simulator;
    
	Timeline loop;
	
	public MainController(){
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                e -> step(SECOND_DELAY));
        loop = new Timeline();
        loop.setCycleCount(Timeline.INDEFINITE);
        loop.getKeyFrames().add(frame);
	}
	
	public void setSimulator(SimulationController s){
		simulator = s;
	}
	
	public void start(){
		loop.play();
	}
	
	private void step(double elapsedTime){
		simulator.step();
		System.out.println("TEST");
	}
	
}
