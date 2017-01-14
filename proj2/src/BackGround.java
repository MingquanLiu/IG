import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BackGround {

	private int BGHeight = 900;
	private int BGWidth = 1500;
	Image left, mid, right;
	Label imageLabel;
	public BackGround()
	{
		left = new Image("bg.png", BGWidth, BGHeight, false, false);
		mid = new Image("bg.png", BGWidth, BGHeight, false, false);
		right = new Image("bg.png", BGWidth, BGHeight, false, false);
		imageLabel = new Label("", new ImageView(mid));
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
}
