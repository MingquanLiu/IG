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
	Media BGM,WinningMusic,BGM3,BGM4,BGM5,BGM6;
	MediaPlayer mediaPlayer;
	AudioClip sound;
	public BackGround()
	{
		left = new Image("bg left.png", BGWidth, BGHeight, false, false);
		mid = new Image("bg mid.png", BGWidth, BGHeight, false, false);
		right = new Image("bg right.png", BGWidth, BGHeight, false, false);
		imageLabel = new Label("", new ImageView(mid));
		BGM = new Media(getClass().getClassLoader().getResource("BGM.mp3").toString());
		WinningMusic = new Media(getClass().getClassLoader().getResource("WinningMusic.mp3").toString());
		BGM3 =new Media(getClass().getClassLoader().getResource("BGM3.mp3").toString());
		BGM4 =new Media(getClass().getClassLoader().getResource("BGM4.mp3").toString());
		BGM5 =new Media(getClass().getClassLoader().getResource("BGM5.mp3").toString());
		BGM6 =new Media(getClass().getClassLoader().getResource("BGM6.mp3").toString());
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
	
	public void play(int c)
	{

		switch (c) {
		case 0:
		case 1:
		case 2:	
			break;
		case 3:	
			mediaPlayer.stop();
			playM(BGM3);
			break;
		case 4:	
			mediaPlayer.stop();
			playM(BGM4);
			break;
		case 5:	
			mediaPlayer.stop();
			playM(BGM5);
			break;
		case 6:	
			mediaPlayer.stop();
			playM(BGM6);
			break;
		}
	}
	
	public void playM(Media music)
	{
		 mediaPlayer = new MediaPlayer(music);
		 mediaPlayer.setVolume(0.4);
		 mediaPlayer.setAutoPlay(true);
		 mediaPlayer.setCycleCount(mediaPlayer.INDEFINITE);
	}
	
	public void playMusic()
	{
//		 mediaPlayer = new MediaPlayer(BGM);
//		 mediaPlayer.setVolume(0.4);
//		 mediaPlayer.setAutoPlay(true);
//		 mediaPlayer.setCycleCount(mediaPlayer.INDEFINITE);
		playM(BGM);
//		sound.play();
	}
	
	public void playWinningMusic()
	{
//		 mediaPlayer = new MediaPlayer(WinningMusic);		
//		 mediaPlayer.setVolume(0.4);
//		 mediaPlayer.setAutoPlay(true);
//		 mediaPlayer.setCycleCount(mediaPlayer.INDEFINITE);
		playM(WinningMusic);
	}
	
	public void stop()
	{
		if(mediaPlayer!=null)
		mediaPlayer.stop();
	}
	
	
	public void play()
	{
		 mediaPlayer.setAutoPlay(true);
//		 mediaPlayer.setVolume(0.4);
//		System.out.println(mediaPlayer.getVolume()); 
//		sound.play();
	}
}
