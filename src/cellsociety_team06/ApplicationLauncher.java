package cellsociety_team06;

import javafx.application.Application;
import javafx.stage.Stage;

public class ApplicationLauncher extends Application {
	
	public static void main(String [] args){
		SpecificationFileParser sfp = new SpecificationFileParser();
		sfp.readFile("data/wator.xml");
		System.out.println(sfp.getBoard().get(0).getRule());
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("TEST");
	}
}
