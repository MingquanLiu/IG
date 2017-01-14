import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.layout.*;

public class GameApp extends Application{
	public GameApp(){		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Game game = new IG();
		primaryStage.setTitle(game.getName());
		primaryStage.setScene(new Scene(game.getPane(), IG.WIDTH, IG.HEIGHT));
		primaryStage.show();
	}
	
	public static void main(String[] args){
		launch(args);
	}
}
