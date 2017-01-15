import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.control.Label;
import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseEvent;
import javafx.event.*;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Vector;

public class IG extends Pane implements Game {

	// Constants
	/**
	 * The width of the game board.
	 */
	public static final int WIDTH = 1500;
	/**
	 * The height of the game board.
	 */
	public static final int HEIGHT = 900;

	private long openTime, holdTime, restartTime;

	public enum logicState {
		OPEN, HOLD, LOOSE, RESTART
	}

	private logicState logicS;
	private Disk zero = new Disk(0, "00.png"), one = new Disk(1, "01.png"), two = new Disk(2, "02.png"),
			three = new Disk(3, "03.png"), four = new Disk(4, "04.png"), five = new Disk(5, "05.png"),
			six = new Disk(6, "06.png");
//	
//	private AudioClip mZero = new AudioClip(getClass().getClassLoader().getResource("01.wav").toString()),
//			mOne = new AudioClip(getClass().getClassLoader().getResource("02.wav").toString()), 
//			mTwo = new AudioClip(getClass().getClassLoader().getResource("03.wav").toString()),
//  		    mThree = new AudioClip(getClass().getClassLoader().getResource("04.wav").toString()),
//  		    mFour = new AudioClip(getClass().getClassLoader().getResource("05.wav").toString()),  
//  		  	mFive = new AudioClip(getClass().getClassLoader().getResource("06.wav").toString()),
//  		  	mSix = new AudioClip(getClass().getClassLoader().getResource("07.wav").toString());
	
	
	public PalmH palm;
	Controller controller;
	private Tower t1 = new Tower(250, 500, 1), t2 = new Tower(750, 500, 2), t3 = new Tower(1250, 500, 3);

	private Restart restart = new Restart(1250, 40);
	private Disk heldDisk;

	public enum GameState {
		WON, LOST, ACTIVE, NEW
	}

	private BackGround bg = new BackGround();
	public IG() {
		setStyle("-fx-background-color: white;");
		getChildren().add(bg.getLabel());
		getChildren().add(t1.getImage());
		getChildren().add(t2.getImage());
		getChildren().add(t3.getImage());
		
		getChildren().add(six.getImage());
		getChildren().add(five.getImage());
		getChildren().add(four.getImage());
		getChildren().add(three.getImage());
		getChildren().add(two.getImage());
		getChildren().add(one.getImage());
		getChildren().add(zero.getImage());
		getChildren().add(restart.getImage());
		getChildren().add(restart.questionBox());
		
		palm = new PalmH();
		getChildren().add(palm.getCircle());
		
		zero.setMusic("01.wav");
		one.setMusic("02.wav");
		two.setMusic("03.wav");
		three.setMusic("04.wav");
		four.setMusic("05.wav");
		five.setMusic("06.wav");
		six.setMusic("07.wav");
		
		

		restartGame(GameState.NEW);
	}

	public Pane getPane() {
		return this;
	}

	public String getName() {
		return " Tower Of Honoi ";
	}

	public void restartGame(GameState state) {
		final String message;
		if (state == GameState.LOST) {
			message = "Game Over\n";
		} else if (state == GameState.WON) {
			message = "You won!\n";
		} else {
			message = "";
		}
		
		t1.resetStack();
		t2.resetStack();
		t3.resetStack();
		
		t1.addDisk(six);
		t1.addDisk(five);
		t1.addDisk(four);
		t1.addDisk(three);
		t1.addDisk(two);
		t1.addDisk(one);
		t1.addDisk(zero);

		final Label startLabel = new Label(message + "Click mouse to start");
		startLabel.setLayoutX(WIDTH / 2 - 50);
		startLabel.setLayoutY(HEIGHT / 2 + 100);
		getChildren().add(startLabel);

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
				bg.stop();
				bg.playMusic();
				run();

			}
		});
		// start an event from Leap Motion
		controller = new Controller();

	}

	public void run() {
		// Instantiate and start an AnimationTimer to update the component of
		// the game.
		new AnimationTimer() {
			private long lastNanoTime = -1;

			public void handle(long currentNanoTime) {
				if (lastNanoTime >= 0) { // Necessary for first clock-tick.
					GameState state;
					if ((state = runOneTimestep(currentNanoTime - lastNanoTime)) != GameState.ACTIVE) {
						// Once the game is no longer ACTIVE, stop the
						// AnimationTimer.
						stop();
						// Restart the game, with a message that depends on
						// whether the user won or lost the game.
						restartGame(state);
					}
				}
				// Keep track of how much time actually transpired since the
				// last clock-tick.
				lastNanoTime = currentNanoTime;
			}
		}.start();
	}

	public GameState runOneTimestep(long deltaNanoTime) {
		Hand hand = controller.frame().hands().get(0);
		bg.play();
		if (hand != null) {
			this.palm.updatePosition((int) map(-200, 200, -750, 750, hand.palmPosition().getX()),
					(int) map(400, 100, -450, 450, hand.palmPosition().getY()));
		}

		if (!gameLogic(hand))
			return GameState.NEW;
		
		if (t2.getSize() == 7 || t3.getSize() == 7){
			bg.stop();
			bg.playWinningMusic();
			return GameState.WON;
		}

		return GameState.ACTIVE;
	}
	
	private int handHeld(Hand hand) {
		if (isPoint(hand, 65.0f)) {
			palm.bePointed();
			return 1;
		} else if (isPinch(hand, 50.0f)) {
			palm.bePinched();
			return 2;
		} else {
			palm.beOpen();
			return 3;
		}
	}

	private boolean isPinch(Hand hand, float radius) {
		int pinches = 0;
		Vector thumbpos = hand.fingers().get(0).tipPosition();
		for (int x = 1; x < 5; x++) {
			if (distance(thumbpos, hand.pointables().get(x).tipPosition()) < radius)
				pinches++;
		}
		return pinches > 2;
	}

	private boolean isPoint(Hand hand, float radius) {
		int inrange = 0;

		Vector indexpos = hand.fingers().get(1).tipPosition();
		for (Finger finger : hand.fingers())
			if (distance(indexpos, finger.tipPosition()) < radius)
				inrange++;
		return inrange == 1;
	}

	private Tower decideArea() {
		Tower temp = null;
		int xPos = palm.getX();
		int yPos = palm.getY();
//		System.out.println(xPos + " " + yPos);
		if (-450 <= yPos && yPos <=450) {
			if (-750 <= xPos && xPos < -250)
				temp = t1;
			else if (-250 <=xPos && xPos < 250)
				temp = t2;
			else if (250 <= xPos && xPos <=750)
				temp = t3;
		}
		return temp;
	}


	private boolean gameLogic(Hand hand) {
		Tower t = decideArea();
		if (t == null && logicS !=logicState.RESTART)
			logicS = logicState.LOOSE;
		else if (logicS !=logicState.RESTART)
			bg.changeBG(t.getArea());

		int handPos = handHeld(hand);
		
		switch (logicS) {
		case OPEN:
			if (handPos == 2 && !t.stackEmpty() && (System.currentTimeMillis() - this.openTime) > 600) {
				heldDisk = t.getTop();
//				heldDisk.playMusic();
				bg.play(heldDisk.firstT());
				
				logicS = logicState.HOLD;
				this.holdTime = System.currentTimeMillis();
			}
			else if (handPos == 1 && (System.currentTimeMillis() - this.openTime) > 600){
				if (restart.onClick(palm.getX()+750, palm.getY()+450)){
					this.restartTime = System.currentTimeMillis();
					restart.questionBox().setVisible(true);
					logicS = logicState.RESTART;
				}
				else{
					//play music?
				}
			}
			break;
		case HOLD:
			heldDisk.moveTo(palm.getX()+710, palm.getY()+490);
			if (handPos!=2 && (System.currentTimeMillis() - this.holdTime) > 600)
				logicS = logicState.LOOSE;
			break;
		case LOOSE:
            if(heldDisk!=null){
            	if ( t!=null)
    				t.addDisk(heldDisk);
    			else
    				heldDisk.getHome().addDisk(heldDisk);
            }
			heldDisk = null;
			logicS = logicState.OPEN;
			this.openTime = System.currentTimeMillis();
			break;
		case RESTART:
			if (handPos == 1) {
				if (this.restart.onClickNo(palm.getX(), palm.getY())) {
					logicS = logicState.OPEN;
					this.openTime = System.currentTimeMillis();
					restart.questionBox().setVisible(false);
				} else if (this.restart.onClickYes(palm.getX(), palm.getY())) {
					restart.questionBox().setVisible(false);
					return false;//discontinue the logic
				}
			}
			break;
		}
		return true;
	}

	private double distance(Vector t, Vector f) {
		return Math.sqrt(Math.pow((t.getX() - f.getX()), 2) + Math.pow((t.getY() - f.getY()), 2)
				+ Math.pow((t.getZ() - f.getZ()), 2));
	}

	private double map(double imin, double imax, double fmin, double fmax, double val) {
		return (fmax - fmin) / (imax - imin) * (val - imin) + fmin;
	}
}
