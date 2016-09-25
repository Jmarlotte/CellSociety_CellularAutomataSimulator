package cellsociety_team06;

import java.util.ArrayList;

import cell.Cell;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ApplicationLauncher extends Application {
	
	public static void main(String [] args){
		SpecificationFileParser sfp = new SpecificationFileParser();
		sfp.readFile("data/fire.xml");
		System.out.println(sfp.getBoard().get(0).getRule());
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		SpecificationFileParser sfp = new SpecificationFileParser();
		sfp.readFile("data/fire.xml");
		
		SimulationDisplay displayView = new SimulationDisplay(100, 100);
		
		ArrayList<Cell> board = sfp.getBoard();
		displayView.updateScreen(board);
		
		primaryStage.setScene(displayView.getScene());
		
		primaryStage.show();
		SimulationController simulationController = new SimulationController(board, displayView);
		//Hand off control to MainController 
		MainController controller = new MainController();
		controller.setSimulator(simulationController);
		controller.start();
	}
}
