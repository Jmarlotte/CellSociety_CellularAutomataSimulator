package cellsociety_team06;

import io.SpecificationFileParser;
import java.util.ArrayList;
import cell.Cell;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ApplicationLauncher extends Application {
	private static final String FIRE_FILE = "data/fire.xml";
	private static final String WATOR_FILE = "data/wator.xml";
	private static final String GAME_OF_LIFE_FILE = "data/game_of_life.xml";
	private static final String SEGREGATION_FILE = "data/segregation.xml";
	
	public static void main(String [] args){
//		System.out.println(sfp.getBoard().get(0).getRule());
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		MainController controller = new MainController();
		primaryStage.setScene(controller.getDisplay().getScene());
//		SimulationDisplay displayView = new SimulationDisplay();
//		primaryStage.setScene(displayView.getScene());
		primaryStage.show();
//		controller.resetSimulation();
		

//		//TODO: Implement a method where the simulationController sets up initial stuff
//		
//		//Hand off control to MainController 
//		controller.setSimulator(simulationController);
//		controller.start();
	}
}
