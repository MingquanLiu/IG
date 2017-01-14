import java.util.Stack;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tower {
	//instance variables
		private final int towerWidth = 250;
		private final int towerHeight = 300;
		Image image = new Image("towerImage.png", towerWidth, towerHeight, false, false);
		private double x,y;//location
		private Label imageLabel = new Label("", new ImageView(image));
		private Stack<Disk> diskStack = new Stack<Disk>();
		private int count;
		private int area;
		public Tower(double x, double y,int area){
			this.x = x;
			this.y = y;
			imageLabel.setLayoutX(x-towerWidth/2);
			imageLabel.setLayoutY(y-towerHeight/2);
			count =0;
			this.area = area;
		}
		
		public Label getImage(){
			return imageLabel;
		}
		
		public boolean addDisk(Disk disk)
		{
			if(diskStack.isEmpty()||disk.getValue()<diskStack.peek().getValue())
			{
				disk.setHome(this);
				diskStack.push(disk);
				count++;
				disk.moveTo(x, 670-count*30);
				return true;
			}
			
			disk.getHome().addDisk(disk);
			return false;
		}

		public Disk getTop()
		{
			count--;
			return diskStack.pop();
			
		}
		
		public int getArea()
		{
			return area;
		}
		
		public boolean stackEmpty()
		{
			return diskStack.isEmpty();
				
		}
		
}
