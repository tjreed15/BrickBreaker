package powerUps;

import basics.Ball;
import basics.Paddle;

public class Timer extends Thread{

	private PowerUpDelegate delegate;
	private char action;
	private int time;
	private Ball ball;
	private Paddle paddle;
	
	public Timer(int time, PowerUpDelegate delegate, char action){
		this.time = time;
		this.delegate = delegate;
		this.action = action;
		paddle = null; ball = null;
	}
	
	public void run(){
		try{Thread.sleep(time);}
		catch(Exception e){e.printStackTrace(); System.exit(1);}
		delegate.timerEndedWithAction(action, ball, paddle);
	}
	
	public void addBall(Ball ball){
		this.ball = ball;
	}
	
	public void addPaddle(Paddle paddle){
		this.paddle = paddle;
	}
	
	
	
}
