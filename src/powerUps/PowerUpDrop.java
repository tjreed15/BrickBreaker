package powerUps;

import java.awt.Color;

import basics.Ball;
import basics.View;
import acm.graphics.GRect;

public class PowerUpDrop {

	private GRect powerUp;
	private double velocity;
	private int powerType;
	private View view;
	private Ball ball;
	
	public PowerUpDrop(double x, double y){
		velocity = 5;
		ball = null;
		powerType = 0;
		powerUp = new GRect(x, y, WIDTH, HEIGHT);
		powerUp.setFilled(true);
		powerUp.setFillColor(getColor());
	}
	
	public void addTo(View view){
		this.view = view;
		view.add(powerUp);
	}
	
	public void move(double dx, double dy){
		powerUp.move(dx, dy);
	}
	
	public void drop(){
		powerUp.move(0, velocity);
	}
	
	public void remove(){
		view.remove(powerUp);
	}
	
	
	
	public Ball getBall(){
		return ball;
	}
	
	public void addBall(Ball ball){
		this.ball = ball;
	}
	
	public int getPowerType(){
		return powerType;
	}
	
	public void setPowerType(int type){
		powerType = type;
	}
	
	public double getVelocity(){
		return velocity;
	}
	
	public void setVelocity(double velocity){
		this.velocity = velocity;
	}
	
	public double getX(){
		return powerUp.getX();
	}
	
	public void setX(double x){
		powerUp.setBounds(x, powerUp.getY(), powerUp.getWidth(), powerUp.getHeight());
	}
	
	public double getY(){
		return powerUp.getY();
	}
	
	public void setY(double y){
		powerUp.setBounds(powerUp.getX(), y, powerUp.getWidth(), powerUp.getHeight());
	}
	
	
	
	public double getWidth(){
		return powerUp.getWidth();
	}
	
	public void setWidth(double width){
		powerUp.setBounds(powerUp.getX(), powerUp.getY(), width, powerUp.getHeight());
	}
	
	public double getHeight(){
		return powerUp.getHeight();
	}
	
	public void setHeight(double height){
		powerUp.setBounds(powerUp.getX(), powerUp.getY(), powerUp.getWidth(), height);
	}
	
	
	
	private Color getColor(){
		return Color.white;
	}
	
	
	private static final double WIDTH = 5;
	private static final double HEIGHT = 15;
	
}
