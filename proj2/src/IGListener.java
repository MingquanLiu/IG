import java.util.concurrent.TimeUnit;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

class IGListener extends Listener {
	
	private IG ig; 
	
	public IGListener(IG ig){
		this.ig = ig;
	}
    public void onInit(Controller controller) {
        System.out.println("Initialized");
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected");
    }

    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
    }

    public void onExit(Controller controller) {
        System.out.println("Exited");
    }

    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();
        System.out.println("Frame id: " + frame.id()
                         + ", timestamp: " + frame.timestamp()
                         + ", hands: " + frame.hands().count()
                         + ", fingers: " + frame.fingers().count());

        //Get hands
        for(Hand hand : frame.hands()) {
            String handType = hand.isLeft() ? "Left hand" : "Right hand";
            System.out.println("  " + handType + ", id: " + hand.id()
                             + ", palm position: " + hand.palmPosition());

            if (isPoint(hand, 70.0f))
            	System.out.println("pointed");
            else if (isPinch(hand, 80.0f))
            	System.out.println("pinched");
            else 
            	System.out.println("open");
            
        }

        if (!frame.hands().isEmpty()) {
            System.out.println();
        }
        
        try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        
    }
    
    private boolean isPinch(Hand hand, float radius)
    {
		int pinches = 0;
		Vector thumbpos = hand.fingers().get(0).tipPosition();
		System.out.println(thumbpos);
		for (int x = 1; x < 5; x++) {
			System.out.println(hand.fingers().get(x).tipPosition());
			if (distance(thumbpos, hand.pointables().get(x).tipPosition()) < radius)
				pinches++;
		}
		
		return pinches > 2;
    }
    
    private boolean isPoint(Hand hand, float radius)
    {
    	int inrange = 0; 
    	
    	Vector indexpos = hand.fingers().get(1).tipPosition();
    	System.out.println(indexpos);
    	for (Finger finger : hand.fingers())
    		if (distance(indexpos, finger.tipPosition()) < radius)
    			inrange++;
    	return inrange == 1;
    }
    
    private double distance(Vector t, Vector f)
    {
    	return Math.sqrt(Math.pow((t.getX()-f.getX()), 2) 
    			+ Math.pow((t.getY()-f.getY()), 2)
    			+ Math.pow((t.getZ()-f.getZ()), 2));
    }
    
    private double map(double imin, double imax, double fmin, double fmax, double val){
    	return (fmax-fmin)/(imax-imin)*val+imin;
    }
}