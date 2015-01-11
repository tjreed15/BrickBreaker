package basics;

import java.awt.Color;
import acm.graphics.GOval;

public class Ball extends BoardItem{
	
	private double xVelocity, yVelocity, reshootX, reshootY;
	private int nPaddleBounces, nReshoots;
	private GOval ball;
	private View view;
	
	private boolean invincible, controlled;
	
	public Ball(double x, double y, double radius){
		ball = new GOval(x, y, radius*2, radius*2);
		ball.setFilled(true);
		ball.setFillColor(getColor());
		reshootX = x;
		reshootY = y;
		this.xVelocity = this.yVelocity = 0;
		nPaddleBounces = nReshoots = -1;
	}
	
	public String toString(){
		String toReturn = "(X:" +  ball.getX() + " Y:" +  ball.getY();
		toReturn += " radius:" +  ball.getWidth()/2;
		toReturn += " xVel:" + xVelocity + " yVel:" + yVelocity;
		toReturn +=  " nPaddleBounces:" + nPaddleBounces;
		toReturn +=  " nReshoots:" + nReshoots;
		return toReturn + ")";
	}
	
	
/******************************************************************************************************************
 ***************************		Initializers	***************************************************************
 ******************************************************************************************************************/
	public boolean isBall(){
		return true;
	}
	
	public void move(double dx, double dy){
		ball.move(dx, dy);
	}
	
	public void setLocation(double x, double y){
		double dy = y-ball.getY();
		double dx = x-ball.getX();
		move(dx, dy);
	}
	
	public void setX(double x){
		ball.setLocation(x, ball.getY());
	}
	
	public double getX(){
		return ball.getX();
	}
	
	public void setY(double y){
		ball.setLocation(ball.getX(), y);
	}
	
	public double getY(){
		return ball.getY();
	}
	
	public void setSize(double width, double height){
		ball.setSize(width, height);
	}
	
	public double getWidth(){
		return ball.getWidth();
	}
	
	public double getHeight(){
		return ball.getHeight();
	}
	
	public double getRadius(){
		return ball.getWidth()/2;
	}
	
	public void setRadius(double rad){
		ball.setSize(rad*2, rad*2);
	}
	
	public boolean contains(double x, double y){
		return ball.contains(x, y);
	}
	
	public void setNPaddleBounces(int nBounces){
		this.nPaddleBounces = nBounces;
		ball.setFillColor(getColor());
	}
	
	public int getNPaddleBounces(){
		return nPaddleBounces;
	}
	
	public void setNReshoots(int nReshoots){
		this.nReshoots = nReshoots;
	}
	
	public int getNReshoots(){
		return nReshoots;
	}
	
	public void addTo(View view){
		this.view = view;
		ball.setLocation(ball.getX(), ball.getY());
		view.add(ball);
	}
	
	public void setReshootX(double x){
		reshootX = x;
	}
	
	public void setReshootY(double y){
		reshootY = y;
	}
	
	public double getReshootX(){
		return reshootX;
	}
	
	public double getReshootY(){
		return reshootY;
	}
	
	public void remove(){
		view.remove(ball);
	}
	
	public void setVelocity(double xVelocity, double yVelocity){
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
	}
	
	public void move(){
		double x = (controlled)? 0:xVelocity;
		ball.move(x, yVelocity);
	}
	
	public double getXVel(){
		return xVelocity;
	}
	
	public void setXVel(double xVelocity){
		this.xVelocity = xVelocity;
	}
	
	public double getYVel(){
		return yVelocity;
	}
	
	public void setYVel(double yVelocity){
		this.yVelocity = yVelocity;
	}
	
	public boolean isInvincible(){
		return invincible;
	}
	
	public void setInvincible(boolean invincible){
		this.invincible = invincible;
	}
	
	public boolean isControlled(){
		return controlled;
	}
	
	public void setControlled(boolean controlled){
		this.controlled = controlled;
	}
		
		
/******************************************************************************************************************
 ***************************		Game Play		***************************************************************
 ******************************************************************************************************************/

		
	
	
	
/******************************************************************************************************************
 ***************************	Initialization		***************************************************************
 ******************************************************************************************************************/
	
	private Color getColor(){
		switch(nPaddleBounces){
		case 1: return Color.cyan;
		case 2: return Color.green;
		case 3: return Color.yellow;
		case 4: return Color.orange;
		case 5: return Color.red;
		default: return Color.black;
		}
	}
}
