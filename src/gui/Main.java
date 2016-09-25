package gui;

import java.util.*;

import cellsociety_team06.SimulationController;
import io.SpecificationFileParser;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import cell.Cell;

public class Main extends Application {

	public static final int FRAMES_PER_SECOND = 32;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final int SIM_INTERVAL = 200;
	
	private GUI gui;
	private SimulationController controller;
	
	@Override
	public void start(Stage s) throws Exception {
		
		SpecificationFileParser sfp = new SpecificationFileParser();
		sfp.readFile("data/segregation.xml");
		System.out.println(sfp.getBoard().get(0).getRule());
		ArrayList<Cell> board = sfp.getBoard();
		
		gui = new GUI();
        s.setTitle("Cell Society");

        ColorMap cm = new ThreeColorMapSegregation();
        Scene scene = gui.init(cm, board, "WaTor");
        s.setScene(scene);
        s.show();
        
        controller = new SimulationController(board, gui);
		controller.startTask(SIM_INTERVAL);
		/*WaTorStepper stepper = new WaTorStepper(board);
		gui.updateScreen(board);
		System.out.println("press enter");
		new InputStreamReader(System.in).read();
		stepper.step();
		gui.updateScreen(board);*/

        // sets the game's loop
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                                      e -> gui.step(SECOND_DELAY));
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
	}
	
	public static void main (String[] args) {
        launch(args);
    }

}
