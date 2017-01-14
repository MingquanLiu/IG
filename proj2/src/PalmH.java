import javax.swing.ImageIcon;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PalmH {
	

	private int X, Y; 
	public static final int MAX_X = IG.WIDTH;
	public static final int MAX_Y = IG.HEIGHT;
	private final int handWidth = 70;
	private final int handHeight = 80;
	Image hand,handHold,pointed;
	
	private Label imageLabel,imageLabelHold,imageReturn;
	int choice;										// 0 means open 1 means point 2 means hold 
	public PalmH()
	{
		choice =0;
		X = MAX_X/2;
		Y = MAX_Y/2;
		hand = new Image("open.png", handWidth, handHeight, false, false);
		imageLabel = new Label("", new ImageView(hand));
		imageLabel.setLayoutX(X-handWidth/2);
		imageLabel.setLayoutY(Y-handHeight/2);
		
		handHold = new Image("pinch.png", handWidth, handHeight, false, false);
		pointed = new Image("point.png", handWidth, handHeight, false, false);
//		imageLabelHold = new Label("",new ImageView(handHold));
//		imageLabelHold.setLayoutX(X-handWidth/2);
//		imageLabelHold.setLayoutY(Y-handHeight/2);
//		
//		imageReturn = imageLabel;
		
		
	}
	
	public void bePointed(){
		//imageReturn = imageLabelHold;
		imageLabel.setGraphic(new ImageView(pointed));
	}
	
	public void bePinched(){
		imageLabel.setGraphic(new ImageView(handHold));
	}
	
	public void beOpen(){
		imageLabel.setGraphic(new ImageView(hand));
	}
	
	public int getX(){
		return X;
	}
	
	public int getY(){
		return Y;
	}
	
	public void setX(int x){
		X = x;
	}
	
	public void setY(int y){
		Y = y;
	}
	
	public Label getCircle(){
		return imageLabel;
	}
	
	public void updatePosition(int x, int y){
		setX(x);
		setY(y);
		imageLabel.setTranslateX(x);
		imageLabel.setTranslateY(y);
//		imageLabelHold.setTranslateX(x);
//		imageLabelHold.setTranslateY(y);
	}
	
	

}
