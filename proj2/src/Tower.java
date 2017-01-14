import java.util.Stack;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tower {
	//instance variables
		private final int towerWidth = 250;
		private final int towerHeight = 400;
		Image image = new Image("towerImage.png", towerWidth, towerHeight, false, false);
		private double x,y;//location
		private Label imageLabel = new Label("", new ImageView(image));
		private Stack<Disk> diskStack = new Stack<Disk>();
		
		public Tower(double x, double y){
			this.x = x;
			this.y = y;
			imageLabel.setLayoutX(x-towerWidth/2);
			imageLabel.setLayoutY(y-towerHeight/2);
		}
		
		public Label getImage(){
			return imageLabel;
		}
		
		public boolean addDisk(Disk disk, Tower t)
		{
			if(diskStack.peek()==null)
			{
				diskStack.push(disk);
				return true;
			}
			if(disk.getValue()<diskStack.peek().getValue())
			{
				diskStack.push(disk);
				return true;
			}
			
			t.addDisk(disk, this);
			return false;
		}
		
		public Disk getTop()
		{
			return diskStack.pop();
		}
	
}
