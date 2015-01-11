package powerUps;

import java.awt.Color;

import acm.graphics.GObject;
import acm.graphics.GRect;
import basics.Paddle;
import basics.View;

public class Bullet extends Thread{

	private double xVel, yVel;
	private int strength;
	
	private View view;
	private GRect bullet;
	private PowerUpDelegate powerUps;
	
	public Bullet(PowerUpDelegate powerUps, int strength){
		this.powerUps = powerUps;
		this.strength = strength;
		bullet = new GRect(BULLET_WIDTH, BULLET_HEIGHT);
		bullet.setFilled(true); bullet.setFillColor(getColor());
	}
	
	public void shoot(Paddle paddle, View view, double xVel, double yVel){
		this.view = view;
		this.xVel = xVel;
		this.yVel = yVel;
		bullet.setLocation(paddle.getX()+(paddle.getWidth()-BULLET_WIDTH)/2, paddle.getY()-BULLET_HEIGHT);
		view.add(bullet);
		start();
	}
	
	public void run(){
		while(true){
			bullet.move(xVel, -yVel); 
			if(bullet.getX()<0  || bullet.getY()<0 || bullet.getX()>view.getWidth() || bullet.getY()>view.getHeight()){
				view.remove(bullet);
				break;
			}
			GObject hit = view.getElementAt(bullet.getX()+bullet.getWidth(), bullet.getY()-1);
			if(hit != null){
				view.remove(bullet);
				powerUps.bulletHit(hit);
				break;
			}
			try {Thread.sleep(PAUSE_TIME);} catch (InterruptedException e){e.printStackTrace();}
		}
	}
	
	
	
	
	private Color getColor(){
		switch(strength){
		default: return Color.cyan;
		}
	}
	
	
	private static final double BULLET_WIDTH = 4;
	private static final double BULLET_HEIGHT = 10;
	private static final int PAUSE_TIME = 5;
}
