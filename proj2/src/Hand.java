import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Hand {
	

	public static final int BALL_RADIUS = 20;
	private int X, Y; 
	
	public static final int MAX_X = IG.WIDTH;
	public static final int MAX_Y = IG.HEIGHT;
	
	private Circle circle; 
	
	public Hand()
	{
		X = MAX_X/2;
		Y = MAX_Y/2;
		circle = new Circle(X, Y, BALL_RADIUS);
		circle.setFill(Color.BLACK);
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
	
	public Circle getCircle(){
		return circle;
	}
	
	public void updatePosition(int x, int y){
		setX(x);
		setY(y);
		circle.setTranslateX(x - (circle.getLayoutX() + BALL_RADIUS));
		circle.setTranslateY(y - (circle.getLayoutY() + BALL_RADIUS));
	}

}
