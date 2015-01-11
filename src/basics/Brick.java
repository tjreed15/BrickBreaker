package basics;

import java.awt.Color;

import powerUps.BrickExploder;
import powerUps.PowerUpDelegate;

import acm.graphics.GObject;
import acm.graphics.GRoundRect;

public class Brick extends BoardItem{
	
	private int points;
	private int health; //-1: unbreakable... {-inf, -2]: not needed to win, but will break if hit
	protected GRoundRect brick;
	protected View view;
	
	
	public Brick(double x, double y, double width, double height){
		this.points = 0;
		this.health = 1;
		brick = new GRoundRect(x, y, width, height);
		brick.setFilled(true);
		brick.setFillColor(getColor());
		
	}
	
	public boolean isMovingBrick(){
		return false;
	}
	
	public void addTo(View view){
		this.view = view;
		view.add(brick);
	}
	
	public String toString(){
		String toReturn = "[X:" +  brick.getX() + " Y:" +  brick.getY();
		toReturn += " width:" +  brick.getWidth() + " height:" +  brick.getHeight();
		toReturn += " health:" + health + " points:" + points;
		return toReturn + "]";
	}
	
	
/******************************************************************************************************************
 ***************************		Initializers	***************************************************************
 ******************************************************************************************************************/
	public boolean isBrick(){
		return true;
	}
	
	public void move(double dx, double dy){
		brick.move(dx, dy);
	}
	
	public void setLocation(double x, double y){
		double dy = y-brick.getY();
		double dx = x-brick.getX();
		move(dx, dy);
	}
	
	public void setSize(double width, double height){
		brick.setSize(width, height);
	}
	
	public void setWidth(double width){
		brick.setSize(width, brick.getHeight());
	}
	
	public double getWidth(){
		return brick.getWidth();
	}
	
	public void setHeight(double height){
		brick.setSize(brick.getWidth(), height);
	}
	
	public double getHeight(){
		return brick.getHeight();
	}
	
	public void setX(double x){
		brick.setLocation(x, brick.getY());
	}
	
	public double getX(){
		return brick.getX();
	}
	
	public void setY(double y){
		brick.setLocation(brick.getX(), y);
	}
	
	public double getY(){
		return brick.getY();
	}
	
	public boolean contains(double x, double y){
		return brick.contains(x, y);
	}
	
	
	public void remove(){
		view.remove(brick);
	}
	
	public boolean isObject(GObject obj){
		return obj == brick;
	}
	
	
	public void setPoints(int points){
		this.points = points;
		brick.setFillColor(getColor());
	}
	
	public int getPoints(){
		return points;
	}
	
	public void setHealth(int health){
		this.health = health;
	}
	
	public int getHealth(){
		return health;
	}
	
	
	
/******************************************************************************************************************
 ***************************		Game Time		***************************************************************
 ******************************************************************************************************************/	
	//Returns if it is killed or not
	public boolean hit(PowerUpDelegate powerUps){
		if(health == -1) return false;
		health--;
		if(health<=0){
			if(powerUps.brickAnimation()){
				BrickExploder exp = new BrickExploder(brick, powerUps);
				exp.addTo(view);
				exp.start();
			}
			return true;
		}
		return false;
	}
	
	
	
/******************************************************************************************************************
 ***************************	Private Methods		***************************************************************
 ******************************************************************************************************************/
	private Color getColor(){
		switch(points){
		case -2: return Color.magenta; //Indestructable... Shield
		// -1 is used as the "invisible" bricks that only affect setup, not runtime
		case 0: return Color.white; // Blockade bricks
		case 1: return Color.cyan;
		case 2: return Color.green;
		case 3: return Color.yellow;
		case 4: return Color.orange;
		case 5: return Color.red;
		default: return Color.black;
		}
	}
	
	
	
}
