package powerUps;

import acm.graphics.GLine;
import basics.Paddle;
import basics.View;

public class ShooterArrow extends Thread{

	private double velocity;
	private Paddle paddle;
	private GLine arrow;
	private View view;
	private int time;
	
	public ShooterArrow(Paddle paddle, double velocity, View view){
		this.paddle = paddle;
		this.velocity = velocity;
		this.view = view;
		time = 0;
		arrow = new GLine(paddle.getX()+(paddle.getWidth()/2), paddle.getY(), paddle.getX()+paddle.getWidth(), paddle.getY());
		view.add(arrow);
	}
	
	public void run(){
		while(true){
			arrow.setStartPoint(paddle.getX()+(paddle.getWidth()/2), paddle.getY());
			arrow.setEndPoint(paddle.getX()+(paddle.getWidth()/2)+((paddle.getWidth()/2)*Math.cos(Math.toRadians(time))),
					paddle.getY()+(paddle.getWidth()/2)*-Math.abs(Math.sin(Math.toRadians(time))));
			try {Thread.sleep(PAUSE_TIME);} catch (InterruptedException e){e.printStackTrace();}
			time+=velocity;
		}
	}
	
	public double getXVelocity(){
		return velocity*Math.cos(Math.toRadians(time)); //Rounding when casting slowed it down (add 1)
	}
	
	public double getYVelocity(){
		return -Math.abs(velocity*Math.sin(Math.toRadians(time))); //Rounding when casting slowed it down (sub 1)
	}
	
	public void remove(){
		view.remove(arrow);
	}
	
	private static final int PAUSE_TIME = 100;
}
