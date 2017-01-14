import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.control.Label;
import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseEvent;
import javafx.event.*;

import java.io.IOException;
import java.util.*;




public class IG extends Pane implements Game{
	
	
	// Constants
	/**
	 * The width of the game board.
	 */
	public static final int WIDTH = 800;
	/**
	 * The height of the game board.
	 */
	public static final int HEIGHT = 600;
	
	
	public enum GameState {
		WON, LOST, ACTIVE, NEW
	}
	
	public IG () {
		setStyle("-fx-background-color: white;");

		restartGame(GameState.NEW);
	}
	
	public void restartGame(GameState state)
	{
		final String message;
		if (state == GameState.LOST) {
			message = "Game Over\n";
		} else if (state == GameState.WON) {
			message = "You won!\n";
		} else {
			message = "";
		}
		
		final Label startLabel = new Label(message + "Click mouse to start");
		startLabel.setLayoutX(WIDTH / 2 - 50);
		startLabel.setLayoutY(HEIGHT / 2 + 100);
		getChildren().add(startLabel);
		
		try {
			System.in.read();
		} catch (IOException e) {
			
		}
		
	}
	
	public void run () {
		// Instantiate and start an AnimationTimer to update the component of the game.
		new AnimationTimer () {
			private long lastNanoTime = -1;
			public void handle (long currentNanoTime) {
				if (lastNanoTime >= 0) {  // Necessary for first clock-tick.
					GameState state;
					if ((state = runOneTimestep(currentNanoTime - lastNanoTime)) != GameState.ACTIVE) {
						// Once the game is no longer ACTIVE, stop the AnimationTimer.
						stop();
						// Restart the game, with a message that depends on whether
						// the user won or lost the game.
						restartGame(state);
					}
				}
				// Keep track of how much time actually transpired since the last clock-tick.
				lastNanoTime = currentNanoTime;
			}
		}.start();
	}
	
	public GameState runOneTimestep(long deltaNanoTime)
	{
		
		return GameState.ACTIVE;
	}

	@Override
	public Pane getPane() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "TowerOfHonoi";
	}

}
