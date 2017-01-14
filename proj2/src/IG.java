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
import com.sun.xml.internal.ws.wsdl.writer.document.OpenAtts;


public class IG extends Pane implements Game{
	
	
	// Constants
	/**
	 * The width of the game board.
	 */
	public static final int WIDTH = 1500;
	/**
	 * The height of the game board.
	 */
	public static final int HEIGHT = 900;
	
	int area =2;
	
	public enum logicState {
		OPEN, HOLD, LOOSE
	}
	//instance variables
	public PalmH hand; 	
	Controller controller;
	private LinkedList<Tower> towers = new LinkedList<Tower>();
	private Tower t1 = new Tower(250, 450);
	private Tower t2 = new Tower(750, 450);
	private Tower t3 = new Tower(1250, 450);
	
	private Disk heldDisk;
	private logicState logicS;
	public enum GameState {
		WON, LOST, ACTIVE, NEW
	}
	
	private BackGround bg = new BackGround();
	public IG () {
		setStyle("-fx-background-color: white;");
		getChildren().add(bg.getLabel());
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
		
		hand = new PalmH();
		getChildren().add(hand.getCircle());
		
		heldDisk = null;
		logicS = logicState.OPEN;
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

		handHeld(hand);
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

	public void setHandPos(int map, int map2) {
		// TODO Auto-generated method stub
		this.hand.updatePosition(map, map2);
		
	}
	//Game Logic Area 
	
	//Area Area Area
	private int decideArea()
	{
		int xPos = hand.getX();
		int yPos = hand.getY();
		if(0<yPos&&yPos<900)
		{
			if(0<xPos&&xPos<500)
				area =1;
			if(500<xPos&&xPos<1000)
				area =2;
			if(1000<xPos&&xPos<1500)
				area =3;
		}
		return area;
	}
	
	private boolean handHeld(Hand hand)
	{
		if (isPoint(hand, 65.0f)) {
			this.hand.bePointed();
			return true;
		} else if (isPinch(hand, 80.0f)) {
			this.hand.bePinched();
			return true;
		} else {
			this.hand.beOpen();
			return false;
		}
	}
	
	private void gameLogic(Hand hand)
	{
		switch (logicS) {
		case OPEN:
			bg.changeBG(decideArea());
			if(handHeld(hand))
				logicS = logicState.HOLD;
			break;
		case HOLD:
			
			break;
		case LOOSE:
			
			break;
		}
	}
	
	private void changeBackground(int area)
	{
		
	}

}

