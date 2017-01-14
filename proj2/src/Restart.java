import javafx.scene.media.*;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.image.*;

public class Restart {
	final static int width = 150, height = 37;
	private Image image = new Image("restart.png", width, height, true, false);
	private Label imageLabel = new Label("", new ImageView(image));
	private int x, y;
	
	private Image image2 = new Image("restart2.png");
	private Label imageLabel2 = new Label("", new ImageView(image2));
	
	Restart(int x, int y)
	{
		this.x = x; 
		this.y = y; 
		imageLabel.setLayoutX(x);
		imageLabel.setLayoutY(y);
		imageLabel2.setVisible(false);
		imageLabel2.setLayoutX(750-137);
		imageLabel2.setLayoutY(450-84);
	}
	
	public Label getImage(){
		return imageLabel;
	}
	
	public int getUpperBound(){
		return y; 
	}
	
	public int getLowerBound(){
		return y + height;
	}
	
	public int getLeftBound(){
		return x;
	}
	
	public int getRightBound(){
		return x + width;
	}
	
	public boolean onClick(int x, int y){
		return y > getUpperBound() && y < getLowerBound() 
				&& x > getLeftBound() && x < getRightBound();
	}
	
	public Label questionBox(){
		return imageLabel2;
	}
	
	public boolean onClickYes(int x, int y){//-80-20, 20-60 
		return false;
	}
	
	public boolean onClickNo(int x, int y){//60-150, 20-60
		return false;
	}
	
}
