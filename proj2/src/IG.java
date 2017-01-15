import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

	private long openTime, holdTime;

	/**
	 * The several states the cursor/hand could be in
	 */
	public enum logicState {
		OPEN, HOLD, LOOSE, RESTART
	}
	private logicState logicS;
	
	private Disk zero = new Disk(0, "00.png"), one = new Disk(1, "01.png"), two = new Disk(2, "02.png"),
			three = new Disk(3, "03.png"), four = new Disk(4, "04.png"), five = new Disk(5, "05.png"),
			six = new Disk(6, "06.png");
	
	//not to be confused with Hand, the default LeapMotion Object vs this, an Javafx component
	public PalmH palm;
	//Leap Motion controller
	Controller controller;
	private Tower t1 = new Tower(250, 500, 1), t2 = new Tower(750, 500, 2), t3 = new Tower(1250, 500, 3);
    //to handle restart from the middle of a game
	private Restart restart = new Restart(1250, 40);
	//stores the one object being picked by the curser
	private Disk heldDisk;

	//the states that the game coudl be in
	public enum GameState {
		WON, LOST, ACTIVE, NEW
	}

	private BackGround bg = new BackGround();
	
	private Label winImage = new Label("",new ImageView(new Image("winwin.png",1300,800,false,false)));
	
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
		getChildren().add(restart.questionBox());//hidden
		
		palm = new PalmH();
		getChildren().add(palm.getCircle());
		
		zero.setMusic("01.wav");
		one.setMusic("02.wav");
		two.setMusic("03.wav");
		three.setMusic("04.wav");
		four.setMusic("05.wav");
		five.setMusic("06.wav");
		six.setMusic("07.wav");
		
		winImage.setLayoutX(140);
//		winImage.setLayoutY(HEIGHT/2);
		getChildren().add(winImage);
		winImage.setVisible(false);

		restartGame(GameState.NEW);
	}

	@Override
	public Pane getPane() {
		return this;
	}
	@Override
	public String getName() {
		return " Tower Of Honoi ";
	}

	public void restartGame(GameState state) {
		final String message;
		if (state == GameState.LOST) {
			message = "Game Over\n";
		} else if (state == GameState.WON) {
//			message = "You won!\n";
		} else {
			message = "";
		}
		
		t1.resetStack();
		t2.resetStack();
		t3.resetStack();
		
		t1.addDisk(six);//bottom of the stack
		t1.addDisk(five);
		t1.addDisk(four);
		t1.addDisk(three);
		t1.addDisk(two);
		t1.addDisk(one);
		t1.addDisk(zero);//top of the stack

//		final Label startLabel = new Label(message + "Click mouse to start");
//		startLabel.setLayoutX(WIDTH / 2 - 50);
//		startLabel.setLayoutY(HEIGHT / 2 + 100);
//		getChildren().add(startLabel);

		heldDisk = null;
		logicS = logicState.OPEN;
		// Add event handler to start the game
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				IG.this.setOnMouseClicked(null);// only listen to one click
				// As soon as the mouse is clicked, remove the startLabel from
				// the game board
//				getChildren().remove(startLabel);
				run();
			}
		});
		// start an event from Leap Motion
		controller = new Controller();

	}

	public void run() {
		winImage.setVisible(false);
		bg.stop();
		bg.playMusic();
		// Instantiate and start an AnimationTimer to update the component of
		// the game.
		new AnimationTimer() {
			private long lastNanoTime = -1;

			public void handle(long currentNanoTime) {
				if (lastNanoTime >= 0) { // Necessary for first clock-tick.
					GameState state;
					if ((state = runOneTimestep()) != GameState.ACTIVE) {
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
		}.start();//start animation
	}

	/**
	 * Update one frame for the animation and controls all of the background processiong
	 * @return the status of the game going on
	 */
	private GameState runOneTimestep() {
		//only the first hand in the game is being considered
		Hand hand = controller.frame().hands().get(0);
		bg.play();
		
		//updates the position of the palm based on the location of the center of the hand
		if (hand != null) {
			this.palm.updatePosition((int) map(-200, 200, -750, 750, hand.palmPosition().getX()),
					(int) map(400, 100, -450, 450, hand.palmPosition().getY()));
		}
		//if something abnormal occurred in the game that request for a restart
		if (!gameLogic(hand))
			return GameState.NEW;
		
		if (t2.getSize() == 2 || t3.getSize() == 7){
			winImage.setVisible(true);
			bg.stop();
			bg.playWinningMusic();
			return GameState.WON;
		}
		//if everything is normal
		return GameState.ACTIVE;
	}
	
	/**
	 * identify the gesture of the hand to one of these gestures: point with index finger, pinch, and open
	 * @param hand given the hand object detected by Leap Motion
	 * @return the corresponding index of each position
	 */
	private int handHeld(Hand hand) {
		if (isPoint(hand, 65.0f)) {//index finger approx. 6.5 cm away from all other finger tips
			palm.bePointed();
			return 1;
		} else if (isPinch(hand, 50.0f)) {//all fingers are within 5cm radius of the tip of the thumb
			palm.bePinched();
			return 2;
		} else {
			palm.beOpen();
			return 3;
		}
	}

	/**
	 * determine if the hand detected by LeapMotion is in pinch gesture: 
	 * it is in pinch position of 3/4 of the remaining fingers are within the given
	 * radius of the thumb bc of noise
	 * @param hand the hand object returned by Leap Motion
	 * @param radius how far away each finger tip should be away from the thumb in pinch position
	 * @return if the hand is in pinch gesture
	 */
	private boolean isPinch(Hand hand, float radius) {
		int pinches = 0;
		Vector thumbpos = hand.fingers().get(0).tipPosition();
		for (int x = 1; x < 5; x++) {
			if (distance(thumbpos, hand.pointables().get(x).tipPosition()) < radius)
				pinches++;
		}
		return pinches > 2;
	}

	/**
	 * determine if the hand detected by LeapMotion is in point gesture: 
	 * the tip of the index finger is at least at radius distance away
	 * @param hand the hand object returned by Leap Motion
	 * @param radius radius how far away each finger tip should be away from the index in point position
	 * @return if there is no tip of fingers within the radius
	 */
	private boolean isPoint(Hand hand, float radius) {
		int inrange = 0;

		Vector indexpos = hand.fingers().get(1).tipPosition();
		for (Finger finger : hand.fingers())
			if (distance(indexpos, finger.tipPosition()) < radius)
				inrange++;
		return inrange == 1;
	}

	/**
	 * determine whether the cursor is in either the left, the middle, or the right third of the frame
	 * @return the tower that is associated with that part of the frame
	 */
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


	/**
	 * Main logic behind picking up disks and placing them in allowed positions
	 * @param hand radius how far away each finger tip should be away from the thumb in pinch position
	 * @return if the game can continue as normal or requires break in
	 */
	private boolean gameLogic(Hand hand) {
		
		Tower t = decideArea();
		
		//let loose of the disk
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
					restart.questionBox().setVisible(true);
					logicS = logicState.RESTART;
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
