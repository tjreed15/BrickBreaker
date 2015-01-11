package powerUps;

import java.awt.Color;

import basics.View;
import acm.graphics.GArc;
import acm.graphics.GObject;

public class PacMan extends Thread{
	
	private GArc pac;
	private double angleOffset, directionAngle, speed;
	private PowerUpDelegate powerUps;
	private View view;
	private double time, maxTime;
	
	public PacMan(double x, double y, double radius, PowerUpDelegate powerUps){
		this.powerUps = powerUps;
		pac = new GArc(radius*2, radius*2, 0, 360);
		pac.setFilled(true); pac.setFillColor(Color.yellow);
		pac.setLocation(x, y);
		angleOffset = 0;
	}
	
	
	public void run(){
		boolean grow = true;
		while(time<maxTime){
			pac.setStartAngle(directionAngle+angleOffset);
			pac.setSweepAngle(360-(2*angleOffset));
			angleOffset = (grow)? angleOffset+ANGLE_OFFSET_CHANGE:angleOffset-ANGLE_OFFSET_CHANGE;
			if(angleOffset>MAX_ANGLE_OFFSET || angleOffset<0) grow = !grow;
			try {Thread.sleep(PAUSE_TIME);} catch (InterruptedException e){e.printStackTrace();}
			time += PAUSE_TIME;
		}
		powerUps.removePac(this);
	}
	
	
	public void addTo(View view){
		this.view = view;
		view.add(pac);
	}
	
	public void remove(){
		view.remove(pac);
	}
	
	public void move(double dx, double dy){
		if(pac.getX()+dx>0 && pac.getX()+dx+pac.getWidth()<view.getWidth() && 
				pac.getY()+dy>0 && pac.getY()+dy+pac.getHeight()<view.getWidth()){
			pac.move(dx, dy);
			checkHit();
		}		
		directionAngle = (dx>0)? 0:180;
		if(dy==0) return;
		directionAngle = (dy<0)? 90:270;
	}

	public void setStrengthAttributes(int time, double speed){
		this.speed = speed;
		this.maxTime = time*1000; // Seconds -> Milliseconds
	}
	
	public double getSpeed(){
		return speed;
	}
	
	public double getX(){
		return pac.getX();
	}
	
	public double getY(){
		return pac.getY();
	}
	
	
	
	private void checkHit(){
		GObject tl = view.getElementAt(pac.getX(), pac.getY());
		GObject tr = view.getElementAt(pac.getX()+ pac.getWidth(), pac.getY());
		GObject bl = view.getElementAt(pac.getX(), pac.getY()+pac.getHeight());
		GObject br = view.getElementAt(pac.getX()+pac.getWidth(), pac.getY()+ pac.getHeight());
		
		if (tl != null) powerUps.pacHit(tl);
		if (br != null) powerUps.pacHit(br);
		if (tr != null) powerUps.pacHit(tr);
		if (bl != null) powerUps.pacHit(bl);
	}
	
	private static final double MAX_ANGLE_OFFSET = 45;
	private static final double ANGLE_OFFSET_CHANGE = 5;
	private static final int PAUSE_TIME = 50;
}
