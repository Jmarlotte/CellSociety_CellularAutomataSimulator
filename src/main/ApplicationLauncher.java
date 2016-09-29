package main;

import javafx.application.Application;
import javafx.stage.Stage;

public class ApplicationLauncher extends Application {
	
	public static void main(String [] args){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		MainController controller = new MainController();
		primaryStage.setScene(controller.getDisplay().getScene());
		primaryStage.show();
	}
}
