import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.control.Label;
import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseEvent;
import javafx.event.*;

import java.io.IOException;
import java.util.*;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Vector;


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
	
	public Palm hand; 
	
	Controller controller;
	
	
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
		//start an event from Leap Motion
         controller = new Controller();

		
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
		Hand hand = controller.frame().hands().get(0);
		if (hand != null){
			this.hand.updatePosition((int)map(-200, 200, 0, 800, hand.palmPosition().getX()), 
	        		(int)map(400, 100, 0, 600, hand.palmPosition().getY()));
		}

		if (isPoint(hand, 65.0f)) {
			this.hand.bePointed();
		} else if (isPinch(hand, 80.0f)) {
			this.hand.bePinched();
		} else {
			this.hand.beOpen();
		}
        
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
	
    private boolean isPinch(Hand hand, float radius)
    {
		int pinches = 0;
		Vector thumbpos = hand.fingers().get(0).tipPosition();
		for (int x = 1; x < 5; x++) {
			if (distance(thumbpos, hand.pointables().get(x).tipPosition()) < radius)
				pinches++;
		}
		
		return pinches > 2;
    }
    
    private boolean isPoint(Hand hand, float radius)
    {
    	int inrange = 0; 
    	
    	Vector indexpos = hand.fingers().get(1).tipPosition();
    	for (Finger finger : hand.fingers())
    		if (distance(indexpos, finger.tipPosition()) < radius)
    			inrange++;
    	return inrange == 1;
    }
    
    private double distance(Vector t, Vector f)
    {
    	return Math.sqrt(Math.pow((t.getX()-f.getX()), 2) 
    			+ Math.pow((t.getY()-f.getY()), 2)
    			+ Math.pow((t.getZ()-f.getZ()), 2));
    }

}
