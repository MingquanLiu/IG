//import com.sun.media.jfxmedia.AudioClip;

import java.io.File;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.*;
import javafx.stage.FileChooser;            

public class BackGround {

	private int BGHeight = 900;
	private int BGWidth = 1500;
	Image left, mid, right;
	Label imageLabel;
	Media BGM;
	MediaPlayer mediaPlayer;
	MediaView mediaView;
	AudioClip sound;
	public BackGround()
	{
		left = new Image("bg left.png", BGWidth, BGHeight, false, false);
		mid = new Image("bg mid.png", BGWidth, BGHeight, false, false);
		right = new Image("bg right.png", BGWidth, BGHeight, false, false);
		imageLabel = new Label("", new ImageView(mid));
		BGM = new Media(getClass().getClassLoader().getResource("BGMBind.wav").toString());
		
		
//		 mediaView.setMediaPlayer(mediaPlayer);
	}
	
	public Label getLabel()
	{
		return imageLabel;
	}
	
	public void changeBG(int area)
	{
		switch (area) {
		case 1:
			imageLabel.setGraphic(new ImageView(left));
			break;
		case 2:
			imageLabel.setGraphic(new ImageView(mid));
			break;
		case 3:
			imageLabel.setGraphic(new ImageView(right));
			break;
		}
	}
	
	public void playMusic()
	{
		 mediaPlayer = new MediaPlayer(BGM);
		 mediaPlayer.setVolume(0.4);
		 mediaPlayer.setAutoPlay(true);
		 mediaPlayer.setCycleCount(mediaPlayer.INDEFINITE);
//		sound.play();
	}
	
	public void play()
	{
		 mediaPlayer.setAutoPlay(true);
//		 mediaPlayer.setVolume(0.4);
		System.out.println(mediaPlayer.getVolume()); 
//		sound.play();
	}
}
