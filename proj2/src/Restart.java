import javafx.scene.media.*;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.image.*;

public class Restart {
	final static int width = 150, height = 37;
	private Image image = new Image("restart.png", width, height, true, false);
	private Label imageLabel = new Label("", new ImageView(image));
	private int x, y;
	
	Restart(int x, int y)
	{
		this.x = x; 
		this.y = y; 
		imageLabel.setLayoutX(x);
		imageLabel.setLayoutY(y);
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
	
}
