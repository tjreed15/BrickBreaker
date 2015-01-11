package basics;

import java.awt.Color;
import java.util.LinkedList;

import powerUps.Bullet;
import powerUps.PowerUpDelegate;
import powerUps.ShooterArrow;

import acm.graphics.GObject;
import acm.graphics.GRect;

public class Paddle extends BoardItem{
	
	private int health;
	private double defaultY;
	private View view;
	private GRect paddle;
	
	//Power Ups
//	private PowerUpDelegate powerUps;
	private double maxY, minY, velocity, bulletYVelocity; //How fast the user is moving it
	private int nBullets, nCatches, bulletStrength; 
	private boolean yMovable, frozen;
	private LinkedList<Ball> caughtBalls;
	private LinkedList<ShooterArrow> shooters;
	private LinkedList<GObject> powerUpGraphics;
	
	
	
	public Paddle(double x, double y, double width, double height){
		paddle = new GRect(x, y, width, height);
		paddle.setFilled(true);
		paddle.setFillColor(getColor());
		defaultY = y;
		
		health = -1;
		nCatches = 0;
		yMovable = false;
		caughtBalls = new LinkedList<Ball>();
		shooters = new LinkedList<ShooterArrow>();
		powerUpGraphics = new LinkedList<GObject>();
		nBullets = 0; bulletStrength = 1; bulletYVelocity = DEFAULT_BULLET_VEL;
	}
	
	public String toString(){
		String toReturn = "{X:" +  paddle.getX() + " defaultY:" +  defaultY;
		toReturn += " width:" +  paddle.getWidth() + " height:" +  paddle.getHeight();
		toReturn += " maxY:" + maxY + " minY:" + minY + " health:" + health;
		return toReturn + "}";
	}
	
	
/******************************************************************************************************************
 ***************************		Power Ups		***************************************************************
 ******************************************************************************************************************/	
	public void addBullets(int nBullets){
		this.nBullets += nBullets;
	}
	
	public int getNBullets(){
		return nBullets;
	}
		
	public void shootBullet(PowerUpDelegate powerUps){
		if(nBullets>0){
			Bullet toShoot = new Bullet(powerUps, bulletStrength);
			toShoot.shoot(this, view, velocity, bulletYVelocity);
			nBullets--;
		}
		powerUps.changeBulletLabel(nBullets);
	}
	
	public void addPowerUpGraphic(GObject obj){
		powerUpGraphics.add(obj);
		view.add(obj);
	}
	
	public void removePowerUpGraphic(GObject obj){
		powerUpGraphics.remove(obj);
		view.remove(obj);
	}
			

/******************************************************************************************************************
 ***************************		Game Play		***************************************************************
 ******************************************************************************************************************/
	
	public void move(double dx, double dy){
		if(frozen) return;
		paddle.move(dx, dy);
		for(int i = 0; i<caughtBalls.size(); i++){
			caughtBalls.get(i).move(dx, dy);
		}
		for(int i = 0; i<powerUpGraphics.size(); i++){
			powerUpGraphics.get(i).move(dx, dy);
		}
	}
	
	public void setYMovable(boolean yMovable){
		this.yMovable = yMovable;
	}
	
	public boolean isYMovable(){
		return yMovable;
	}
	
	public void addCatches(int nCatches){
		this.nCatches += nCatches;
	}
	
	public boolean canCatch(){
		return nCatches != 0; //nCatches<0 will be infinite amount of catches
	}
	
	public void catchBall(Ball ball){
		nCatches--;
		ShooterArrow toAdd = new ShooterArrow(this, Math.sqrt(Math.pow(ball.getXVel(), 2) + Math.pow(ball.getYVel(), 2)), view);
		ball.setVelocity(0, 0);
		ball.setX(paddle.getX()+paddle.getWidth()/2-ball.getRadius());
		ball.setY(paddle.getY()+paddle.getHeight());
		shooters.add(toAdd);
		toAdd.start();
		caughtBalls.add(ball);
	}
	
	public Ball shootBall(){
		if(!caughtBalls.isEmpty()){
			double xVelocity = shooters.get(0).getXVelocity();
			double yVelocity = shooters.get(0).getYVelocity();
			caughtBalls.get(0).setX(paddle.getX()+paddle.getWidth()/2-caughtBalls.get(0).getRadius());
			caughtBalls.get(0).setY(paddle.getY()-caughtBalls.get(0).getHeight());
			caughtBalls.get(0).setVelocity(xVelocity, yVelocity);
			shooters.get(0).remove();
			shooters.remove(0);
			return caughtBalls.remove(0);
		}
		return null;
	}
	
	
	
	

/******************************************************************************************************************
 ***************************		Initializers	***************************************************************
 ******************************************************************************************************************/
	public boolean isPaddle(){
		return true;
	}
	
	public void setX(double x){
		double dx = x-paddle.getX();
		move(dx, 0);
	}
	
	public double getX(){
		return paddle.getX();
	}
	
	public void setY(double y){
		double dy = y-paddle.getY();
		move(0, dy);
	}
	
	public double getY(){
		return paddle.getY();
	}
	
	public void setLocation(double x, double y){
		double dy = y-paddle.getY();
		double dx = x-paddle.getX();
		move(dx, dy);
	}
	
	public void setDefaultY(double defaultY){
		this.defaultY = defaultY;
		double dy = defaultY-paddle.getY();
		move(0, dy);
	}
	
	public double getDefaultY(){
		return defaultY;
	}
	
	public void setMaxY(double maxY){
		this.maxY = maxY;
	}
	
	public double getMaxY(){
		return maxY;
	}
	
	public void setMinY(double minY){
		this.minY = minY;
	}
	
	public double getMinY(){
		return minY;
	}
	
	public void setSize(double width, double height){
		paddle.setSize(width, height);
	}
	
	public void setWidth(double width){
		paddle.setSize(width, paddle.getHeight());
	}
	
	public double getWidth(){
		return paddle.getWidth();
	}
	
	public void setHeight(double height){
		paddle.setSize(paddle.getWidth(), height);
	}
	
	public double getHeight(){
		return paddle.getHeight();
	}
	
	public boolean contains(double x, double y){
		return paddle.contains(x, y);
	}
	
	public void setHealth(int health){
		this.health = health;
		paddle.setFillColor(getColor());
	}
	
	public int getHealth(){
		return health;
	}
	
	public void addTo(View view){
		this.view = view;
		view.add(paddle);
	}
	
	public void remove(){
		view.remove(paddle);
	}
	
	public boolean isObject(GObject obj){
		return obj == paddle;
	}
	
	public boolean isFrozen(){
		return frozen;
	}
	
	public void setFrozen(boolean frozen){
		this.frozen = frozen;
	}
	
	
	

/******************************************************************************************************************
 ***************************	Private Methods		***************************************************************
 ******************************************************************************************************************/	
	public Color getColor(){
		switch(health){
		case 1: return Color.red;
		case 2: return Color.yellow;
		default: return Color.white;
		}
	}

	
	
	private static final double DEFAULT_BULLET_VEL = 10;
	
}
