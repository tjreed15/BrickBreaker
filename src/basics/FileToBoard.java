package basics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import powerUps.MovingBrick;

public class FileToBoard {

	private Board board;
	private double xSep, ySep, brickWidth, brickHeight;
	private double ballRadius, paddleWidth, paddleHeight, paddleY, paddleMaxY, paddleMinY;
	
	public FileToBoard(){}
	
	public Board getBoard(int level){
		board = new Board(-1, -1, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		try {
			Scanner LFS = new Scanner(new File(FILE_NAME+level));
			if(LFS.hasNextLine()) initLevelVariables(LFS.nextLine());
			double x; double y = ySep;
			while(LFS.hasNextLine()){
				x = xSep;
				String row = LFS.nextLine();
				Scanner lineScanner = new Scanner(row);
				lineScanner.useDelimiter("[\\]\\}\\)\\>]");
				while(lineScanner.hasNext()){
					String next = lineScanner.next().trim();
					if(next.isEmpty()) continue;
					switch(next.charAt(0)){
					case '[':
						Brick brick = new Brick(x, y, brickWidth, brickHeight);
						defineBrick(brick, next.substring(1));
						x += xSep+brickWidth;
						break;
					case '{':
						Paddle paddle = new Paddle(x, paddleY, paddleWidth, paddleHeight);
						definePaddle(paddle, next.substring(1));
						x += xSep+brickWidth;
						break;
					case '(':
						Ball ball = new Ball(x, y, ballRadius);
						defineBall(ball, next.substring(1));
						x += xSep+brickHeight;
						break;
					case '<':
						MovingBrick move = new MovingBrick(x, y, brickWidth, brickHeight);
						defineMovingBrick(move, next.substring(1));
						x += xSep+brickWidth;
						break;
					}			
				}
				y += ySep + brickHeight;
			}
			board.initiateLabelBar(board.getWidth(), 0, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_HEIGHT);
			board.initiatePowerUpBar(board.getWidth(), DEFAULT_LABEL_HEIGHT, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_WIDTH);
			board.finishedInit(level);
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return board;
	}
	
	
	
	
	
	
	
	private void initLevelVariables(String code){
		double x = -1;
		double y = -1;
		double width = DEFAULT_WIDTH;
		double height = DEFAULT_HEIGHT;
		paddleY = DEFAULT_HEIGHT-DEFAULT_PADDLE_Y_OFFSET;
		paddleMaxY = DEFAULT_HEIGHT-DEFAULT_MAX_PADDLE_Y_OFFSET;
		paddleMinY = DEFAULT_HEIGHT-DEFAULT_MIN_PADDLE_Y_OFFSET;
		int direction = 0;
		paddleWidth = DEFAULT_PADDLE_WIDTH;
		paddleHeight = DEFAULT_PADDLE_HEIGHT;
		ballRadius = DEFAULT_BALL_RADIUS;
		int nBrickSpeedUp = DEFAULT_NBRICK_SPEEDUP;
		int pauseTime = DEFAULT_PAUSE_TIME;
		xSep = DEFAULT_X_SEP;
		ySep = DEFAULT_Y_SEP;
		brickWidth = DEFAULT_BRICK_WIDTH;
		brickHeight = DEFAULT_BRICK_HEIGHT;
		
		Scanner lineScanner = new Scanner(code);
		while(lineScanner.hasNext()){
			String keyValue = lineScanner.next();
			if(keyValue.contains("width")){
				width = Double.valueOf(keyValue.substring(keyValue.indexOf(":")+1));
			}
			else if(keyValue.contains("height")){
				height = Double.valueOf(keyValue.substring(keyValue.indexOf(":")+1));
				paddleY = height-DEFAULT_PADDLE_Y_OFFSET;
				paddleMaxY = height-DEFAULT_MAX_PADDLE_Y_OFFSET;
				paddleMinY = height-DEFAULT_MIN_PADDLE_Y_OFFSET;
			}
			else if(keyValue.contains("direction")){
				direction = Integer.valueOf(keyValue.substring(keyValue.indexOf(":")+1));
			}
			else if(keyValue.contains("paddleWidth")){
				paddleWidth = Double.valueOf(keyValue.substring(keyValue.indexOf(":")+1));
			}
			else if(keyValue.contains("paddleHeight")){
				paddleHeight = Double.valueOf(keyValue.substring(keyValue.indexOf(":")+1));
			}
			else if(keyValue.contains("paddleY")){
				paddleY = Double.valueOf(keyValue.substring(keyValue.indexOf(":")+1));
			}
			else if(keyValue.contains("xSep")){
				xSep = Double.valueOf(keyValue.substring(keyValue.indexOf(":")+1));
			}
			else if(keyValue.contains("ySep")){
				ySep = Double.valueOf(keyValue.substring(keyValue.indexOf(":")+1));
			}
			else if(keyValue.contains("defaultWidth")){
				brickWidth = Double.valueOf(keyValue.substring(keyValue.indexOf(":")+1));
			}
			else if(keyValue.contains("defaultHeight")){
				brickHeight = Double.valueOf(keyValue.substring(keyValue.indexOf(":")+1));
			}
			else if(keyValue.contains("X")){
				x = Double.valueOf(keyValue.substring(keyValue.indexOf(":")+1));
			}
			else if(keyValue.contains("Y")){
				y = Double.valueOf(keyValue.substring(keyValue.indexOf(":")+1));
			}
			else if(keyValue.contains("pauseTime")){
				pauseTime = Integer.valueOf(keyValue.substring(keyValue.indexOf(":")+1));
			}
			else if(keyValue.contains("nBrickSpeedUp")){
				nBrickSpeedUp = Integer.valueOf(keyValue.substring(keyValue.indexOf(":")+1));
			}
		}
		board.setBounds(x, y, width, height);
		board.setDirection(direction);
		board.setNBrickSpeedUp(nBrickSpeedUp);
		board.setPauseTime(pauseTime);
	}
	
	
	private void defineBall(Ball ball, String attributes){
		Scanner brickScanner = new Scanner(attributes);
		while(brickScanner.hasNext()){
			String attribute = brickScanner.next();
			if(attribute.contains("radius")){
				double newRadius = Double.valueOf(attribute.substring(attribute.indexOf(":")+1));
				ball.setRadius(newRadius);
			}
			else if(attribute.contains("X")){
				double x = Double.valueOf(attribute.substring(attribute.indexOf(":")+1));
				x = (x<0)? (board.getWidth()-ball.getWidth())/2:x;
				ball.setX(x);
				ball.setReshootX(x);
			}
			else if(attribute.contains("xVel")){
				ball.setVelocity(Double.valueOf(attribute.substring(attribute.indexOf(":")+1)), ball.getYVel());
			}
			else if(attribute.contains("yVel")){
				ball.setVelocity(ball.getXVel(), Double.valueOf(attribute.substring(attribute.indexOf(":")+1)));
			}
			else if(attribute.contains("Y")){
				double y = Double.valueOf(attribute.substring(attribute.indexOf(":")+1));
				y = (y<0)? (board.getHeight()-ball.getHeight())/2:y;
				ball.setY(y);
				ball.setReshootY(y);
			}
			else if(attribute.contains("nReshoots")){
				ball.setNReshoots(Integer.valueOf(attribute.substring(attribute.indexOf(":")+1)));
			}
			else if(attribute.contains("nPaddleBounces")){
				ball.setNPaddleBounces(Integer.valueOf(attribute.substring(attribute.indexOf(":")+1)));
			}
			
		}
		board.addBall(ball);
	}
	
	private void definePaddle(Paddle paddle, String attributes){
		paddle.setDefaultY(paddleY);
		paddle.setMaxY(paddleMaxY);
		paddle.setMinY(paddleMinY);
		Scanner brickScanner = new Scanner(attributes);
		while(brickScanner.hasNext()){
			String attribute = brickScanner.next();
			if(attribute.contains("width")){
				double newWidth = Double.valueOf(attribute.substring(attribute.indexOf(":")+1));
				if(newWidth<0){ //If we want to offset from being brick-aligned
					paddle.setX(paddle.getX() + brickWidth + newWidth);
					newWidth *= -1;
				}
				paddle.setWidth(newWidth);
			}
			else if(attribute.contains("height")){
				double newHeight = Double.valueOf(attribute.substring(attribute.indexOf(":")+1));
				if(newHeight<0){//If we want to offset from being brick-aligned
					paddle.setX(paddle.getX() + brickHeight + newHeight);
					newHeight *= -1;
				}
				paddle.setHeight(newHeight);
			}
			else if(attribute.contains("defaultY")){
				paddle.setDefaultY(Double.valueOf(attribute.substring(attribute.indexOf(":")+1)));
			}
			else if(attribute.contains("maxY")){
				paddle.setMaxY(Double.valueOf(attribute.substring(attribute.indexOf(":")+1)));
			}
			else if(attribute.contains("minY")){
				paddle.setMinY(Double.valueOf(attribute.substring(attribute.indexOf(":")+1)));
			}
			else if(attribute.contains("health")){
				paddle.setHealth(Integer.valueOf(attribute.substring(attribute.indexOf(":")+1)));
			}
			else if(attribute.contains("X")){
				double x = Double.valueOf(attribute.substring(attribute.indexOf(":")+1));
				x = (x<0)? (board.getWidth()-paddle.getWidth())/2:x;
				paddle.setX(x);
			}
		}
		if(paddle.getHealth() != 0) board.addPaddle(paddle);
	}
	
	private void defineMovingBrick(MovingBrick brick, String attributes){
		Scanner brickScanner = new Scanner(attributes);
		while(brickScanner.hasNext()){
			String attribute = brickScanner.next();
			if(attribute.contains("width")){
				double newWidth = Double.valueOf(attribute.substring(attribute.indexOf(":")+1));
				if(newWidth<0){
					brick.setX(brick.getX() + brickWidth + newWidth);
					newWidth *= -1;
				}
				brick.setWidth(newWidth);
			}
			else if(attribute.contains("height")){
				double newHeight = Double.valueOf(attribute.substring(attribute.indexOf(":")+1));
				if(newHeight<0){
					brick.setX(brick.getX() + brickHeight + newHeight);
					newHeight *= -1;
				}
				brick.setHeight(newHeight);
			}
			else if(attribute.contains("points")){
				brick.setPoints(Integer.valueOf(attribute.substring(attribute.indexOf(":")+1)));
			}
			else if(attribute.contains("velocity")){
				brick.setVelocity(Double.valueOf(attribute.substring(attribute.indexOf(":")+1)));
			}
			else if(attribute.contains("pathXDist")){
				brick.addPathXDist(Double.valueOf(attribute.substring(attribute.indexOf(":")+1)));
			}
			else if(attribute.contains("pathYDist")){
				brick.addPathYDist(Double.valueOf(attribute.substring(attribute.indexOf(":")+1)));
			}
			else if(attribute.contains("pathX")){
				brick.addPathX(Double.valueOf(attribute.substring(attribute.indexOf(":")+1)));
			}
			else if(attribute.contains("pathY")){
				brick.addPathY(Double.valueOf(attribute.substring(attribute.indexOf(":")+1)));
			}
			else if(attribute.contains("centerX")){
				brick.setX(Double.valueOf(attribute.substring(attribute.indexOf(":")+1))-brick.getWidth()/2);
			}
			else if(attribute.contains("centerY")){
				brick.setY(Double.valueOf(attribute.substring(attribute.indexOf(":")+1))-brick.getHeight()/2);
			}
			else if(attribute.contains("X")){
				brick.setX(Double.valueOf(attribute.substring(attribute.indexOf(":")+1)));
			}
			else if(attribute.contains("Y")){
				brick.setY(Double.valueOf(attribute.substring(attribute.indexOf(":")+1)));
			}
		}
		if(brick.getPoints() > -1) board.addMovingBrick(brick);
	}
	
	private void defineBrick(Brick brick, String attributes){
		Scanner brickScanner = new Scanner(attributes);
		while(brickScanner.hasNext()){
			String attribute = brickScanner.next();
			if(attribute.contains("width")){
				double newWidth = Double.valueOf(attribute.substring(attribute.indexOf(":")+1));
				if(newWidth<0){
					brick.setX(brick.getX() + brickWidth + newWidth);
					newWidth *= -1;
				}
				brick.setWidth(newWidth);
			}
			else if(attribute.contains("height")){
				double newHeight = Double.valueOf(attribute.substring(attribute.indexOf(":")+1));
				if(newHeight<0){
					brick.setX(brick.getX() + brickHeight + newHeight);
					newHeight *= -1;
				}
				brick.setHeight(newHeight);
			}
			else if(attribute.contains("points")){
				brick.setPoints(Integer.valueOf(attribute.substring(attribute.indexOf(":")+1)));
			}
			else if(attribute.contains("X")){
				brick.setX(Double.valueOf(attribute.substring(attribute.indexOf(":")+1)));
			}
			else if(attribute.contains("Y")){
				brick.setY(Double.valueOf(attribute.substring(attribute.indexOf(":")+1)));
			}
		}
		if(brick.getPoints() > -1) board.addBrick(brick);
	}
	
	public static final double DEFAULT_WIDTH = 400;
	public static final double DEFAULT_HEIGHT = 500;
	public static final double DEFAULT_PADDLE_WIDTH = 75;
	public static final double DEFAULT_PADDLE_HEIGHT = 15;
	public static final double DEFAULT_PADDLE_Y_OFFSET = 30;
	public static final double DEFAULT_X_SEP = 10;
	public static final double DEFAULT_Y_SEP = 10;
	public static final double DEFAULT_BRICK_WIDTH = 50;
	public static final double DEFAULT_BRICK_HEIGHT = 10;
	public static final int DEFAULT_NBRICK_SPEEDUP = 10;
	public static final int DEFAULT_PAUSE_TIME = 50;
	public static final double DEFAULT_BALL_RADIUS = 12;
	public static final double DEFAULT_MAX_PADDLE_Y_OFFSET = 0;
	public static final double DEFAULT_MIN_PADDLE_Y_OFFSET = DEFAULT_HEIGHT/2;
	public static final String FILE_NAME = "C:/Users/tjreed/workspace/BrickBreaker/Levels/Level";
	
	public static final double DEFAULT_LABEL_WIDTH = 150;
	public static final double DEFAULT_LABEL_HEIGHT = 150;
}
