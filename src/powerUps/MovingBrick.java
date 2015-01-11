package powerUps;

import java.util.LinkedList;
import basics.Brick;

public class MovingBrick extends Brick{
	
	//private PowerUpDelegate powerUps;
	private double velocity;
	private LinkedList<Double> xPoints, yPoints;
	private int currLocation, lastLocation;
	private double[] distance = new double[2];
	
	
	public MovingBrick(double x, double y, double width, double height){
		super(x, y, width, height);
		xPoints = new LinkedList<Double>();
		yPoints = new LinkedList<Double>();
		velocity = 10;
		currLocation = 0; lastLocation = -1;
	}
	
	public boolean isMovingBrick(){
		return true;
	}
	
	public String toString(){
		String toReturn = super.toString();
		toReturn = "<" + toReturn.substring(1, toReturn.length()-1) + " velocity:" + velocity;
		for(int i = 0; i<xPoints.size(); i++){
			toReturn += " pathX:" + xPoints.get(i) + " pathY:" + yPoints.get(i);
 		}
		return toReturn + ">";
	}
	

/******************************************************************************************************************
 ***************************		Game Time		***************************************************************
 ******************************************************************************************************************/	
	public void setVelocity(double velocity){
		this.velocity = velocity;
	}
		
	public void move(){
		if(nearDestination()){
			brick.setLocation(xPoints.get(currLocation)-brick.getWidth()/2, yPoints.get(currLocation)-brick.getHeight()/2);
			calculateNextDestination();
		}
		double dx = (xPoints.get(currLocation) - (brick.getX()+brick.getWidth()/2));
		double dy = (yPoints.get(currLocation) - (brick.getY()+brick.getHeight()/2));
		double theta = Math.atan(dy/dx); //In Radians (from -Pi/2 to Pi/2)
		if(dx<0) theta += Math.PI;
		brick.move(velocity*Math.cos(theta), velocity*Math.sin(theta));
	}
	
	
	
	
	
	
	
	
	public void addPathX(double x){
		xPoints.add(x);
	}
	
	public void addPathY(double y){
		yPoints.add(y);
		lastLocation ++;
	}
	
	public void addPathXDist(double dx){
		double x = (lastLocation == -1)?  (brick.getX()+brick.getWidth()/2)+dx : xPoints.get(lastLocation) + dx;
		xPoints.add(x);
	}
	
	public void addPathYDist(double dy){
		double y = (lastLocation == -1)?  (brick.getY()+brick.getHeight()/2)+dy : yPoints.get(lastLocation)+dy;
		yPoints.add(y);
		lastLocation ++;
	}
	
	
	
	
	
	
	
	
	private boolean nearDestination(){
		return Math.abs((brick.getX()+brick.getWidth()/2)-xPoints.get(lastLocation)) >= distance[0] &&
			(Math.abs((brick.getY()+brick.getHeight()/2)-yPoints.get(lastLocation)) >= distance[1]);
	}
	
	private void calculateNextDestination(){
		lastLocation = currLocation;
		currLocation = (currLocation==xPoints.size()-1)? 0:currLocation+1;
		distance[0] = Math.abs(xPoints.get(currLocation) - xPoints.get(lastLocation));
		distance[1] = Math.abs(yPoints.get(currLocation) - yPoints.get(lastLocation));
	}
	
}

