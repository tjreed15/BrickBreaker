package basics;

import java.awt.Color;
import java.util.LinkedList;

import menu.PauseMenu;
import menu.PauseMenu.PauseSelection;
import powerUps.MovingBrick;
import powerUps.PowerUpDelegate;
import acm.graphics.GCompound;
import acm.graphics.GObject;
import acm.graphics.GRect;
import acm.graphics.GRoundRect;
import acm.util.RandomGenerator;

public class Board extends BoardItem{
	
	//backView holds perimeter and view, view holds everything on board, graphicsOnlyView sits on top
	private View backView, view;
	private GCompound perim;
	private boolean paused;
	private int nBrickSpeedUp, nLives;
	public static int PAUSE_TIME = 50;
	public static boolean BALLS_SHOULD_DIE = true;
	
	//private int direction;
	
	private LinkedList<Brick> bricks;
	private LinkedList<MovingBrick> movingBricks;
	private LinkedList<Paddle> paddles;
	private LinkedList<Ball> balls;
	private PowerUpDelegate powerUps;
	
	public Board(double x, double y, double width, double height){
		view = new View(width, height);
		view.setLocation(x, y);
		nLives = 3;
		//direction = 0;
		bricks = new LinkedList<Brick>();
		movingBricks = new LinkedList<MovingBrick>();
		balls = new LinkedList<Ball>();
		paddles = new LinkedList<Paddle>();
		powerUps = new PowerUpDelegate(this);
		powerUps.setView(view);
	}
	
	public String toString(){
		String toReturn = "width:" + view.getWidth() + " height:" + view.getHeight(); // +  " direction:" + direction; 
		toReturn +=  " X:" + view.getX() + " Y:" + view.getY() + " pauseTime:" + PAUSE_TIME + "\n";
		for(int i = 0; i<bricks.size(); i++){
			toReturn += bricks.get(i) + "\n";
		}
		for(int i = 0; i<paddles.size(); i++){
			toReturn += paddles.get(i) + "\n";
		}
		for(int i = 0; i<balls.size(); i++){
			toReturn += balls.get(i) + "\n";
		}
		return toReturn;
	}
/******************************************************************************************************************
 ***************************	Power Ups			***************************************************************
 ******************************************************************************************************************/	
	//Power Ups can use this for extraneous objects to remove Bricks
	public void externalHit(GObject brick){
		hitBrick(null, brick);
	}
	
	public BoardItem itemAt(double x, double y){
		for(int i = 0; i<balls.size(); i++){
			if(balls.get(i).contains(x, y)) return balls.get(i);
		}
		for(int i = 0; i<paddles.size(); i++){
			if(paddles.get(i).contains(x, y)) return paddles.get(i);
		}
		for(int i = 0; i<bricks.size(); i++){
			if(bricks.get(i).contains(x, y)) return bricks.get(i);
		}
		return null;
	}
	
	public void mousePressed(double x, double y){
		shootBalls();
		shootBullets();
	}
	
	public void removeBall(Ball ball){
		balls.remove(ball);
	}
	
	public void removePaddle(Paddle paddle){
		paddles.remove(paddle);
	}
	
	public void removeBrick(Brick brick){
		bricks.remove(brick);
	}
	
	public void initiatePowerUpBar(double x, double y, double width, double height){
		powerUps.addPowerLabelBar(x, y, width, height);
	}
	
	public void initiateLabelBar(double x, double y, double width, double height){
		powerUps.addLabelBar(x, y, width, height);
	}
	
	public void finishedInit(int level){
		powerUps.changeLevelLabel(level);
		powerUps.changeBrickLabel(bricks.size());
		
	}
	
/******************************************************************************************************************
 ***************************		Game Play		***************************************************************
 ******************************************************************************************************************/
	public void moveObjects(){
		moveBalls();
		moveBricks();
		powerUps.dropPowerUps();
	}
	
	public void movePaddles(double dx, double dy){
		for(int i = 0; i<paddles.size(); i++){
			paddles.get(i).move(dx, dy);
			if (!paddles.get(i).isYMovable()) paddles.get(i).setY(paddles.get(i).getDefaultY());
			else if(paddles.get(i).getY()<paddles.get(i).getMinY()) paddles.get(i).setY(paddles.get(i).getMinY());
			else if(paddles.get(i).getY()>paddles.get(i).getMaxY()) paddles.get(i).setY(paddles.get(i).getMaxY());
			if(paddles.get(i).getX()<0) paddles.get(i).setX(0);
			else if(paddles.get(i).getX()>view.getWidth()-paddles.get(i).getWidth()) paddles.get(i).setX(view.getWidth()-paddles.get(i).getWidth());
		}
	}
	
	 public void moveBalls(){
		LinkedList<Ball> toRemove = new LinkedList<Ball>();
		for (Ball curr : balls){
			curr.move();
			if(curr.getY()<0){
				curr.setYVel(-curr.getYVel());
				curr.move(0, -2*(curr.getY()));
			}
			else if(curr.getY()>(view.getHeight()-curr.getHeight())){
				curr.setYVel(-curr.getYVel());
				curr.move(0, -2*(curr.getY()-(view.getHeight()-curr.getHeight())));
				toRemove.add(curr);
			}
			if(curr.getX()<0){
				curr.setXVel(-curr.getXVel());
				curr.move(-2*(curr.getX()), 0);
			}
			else if(curr.getX()>view.getWidth()-curr.getWidth()){
				curr.setXVel(-curr.getXVel());
				curr.move(-2*(curr.getX()-(view.getWidth()-curr.getWidth())), 0);
			}
			bounce(curr);
		}
		if (BALLS_SHOULD_DIE){ //TODO: Reshoot ball properly
			for(Ball rm : toRemove){
				rm.remove();
				balls.remove(rm);
				nLives--;
				powerUps.changeLifeLabel(nLives);
				rm.setNReshoots(rm.getNReshoots() - 1);
				if(rm.getNReshoots() != 0){
					rm.addTo(view);
					rm.setLocation(rm.getReshootX(), rm.getReshootY());
					balls.add(rm);
				}
			}	
		}
	}
	
	public void moveBricks(){
		for (int i = 0; i<movingBricks.size(); i++){
			movingBricks.get(i).move();
		}
	}
	
	public void startGame(){
		for(int i = 0; i<balls.size(); i++)
			if(balls.get(i).getXVel()==0 && balls.get(i).getYVel()==0)
				balls.get(i).setVelocity(rgen.nextDouble(MIN_X_VEL,MAX_X_VEL), rgen.nextDouble(MIN_Y_VEL,MAX_Y_VEL));
	}
	
	public void shootBalls(){
		for(int i = 0; i<paddles.size(); i++){
			Ball toAdd = paddles.get(i).shootBall();
			if(toAdd != null) balls.add(toAdd);
		}
	}
	
	public void shootBullets(){
		for(int i = 0; i<paddles.size(); i++){
			paddles.get(i).shootBullet(powerUps);
		}
	}
	
	public boolean gameOn(){
		if (nLives == 0){
			return false; // If there are bullets, keep playing
		}
		for(int i = 0; i<bricks.size(); i++){
			if(bricks.get(i).getHealth()>0) return true;
		}
		return false;
	}
	
	public boolean victory(){
		return bricks.size() == 0;
	}
	
	public void pauseMenuSelected(int value){
		this.paused = false;
	}
	
	public void rightArrow(){
		powerUps.rightArrow();
	}
	
	public void leftArrow(){
		powerUps.leftArrow();
	}
	
	public void upArrow(){
		powerUps.upArrow();
	}
	
	public void downArrow(){
		powerUps.downArrow();
	}
	
/******************************************************************************************************************
 ***************************	Initialization		***************************************************************
 ******************************************************************************************************************/
	public void setDirection(int direction){
		//this.direction = direction;
	}
	
	public boolean contains(double x, double y){
		return view.contains(x, y);
	}
	
	
//Adding Objects and adding to screen	
	public void addPaddle(Paddle paddle){ 
		paddles.add(paddle);
		paddle.setY(paddle.getDefaultY());
		paddle.setX((paddle.getX()>=0)? paddle.getX(): 
			(paddles.size()>0)? paddles.get(0).getX() : (view.getWidth()-paddle.getWidth())/2);
		paddle.addTo(view);
		
		DEFAULT_PADDLE_WIDTH = paddle.getWidth();
		DEFAULT_PADDLE_HEIGHT = paddle.getHeight();
		DEFAULT_PADDLE_MAX = paddle.getMaxY();
		DEFAULT_PADDLE_MIN = paddle.getMinY();
	}
	
	public void addBall(Ball ball){ 
		balls.add(ball);
		ball.addTo(view);
		
		DEFAULT_BALL_RADIUS = ball.getRadius();
	}
	
	public void addBrick(Brick brick){
		bricks.add(brick);
		brick.addTo(view);
	}
	
	public void addMovingBrick(MovingBrick brick){
		movingBricks.add(brick);
		bricks.add(brick);
		brick.addTo(view);
	}
	
	public void addTo(View backView){
		this.backView = backView;
		view.setX((view.getX() == -1)? (backView.getWidth()-view.getWidth())/2 : view.getX());
		view.setY((view.getY() == -1)? (backView.getHeight()-view.getHeight())/2 : view.getY());
		addPerimeter();
		backView.add(view);
	}
	
	

	public View addGraphicsOnlyItem(GObject obj){
		view.addGraphicsElement(obj);
		return view;
	}
	
	public View addGraphicsOnlyItemToBack(GObject obj){
		view.addGraphicsElement(obj);
		obj.sendToBack();
		return view;
	}
	
	
	
	
// Setting, moving and adjusting objects on screen	
	public void setPaddleWidth(double paddleWidth){
		for(int i = 0; i<paddles.size(); i++)
			paddles.get(i).setWidth(paddleWidth);
	}
	
	public void setPaddleHeight(double paddleHeight){
		for(int i = 0; i<paddles.size(); i++)
			paddles.get(i).setHeight(paddleHeight);
	}
	
	public void setNBrickSpeedUp(int nBrickSpeedUp){
		this.nBrickSpeedUp = nBrickSpeedUp;
	}
	
	public void setNLives(int nLives){
		this.nLives = nLives;
		powerUps.changeLifeLabel(nLives);
	}
	
	public void setPauseTime(int pauseTime){
		if(pauseTime>MIN_PAUSE_TIME)
			PAUSE_TIME = pauseTime;
	}
	
	public int getNBricks(){
		return bricks.size();
	}
	
	public double getHeight(){
		return view.getHeight();
	}

	public double getWidth(){
		return view.getWidth();
	}

	public double getX(){
		return view.getX();
	}

	public double getY(){
		return view.getY();
	}

	public void setBounds(double x, double y, double width, double height){
		view.setBounds(x, y, width, height);
	}
	
	public void setSize(double width, double height){
		view.setSize(width, height);
		addPerimeter();
	}
	
	public void setHeight(double height){
		view.setHeight(height);
		addPerimeter();
	}

	public void setWidth(double width){
		view.setWidth(width);
		addPerimeter();
	}
	
	public void move(double dx, double dy){
		setLocation(view.getX()+dx, view.getY()+dy);
	}
	
	public void setLocation(double x, double y){
		view.setLocation(x, y);
		addPerimeter();
	}

	public void setX(double x){
		view.setX(x);
		addPerimeter();
	}

	public void setY(double y){
		view.setY(y);
		addPerimeter();
	}

	public Paddle getPaddleAt(double x, double y){
		for(int i = 0; i<paddles.size(); i++){
			if(paddles.get(i).contains(x, y)) return paddles.get(i);
		}
		return null;
	}

	public boolean isBoard(){
		return true;
	}
	
	public boolean isPaused(){
		return paused;
	}
	
	public void setPaused(boolean paused){
		this.paused = paused;
		if(paused){
			System.out.println("PAUSED");
			PauseMenu pause = new PauseMenu(0, 0, 500, 500);
			pause.setdelegate(this);
			pause.addOption("Quit", PauseSelection.QUIT);
			pause.addOption("Continue", PauseSelection.CONTINUE);
			pause.start();
			while (this.paused);
		}
		
	}
	
// Removing graphics items
	public void removeGraphicsOnlyItem(GObject obj){
		view.remove(obj);
	}
	
	public void removeAll(){
		for(int i = 0; i<balls.size(); i++){
			balls.get(i).remove();
		}
		for(int i = 0; i<paddles.size(); i++){
			paddles.get(i).remove();
		}
	}
	
	public void remove(){
		backView.remove(view);
		backView.remove(perim);
	}
	
/******************************************************************************************************************
 ***************************	Private Methods		***************************************************************
 ******************************************************************************************************************/
	private void addPerimeter(){
		try{backView.remove(perim);}catch(Exception e){}
		perim = new GCompound();
		GRect background = new GRect(view.getX(), view.getY(), view.getWidth(), view.getHeight());
		background.setFilled(true); background.setColor(SEE_THROUGH_COLOR);
		perim.add(background);
		for(int i = 0; i<FRAME_WIDTH; i++){
			GRect base = new GRect(view.getX()+i, view.getY()+i, view.getWidth()-(2*i), view.getHeight()-(2*i));
			if(i>0)base.setColor(FRAME_COLOR);
			perim.add(base);
		}
		backView.addGraphicsElement(perim);
		perim.sendToBack();
	}
	
	
	private void bounce(Ball ball){
		GObject tl = view.getElementAt(ball.getX(), ball.getY());
		GObject tr = view.getElementAt(ball.getX()+ ball.getWidth(), ball.getY());
		GObject bl = view.getElementAt(ball.getX(), ball.getY() + ball.getHeight());
		GObject br = view.getElementAt(ball.getX() + ball.getWidth(), ball.getY() + ball.getHeight());
		if (tl != null){
			if(tr != null) doubleHit(ball, TOP_HIT, tl, tr);
			else if(bl != null) doubleHit(ball, LEFT_HIT, tl, bl);
			else singleHit(ball, TOP_LEFT_HIT, tl);
		}
		else if (br != null){
			if(bl != null) doubleHit(ball, BOTTOM_HIT, bl, br);
			else if(tr != null) doubleHit(ball, RIGHT_HIT, tr, br);
			else singleHit(ball, BOTTOM_RIGHT_HIT, br);
		}
		else if(tr != null){
			singleHit(ball, TOP_RIGHT_HIT, tr);
		}
		else if(bl != null){
			singleHit(ball, BOTTOM_LEFT_HIT, bl);
		}
	}
	
	
	private void singleHit(Ball ball, int location, GObject hit){
		if(hitObject(ball, hit)){
			switch(location){
			case TOP_LEFT_HIT: ball.setVelocity(Math.abs(ball.getXVel()), Math.abs(ball.getYVel())); break;
			case TOP_RIGHT_HIT: ball.setVelocity(-Math.abs(ball.getXVel()), Math.abs(ball.getYVel())); break;
			case BOTTOM_LEFT_HIT: ball.setVelocity(Math.abs(ball.getXVel()), -Math.abs(ball.getYVel())); break;
			case BOTTOM_RIGHT_HIT: ball.setVelocity(-Math.abs(ball.getXVel()), -Math.abs(ball.getYVel())); break;
			}
		}
	}
	
	private void doubleHit(Ball ball, int location, GObject hit1, GObject hit2){
		if(hitObject(ball, hit1) || hitObject(ball, hit2)){
			switch(location){
			case TOP_HIT:
				ball.setYVel(Math.abs(ball.getYVel()));
				break;
			case BOTTOM_HIT:
				ball.setYVel(-Math.abs(ball.getYVel()));
				break;
			case LEFT_HIT:
				ball.setXVel(Math.abs(ball.getXVel()));
				break;
			 case RIGHT_HIT:
				 ball.setYVel(-Math.abs(ball.getYVel()));
				break;
			}
		}
	}

	// Returns if the hit will cause the ball to change directions
	private boolean hitObject(Ball ball, GObject obj){
		if (obj==null || powerUps.hitObject(ball, obj)) return false;
		else if(obj.getClass() == BRICK.getClass()){
			hitBrick(ball, obj);
			return !ball.isInvincible();
		}
		else if(obj.getClass() == PADDLE.getClass()){
			hitPaddle(ball, obj);
			return true;
		}
		return false;
	}
	
	private void hitBrick(Ball ball, GObject obj){
		for(int i = 0; i<bricks.size(); i++){
			if (bricks.get(i).isObject(obj)){
				Brick hitBrick = bricks.get(i);
				if(hitBrick.hit(powerUps)){
					hitBrick.remove();
					bricks.remove(hitBrick);
					powerUps.changeBrickLabel(bricks.size());
					if((bricks.size()) % nBrickSpeedUp == 0){
						if(ball != null) powerUps.addPowerUp(hitBrick.getX(), hitBrick.getY(), ball);
						speedUp();
					}
				}
			}
		}
	}

	private void hitPaddle(Ball ball, GObject obj){
		for(int i = 0; i<paddles.size(); i++){
			if (paddles.get(i).isObject(obj) && paddles.get(i).canCatch() && balls.contains(ball)){
				paddles.get(i).catchBall(ball);
				balls.remove(ball);
			}
		}
	}
	
	private void speedUp(){
		if(PAUSE_TIME-TIME_SHIFT>MIN_PAUSE_TIME){
			PAUSE_TIME -= TIME_SHIFT;
		}
	}
	
	
	
	
	
	public static final double MAX_X_VEL = 10;
	public static final double MAX_Y_VEL = 10;
	public static final double MIN_X_VEL = 5;
	public static final double MIN_Y_VEL = 5;
	private static final int MIN_PAUSE_TIME = 1;
	private static final int TIME_SHIFT = 5;
	private static final int FRAME_WIDTH = 5;
	private static final Color FRAME_COLOR = Color.white;
	private static final Color SEE_THROUGH_COLOR = new Color(255, 255, 255, 100);
	
	public static double DEFAULT_PADDLE_WIDTH;
	public static double DEFAULT_PADDLE_HEIGHT;
	public static double DEFAULT_PADDLE_MAX;
	public static double DEFAULT_PADDLE_MIN;
	public static double DEFAULT_BALL_RADIUS;
	public static double DEFAULT_BALL_XVEL;
	public static double DEFAULT_BALL_YVEL;
	
	
	
	
	
	
	
	
	/**
	 * 	1	2	3
	 * 	4		5
	 * 	6	7	8
	 */
	private static final int TOP_LEFT_HIT = 1;
	private static final int TOP_HIT = 2;
	private static final int TOP_RIGHT_HIT = 3;
	private static final int LEFT_HIT = 4;
	private static final int RIGHT_HIT = 5;
	private static final int BOTTOM_LEFT_HIT = 6;
	private static final int BOTTOM_HIT = 7;
	private static final int BOTTOM_RIGHT_HIT = 8;
	
	private static final RandomGenerator rgen = new RandomGenerator();
	private static final GRoundRect BRICK = new GRoundRect(0, 0);
	private static final GRect PADDLE = new GRect(0, 0);
	
}
