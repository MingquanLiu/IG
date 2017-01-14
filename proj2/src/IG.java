import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.control.Label;
import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseEvent;
import javafx.event.*;

import java.io.IOException;
import java.util.*;

import com.leapmotion.leap.Controller;


public class IG extends Pane implements Game{
	
	
	// Constants
	/**
	 * The width of the game board.
	 */
	public static final int WIDTH = 1000;
	/**
	 * The height of the game board.
	 */
	public static final int HEIGHT = 600;
	
	
	//instance variables
	public Palm hand; 	
	Controller controller;
	private LinkedList<Tower> towers = new LinkedList<Tower>();
	private Tower t1 = new Tower(200, 300);
	private Tower t2 = new Tower(500, 300);
	private Tower t3 = new Tower(800, 300);
	
	public enum GameState {
		WON, LOST, ACTIVE, NEW
	}
	
	public IG () {
		setStyle("-fx-background-color: white;");
		
		getChildren().add(t1.getImage());
		getChildren().add(t2.getImage());
		getChildren().add(t3.getImage());
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
		
		hand = new Palm();
		getChildren().add(hand.getCircle());

		// Add event handler to start the game
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				IG.this.setOnMouseClicked(null);// only listen to one click

				// As soon as the mouse is clicked, remove the startLabel from
				// the game board
				getChildren().remove(startLabel);
				
				run();
			}
		});
		
         controller = new Controller();
//        IGListener igListener = new IGListener(this);
//        controller.addListener(igListener);
		
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
	
	public void setHandPos(int x, int y)
	{
		hand.updatePosition(x, y);
	}
	
	public GameState runOneTimestep(long deltaNanoTime)
	{
		setHandPos((int)map(-200, 200, 0, 800, controller.frame().hands().get(0).stabilizedPalmPosition().getX()), 
        		(int)map(350, 100, 0, 600, controller.frame().hands().get(0).stabilizedPalmPosition().getY()));
        
		return GameState.ACTIVE;
	}

	public Pane getPane() {
		// TODO Auto-generated method stub
		return this;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return "TowerOfHonoi";
	}
	
	private double map(double imin, double imax, double fmin, double fmax, double val){
    	return (fmax-fmin)/(imax-imin)*val+imin;
    }

}
