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
		
		SpecificationFileParser sfp = new SpecificationFileParser();
		sfp.readFile(FIRE_FILE);
		
		SimulationDisplay displayView = new SimulationDisplay(100, 100);
		ArrayList<Cell> board = sfp.getBoard();
		SimulationController simulationController = new SimulationController(board, displayView);
		simulationController.setSimType();
		displayView.createBoard(board);
		//TODO: Implement a method where the simulationController sets up initial stuff
//		simulationController.beginStuff
		
		primaryStage.setScene(displayView.getScene());
		primaryStage.show();
		//Hand off control to MainController 
		MainController controller = new MainController();
		controller.setSimulator(simulationController);
		controller.changeSimulationSpeed(10.0);
		controller.start();
	}
}
