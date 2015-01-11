package powerUps;

import java.util.LinkedList;
import acm.graphics.GObject;
import acm.graphics.GRect;
import acm.graphics.GRoundRect;
import acm.util.RandomGenerator;
import basics.Ball;
import basics.Board;
import basics.Brick;
import basics.Paddle;
import basics.View;

public class PowerUpDelegate {

	private Board board;
	private View view;
	private LinkedList<PowerUpDrop> powerUps;
	private LinkedList<Ball> ballControl;
	private LinkedList<PacMan> pacMen;
	private LinkedList<Ball> offScreenBalls;
	private LinkedList<GRect> leftCups;
	private LinkedList<GRect> rightCups;
	private LabelBar labelBar, powerLabel;
	private boolean collide;
	
	
	public PowerUpDelegate(Board board){
		this.board = board;
		powerUps = new LinkedList<PowerUpDrop>();
		ballControl = new LinkedList<Ball>();
		offScreenBalls = new LinkedList<Ball>();
		pacMen = new LinkedList<PacMan>();
		leftCups = new LinkedList<GRect>();
		rightCups = new LinkedList<GRect>();
		powerLabel = null;
	}
	
	public void setView(View view){
		this.view = view;
	}
	
	public void addPowerLabelBar(double x, double y, double width, double height){
		View powerLabelView = new View(width, height);
		powerLabelView.setLocation(x, y);
		view.add(powerLabelView);
		GRect frame = new GRect(x, y, width, height);
		powerLabelView.add(frame);
		powerLabel = new LabelBar();
		powerLabel.addTo(powerLabelView);
	}
	
	public void addLabelBar(double x, double y, double width, double height){
		View labelView = new View(width, height);
		labelView.setLocation(x, y);
		view.add(labelView);
		GRect frame = new GRect(x, y, width, height);
		labelView.add(frame);
		labelBar = new LabelBar();
		labelBar.addTo(labelView);
		labelBar.addLabel("Level: 1", -1);
		labelBar.addLabel("Lives: 3", -1);
		labelBar.addLabel("Bullets: 0", -1);
		labelBar.addLabel("Bricks: ", -1);
	}
	
	public void changeBrickLabel(int nBricks){
		labelBar.updateLabel("Bricks: ", "Bricks: " + nBricks);
	}
	
	public void changeBulletLabel(int nBullets){
		labelBar.updateLabel("Bullets: ", "Bullets: " + nBullets);
	}
	
	public void changeLifeLabel(int nLives){
		labelBar.updateLabel("Lives: ", "Lives: " + nLives);
	}
	
	public void changeLevelLabel(int level){
		labelBar.updateLabel("Level: ", "Level: " + level);
	}
	
	public void dropPowerUps(){
		for (int i = 0; i<powerUps.size(); i++){
			PowerUpDrop pow = powerUps.get(i);
			pow.drop();
			if(pow.getY()>(view.getHeight()-pow.getHeight())){
				pow.remove();
				powerUps.remove(pow);
			}
			for(int j=0; j<NCHECK_SPOTS; j++){
				double x = pow.getX() + (j*pow.getWidth()/(NCHECK_SPOTS-1));
				double y = pow.getY() + (j*pow.getHeight()/(NCHECK_SPOTS-1));
				Paddle catcher = board.getPaddleAt(x, y);
				if(catcher != null){
					pow.remove();
					powerUps.remove(pow);
					executePowerUp(pow, catcher);
					break;
				}
			}
		}
	}
	
	
	public void addPowerUp(double x, double y, Ball ball){
		PowerUpDrop pow = new PowerUpDrop(x, y);
		pow.addBall(ball);
		pow.addTo(view);
		powerUps.add(pow);
		pow.setPowerType(rgen.nextInt(1, MAX_POWER_UP));
		pow.setPowerType(INVINCIBLE_TYPE);
		
	}


	private void executePowerUp(PowerUpDrop powerUp, Paddle paddle){
		updatePowerLabel(powerUp.getPowerType());
		switch(powerUp.getPowerType()){
		case BIG_BALL_TYPE:
			changeRadius(powerUp.getBall(), CHANGE_RADIUS_AMOUNT);
			break;
		case MULTIPLE_BALL_TYPE:
			this.addBall(paddle.getX()+(paddle.getWidth()/2)-Board.DEFAULT_BALL_RADIUS, 
					paddle.getY()-2*Board.DEFAULT_BALL_RADIUS, Board.DEFAULT_BALL_RADIUS, 
					rgen.nextDouble(Board.MIN_X_VEL, Board.MAX_X_VEL), 
					rgen.nextDouble(Board.MIN_Y_VEL, Board.MAX_Y_VEL));
			break;
		case WIDE_PADDLE_TYPE:
			this.extendPaddle(paddle, EXTEND_PADDLE_AMOUNT);
			break;
		case Y_MOVABLE_TYPE:
			this.setPaddleYMovable(paddle, true);
			break;
		case MULTIPLE_PADDLE_TYPE:
			this.addPaddle(Board.DEFAULT_PADDLE_WIDTH, Board.DEFAULT_PADDLE_HEIGHT,
					rgen.nextDouble(Board.DEFAULT_PADDLE_MIN, Board.DEFAULT_PADDLE_MAX), 
					Board.DEFAULT_PADDLE_MAX, Board.DEFAULT_PADDLE_MIN);
			break;
		case CATCH_BALL_TYPE:
			this.catchBall(paddle);
			break;
		case SHEILD_TYPE:
			this.addShield(board);
			break;
		case INVINCIBLE_TYPE:
			this.makeInvincible(powerUp.getBall());
			break;
		case CUP_PADDLE_TYPE:
			this.addCupPaddle(paddle);
			break;
		case DYNAMITE_TYPE:
			this.setBrickExplosionShouldCollide(true);
			break;
		case BULLET_TYPE:
			this.giveBullets(paddle, NBULLETS_PER_POWER_UP);
			break;
		case PAC_MAN_TYPE:
			this.turnBallIntoPacMan(powerUp.getBall());
			break;
		case BALL_CONTROL_TYPE:
			this.controlBall(powerUp.getBall());
			break;
		default: 
			System.out.println("NO SUCH POWER UP TYPE");
			System.exit(1);
		}
	}
	
	public void timerEndedWithAction(char action, Ball ball, Paddle paddle){
		switch(action){
		case YMOVEABLE_OVER_TYPE:
			paddle.setYMovable(false);
			paddle.setY(paddle.getDefaultY());
			break;
		case MULTIPLE_PADDLE_OVER_TYPE:
			paddle.remove();
			board.removePaddle(paddle);
			break;
		case BALL_CONTROL_OVER_TYPE:
			unControlBall(ball);
			break;
		case DYNAMITE_OVER_TYPE:
			collide = false;
			break;
		case INVINCIBLE_BALL_OVER_TYPE:
			ball.setInvincible(false);
			break;
		}
	}
	
	
	public boolean hitObject(Ball ball, GObject obj){
		for(int i = 0; i<leftCups.size(); i++){
			if(obj == leftCups.get(i)){
				if(ball.getXVel()<0) ball.setXVel(-ball.getXVel());
				return true;
			}
		}
		for(int i = 0; i<rightCups.size(); i++){
			if(obj == rightCups.get(i)){
				if(ball.getXVel()>0) ball.setXVel(-ball.getXVel());
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
	
	
	
	public void addBall(double x, double y, double radius, double xVel, double yVel){
		Ball ball = new Ball(x, y, radius);
		board.addBall(ball);
		ball.setVelocity(xVel, yVel);
	}
	
	public void extendPaddle(Paddle paddle, double dx){
		paddle.setWidth(paddle.getWidth()+dx);
	}
	
	public void setPaddleYMovable(Paddle paddle, boolean paddleMovable){
		paddle.setYMovable(paddleMovable);
		Timer timer = new Timer(Y_MOVEABLE_TIME, this, YMOVEABLE_OVER_TYPE);
		timer.addPaddle(paddle);
		timer.start();
	}
	
	public void changeRadius(Ball ball, double dr){
		ball.setRadius(ball.getRadius()+dr);
	}
	
	public void addPaddle(double paddleWidth, double paddleHeight, double paddleY, double maxPaddleY, double minPaddleY){
		Paddle paddle = new Paddle(-1, paddleY, paddleWidth, paddleHeight);
		paddle.setMaxY(maxPaddleY);
		paddle.setMinY(minPaddleY);
		board.addPaddle(paddle);
		Timer timer = new Timer(MULTIPLE_PADDLE_TIME, this, MULTIPLE_PADDLE_OVER_TYPE);
		timer.addPaddle(paddle);
		timer.start();
	}
	
	public void catchBall(Paddle paddle){
		paddle.addCatches(NCATCHES_PER_POWERUP);
	}
	
	public void addShield(Board board){
		Brick shield = new Brick(0, board.getHeight()-SHIELD_HEIGHT, board.getWidth(), SHIELD_HEIGHT);
		shield.setPoints(-2);
		shield.setHealth(-2);
		board.addBrick(shield);
	}
	
	public void makeInvincible(Ball ball){
		ball.setInvincible(true);
		Timer timer = new Timer(INVINCIBLE_BALL_TIME, this, INVINCIBLE_BALL_OVER_TYPE);
		timer.addBall(ball);
		timer.start();
	}
	
	
	public void freezePaddle(Paddle paddle){
		paddle.setFrozen(true);
	}
	
	public void explosionCollide(GRoundRect rect){
		board.externalHit(rect);
	}
	
	public void addExplosionPiece(GRect piece){
		board.addGraphicsOnlyItem(piece);
	}
	
	public void removeExplosionPiece(GRect piece){
		board.removeGraphicsOnlyItem(piece);
	}
	
	public void giveBullets(Paddle paddle, int nBullets){
		paddle.addBullets(nBullets);
		changeBulletLabel(paddle.getNBullets());
	}
	
	public void bulletHit(GObject hit){
		board.externalHit(hit);
	}
	
	public boolean brickAnimation(){
		return true;
	}
	
	
	public void controlBall(Ball ball){
		if(!ball.isControlled()){
			this.ballControl.add(ball);
			ball.setControlled(true);
			Timer timer = new Timer(BALL_CONTROL_TIME, this, BALL_CONTROL_OVER_TYPE);
			timer.addBall(ball);
			timer.start();
		}
	}

	public void unControlBall(Ball ball){
		if(ballControl.contains(ball)){
			this.ballControl.remove(ball);
			ball.setControlled(false);
		}
	}
	
	public void rightArrow(){
		for(int i = 0; i<ballControl.size(); i++){
			ballControl.get(0).move(BALL_CONTROL_SPEED, 0);
		}
		for(int i = 0; i<pacMen.size(); i++){
			pacMen.get(i).move(pacMen.get(i).getSpeed(), 0);
		}
	}
	
	public void leftArrow(){
		for(int i = 0; i<ballControl.size(); i++){
			ballControl.get(0).move(-BALL_CONTROL_SPEED, 0);
		}
		for(int i = 0; i<pacMen.size(); i++){
			pacMen.get(i).move(-pacMen.get(i).getSpeed(), 0);
		}
	}
	
	public void upArrow(){
		for(int i = 0; i<ballControl.size(); i++){
			ballControl.get(0).move(0, -BALL_CONTROL_SPEED);
		}
		for(int i = 0; i<pacMen.size(); i++){
			pacMen.get(i).move(0, -pacMen.get(i).getSpeed());
		}
	}
	
	public void downArrow(){
		for(int i = 0; i<ballControl.size(); i++){
			ballControl.get(0).move(0, BALL_CONTROL_SPEED);
		}
		for(int i = 0; i<pacMen.size(); i++){
			pacMen.get(i).move(0, pacMen.get(i).getSpeed());
		}
	}
	
	public Ball turnBallIntoPacMan(Ball ball){
		if(ball == null) return null;
		offScreenBalls.add(ball);
		ball.remove();
		board.removeBall(ball);
		PacMan pacMan = new PacMan(ball.getX(), ball.getY(), ball.getRadius(), this);
		pacMen.add(pacMan);
		pacMan.addTo(view);
		pacMan.setStrengthAttributes(5, 5);
		pacMan.start();
		return ball;
	}
	
	public void pacHit(GObject obj){
		board.externalHit(obj);
	}
	
	public void removePac(PacMan pacMan){
		Ball toAdd = offScreenBalls.removeFirst();
		toAdd.setX(pacMan.getX()); toAdd.setY(pacMan.getY());
		board.addBall(toAdd);
		
		pacMan.remove();
		pacMen.remove(pacMan);
	}
	
	
	public void setBrickExplosionShouldCollide(boolean collide){
		this.collide = collide;
		Timer timer = new Timer(DYNAMITE_TIME, this, DYNAMITE_OVER_TYPE);
		timer.start();
	}
	
	public boolean brickExplosionShouldCollide(){
		return collide;
	}
	
	public View addShadow(GObject shadow){
		return board.addGraphicsOnlyItemToBack(shadow);
	}
	
	public void removeShadow(GRect shadow){
		board.removeGraphicsOnlyItem(shadow);
	}
	
	public void addCupPaddle(Paddle paddle){
		GRect leftCup = new GRect(paddle.getX(), paddle.getY()-CUP_HEIGHT, CUP_WIDTH, CUP_HEIGHT);
		leftCup.setFilled(true); leftCup.setFillColor(paddle.getColor());
		paddle.addPowerUpGraphic(leftCup);
		leftCups.add(leftCup);
		GRect rightCup = new GRect(paddle.getX()+paddle.getWidth()-CUP_WIDTH, paddle.getY()-CUP_HEIGHT, CUP_WIDTH, CUP_HEIGHT);
		rightCup.setFilled(true); rightCup.setFillColor(paddle.getColor());
		paddle.addPowerUpGraphic(rightCup);
		rightCups.add(rightCup);
	}
	
	
	private void updatePowerLabel(int powerType){
		if(powerLabel != null){
			String updateString = "";
			int fadeTime = LabelBar.DEFAULT_FADE_TIME;
			switch(powerType){
			case BIG_BALL_TYPE:
				updateString = "Big Ball";
				break;
			case MULTIPLE_BALL_TYPE:
				updateString = "Multiple Balls";
				break;
			case WIDE_PADDLE_TYPE:
				updateString = "Wide Paddle";
				break;
			case Y_MOVABLE_TYPE:
				updateString = "Full Motion";
				fadeTime = Y_MOVEABLE_TIME;
				break;
			case MULTIPLE_PADDLE_TYPE:
				updateString = "Multiple Paddles";
				fadeTime = MULTIPLE_PADDLE_TIME;
				break;
			case CATCH_BALL_TYPE:
				updateString = "Catch Ball";
				break;
			case SHEILD_TYPE:
				updateString = "Shield";
				break;
			case INVINCIBLE_TYPE:
				updateString = "InvinciBall";
				fadeTime = INVINCIBLE_BALL_TIME;
				break;
			case CUP_PADDLE_TYPE:
				updateString = "Cup Paddle";
				break;
			case DYNAMITE_TYPE:
				updateString = "Dynamite Bricks";
				fadeTime = DYNAMITE_TIME;
				break;
			case BULLET_TYPE:
				updateString = "Bullets";
				break;
			case PAC_MAN_TYPE:
				updateString = "PacMan";
				break;
			case BALL_CONTROL_TYPE:
				updateString = "Ball Control";
				fadeTime = BALL_CONTROL_TIME;
				break;
			}
			powerLabel.addLabel(updateString, fadeTime);
		}
	}
	
	
	private static final double SHIELD_HEIGHT = 20;
	private static final double CUP_HEIGHT = 30;
	private static final double CUP_WIDTH = 10;
	private static final int NCHECK_SPOTS = 3;
	private static final double CHANGE_RADIUS_AMOUNT = 5;
	private static final double EXTEND_PADDLE_AMOUNT = 5;
	private static final int NBULLETS_PER_POWER_UP = 5;
	private static final double BALL_CONTROL_SPEED = 5;
	private static final int NCATCHES_PER_POWERUP = 3;
	
	private static final int BIG_BALL_TYPE = 1;
	private static final int MULTIPLE_BALL_TYPE = 2;
	private static final int WIDE_PADDLE_TYPE = 3;
	private static final int Y_MOVABLE_TYPE = 4;
	private static final int MULTIPLE_PADDLE_TYPE = 5;
	private static final int CATCH_BALL_TYPE = 6;
	private static final int SHEILD_TYPE = 7;
	private static final int INVINCIBLE_TYPE = 8;
	private static final int CUP_PADDLE_TYPE = 9;
	private static final int DYNAMITE_TYPE = 10;
	private static final int BULLET_TYPE = 11;
	private static final int PAC_MAN_TYPE = 12;
	private static final int BALL_CONTROL_TYPE = 13;
	
	private static final int MAX_POWER_UP = 13;
	//private static final int FREEZE_PADDLE_TYPE = -1;
	
	private static final char YMOVEABLE_OVER_TYPE = 'a';
	private static final char MULTIPLE_PADDLE_OVER_TYPE = 'b';
	private static final char BALL_CONTROL_OVER_TYPE = 'c';
	private static final char DYNAMITE_OVER_TYPE = 'd';
	private static final char INVINCIBLE_BALL_OVER_TYPE = 'e';
	//private static final char CUP_PADDLE_OVER_TYPE = '';
	
	
	private static final int Y_MOVEABLE_TIME = 10000;
	private static final int MULTIPLE_PADDLE_TIME = 10000;
	private static final int BALL_CONTROL_TIME= 10000;
	private static final int DYNAMITE_TIME = 5000;
	private static final int INVINCIBLE_BALL_TIME = 1500;
	//private static final int CUP_PADDLE_TIME = 1000;
	
	
	private RandomGenerator rgen = new RandomGenerator();
}
