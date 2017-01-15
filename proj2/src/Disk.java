import javafx.scene.media.*;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.image.*;

public class Disk {
	// instance variables
	private int diskWidth;// depending on the value of the disk
	private final int diskHeight = 70;
	private int value;// 0-6
	Image image;
	private double x, y;// location
	private Label imageLabel;
	private Tower home;
	AudioClip bm = null;
	boolean firstTime = true;
	Media BGM;
	MediaPlayer mediaPlayer;
	
	public Disk(int value, String url) {
		this.value = value;
		diskWidth = 400;
		image = new Image(url, diskWidth, diskHeight, true, false);
		imageLabel = new Label("", new ImageView(image));
	}

	public Label getImage() {
		return imageLabel;
	}

	public void moveTo(double newX, double newY) {
		imageLabel.setTranslateX(newX - (imageLabel.getLayoutX() + diskWidth / 2));
		imageLabel.setTranslateY(newY - (imageLabel.getLayoutY() + diskHeight / 2));
	}

	public int getValue() {
		return value;
	}

	public void setHome(Tower t) {
		home = t;
	}

	public Tower getHome() {
		return home;
	}
	
	public void setMusic(String url)
	{
		bm = new AudioClip(getClass().getClassLoader().getResource(url).toString());
	}
	
	public void playMusic()
	{
		bm.play(0.8);
	}
	
	public int firstT()
	{
		if (firstTime)
		{
			firstTime = false;			
			return value;
		}
		else
			return 0;
	}
	
}
