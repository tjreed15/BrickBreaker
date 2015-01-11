package creator;

import java.awt.Color;
import java.util.LinkedList;

import creator.TopBarTrait.Type;

import basics.Ball;
import basics.Board;
import basics.BoardItem;
import basics.Brick;
import basics.Paddle;
import basics.View;
import acm.graphics.GCompound;
import acm.graphics.GRect;
import acm.graphics.GRoundRect;

public class TopBar {

	private GCompound closeMe;
	private boolean open;
	private LinkedList<TopBarTrait> traits;
	private View view;
	
	
	public TopBar(double x, double y, double width, double height){
		view = new View(width, height);
		view.setLocation(x, y);
		createCloseMe((width-(width*CLOSE_ME_WIDTH_RATIO))/2, height,
				width*CLOSE_ME_WIDTH_RATIO, height*CLOSE_ME_HEIGHT_RATIO);
		view.add(getFrame());
		open = true;
		traits = new LinkedList<TopBarTrait>();
	}
	
	public void addTo(View backView){
		backView.add(view);
	}
	
	public void newItemSelected(BoardItem item){
		if(item == null) return;
		else if(item.isPaddle()) setPaddleAsDelegate((Paddle) item);
		else if(item.isBall()) setBallAsDelegate((Ball) item);
		else if(item.isBrick()) setBrickAsDelegate((Brick) item);
		else if(item.isBoard()) setBoardAsDelegate((Board) item);
	}
	
	public void openClose(){
		open = !open;
		view.move(0, open? view.getHeight():-view.getHeight());
	}
	
	public boolean contains(double x, double y){
		return view.contains(x, y);
	}
	
	public void mousePressed(double x, double y){
		if(closeMe.contains(x-view.getX(), y-view.getY())){
			openClose();
		}
		else{
			for(int i = 0; i<traits.size(); i++){
				if(traits.get(i).contains(x-view.getX(), y-view.getY())){
					traits.get(i).mousePressed(x-view.getX(), y-view.getY());
				}
			}
		}
	}
	
	
	
	
	
	
	
	
	private void setBoardAsDelegate(Board board){
		while(traits.size()>0){
			traits.get(0).remove();
			traits.remove(0);
		}
		for(int i = 0; i<NBOARD_OPTIONS; i++){
			traits.add(new TopBarTrait((i*view.getWidth()/NBOARD_OPTIONS), 0, view.getWidth()/NBOARD_OPTIONS, view.getHeight()));
			switch(i){
			case 0: traits.get(i).setValueType(Type.WIDTH, "width", board.getWidth()); break;
			case 1: traits.get(i).setValueType(Type.HEIGHT, "height", board.getHeight()); break;
			case 2: traits.get(i).setValueType(Type.X, "X", board.getX()); break;
			case 3: traits.get(i).setValueType(Type.Y, "Y", board.getY()); break;
//			case 4: traits.get(i).setValueType('p', "pauseTime", ); break;
			}
			traits.get(i).addTo(view);
			traits.get(i).setDelegate(board);
		}
	}
		
	private void setPaddleAsDelegate(Paddle paddle){
		while(traits.size()>0){
			traits.get(0).remove();
			traits.remove(0);
		}
		for(int i = 0; i<NPADDLE_OPTIONS; i++){
			traits.add(new TopBarTrait((i*view.getWidth()/NPADDLE_OPTIONS), 0, view.getWidth()/NPADDLE_OPTIONS, view.getHeight()));
			switch(i){
			case 0: traits.get(i).setValueType(Type.WIDTH, "width", paddle.getWidth()); break;
			case 1: traits.get(i).setValueType(Type.HEIGHT, "height", paddle.getHeight()); break;
			case 2: traits.get(i).setValueType(Type.X, "X", paddle.getX()); break;
			case 3: traits.get(i).setValueType(Type.Y, "Y", paddle.getY()); break;
			case 4: traits.get(i).setValueType(Type.MAXY, "maxY", paddle.getMaxY()); break;
			case 5: traits.get(i).setValueType(Type.MINY, "minY", paddle.getMinY()); break;
			case 6: traits.get(i).setValueType(Type.HEALTH, "health", paddle.getHealth()); break;
			}
			traits.get(i).addTo(view);
			traits.get(i).setDelegate(paddle);
		}
	}
	
	private void setBallAsDelegate(Ball ball){
		while(traits.size()>0){
			traits.get(0).remove();
			traits.remove(0);
		}
		for(int i = 0; i<NBALL_OPTIONS; i++){
			traits.add(new TopBarTrait((i*view.getWidth()/NBALL_OPTIONS), 0, view.getWidth()/NBALL_OPTIONS, view.getHeight()));
			switch(i){
			case 0: traits.get(i).setValueType(Type.X, "X", ball.getX()); break;
			case 1: traits.get(i).setValueType(Type.Y, "Y", ball.getY()); break;
			case 2: traits.get(i).setValueType(Type.RADIUS, "radius", ball.getRadius()); break;
			case 3: traits.get(i).setValueType(Type.HEALTH, "health", ball.getHealth()); break;
			case 4: traits.get(i).setValueType(Type.XVEL, "xVel", ball.getXVel()); break;
			case 5: traits.get(i).setValueType(Type.YVEL, "yVel", ball.getYVel()); break;
			}
			traits.get(i).addTo(view);
			traits.get(i).setDelegate(ball);
		}
	}
	
	private void setBrickAsDelegate(Brick brick){
		while(traits.size()>0){
			traits.get(0).remove();
			traits.remove(0);
		}
		for(int i = 0; i<NBRICK_OPTIONS; i++){
			traits.add(new TopBarTrait((i*view.getWidth()/NBRICK_OPTIONS), 0, view.getWidth()/NBRICK_OPTIONS, view.getHeight()));
			switch(i){
			case 0: traits.get(i).setValueType(Type.WIDTH, "width", brick.getWidth()); break;
			case 1: traits.get(i).setValueType(Type.HEIGHT, "height", brick.getHeight()); break;
			case 2: traits.get(i).setValueType(Type.X, "X", brick.getX()); break;
			case 3: traits.get(i).setValueType(Type.Y, "Y", brick.getY()); break;
			case 4: traits.get(i).setValueType(Type.HEALTH, "health", brick.getHealth()); break;
			case 5: traits.get(i).setValueType(Type.POINTS, "points", brick.getPoints()); break;
			}
			traits.get(i).addTo(view);
			traits.get(i).setDelegate(brick);
		}
//		TODO: allow moving brick
	}


	private void createCloseMe(double x, double y, double width, double height){
		closeMe = new GCompound();
		GRoundRect rect = new GRoundRect(x, y-EXTRA_HEIGHT, width, height+EXTRA_HEIGHT);
		closeMe.add(rect);
		view.add(closeMe);
	}
	
	private GCompound getFrame(){
		GCompound toReturn = new GCompound();
		GRect background = new GRect(0, 0, view.getWidth(), view.getHeight());
		background.setFilled(true); background.setFillColor(SEE_THROUGH_COLOR);
		toReturn.add(background);
		for(int i = 0; i<FRAME_WIDTH; i++){
			GRect base = new GRect(i, i, view.getWidth()-(2*i), view.getHeight()-(2*i));
			if(i>0)base.setColor(FRAME_COLOR);
			toReturn.add(base);
		}
		return toReturn;
	}
	
	
	private static final int FRAME_WIDTH = 5;
	private static final Color FRAME_COLOR = Color.white;
	private static final Color SEE_THROUGH_COLOR = new Color(255, 255, 255);
	
	private static final double CLOSE_ME_WIDTH_RATIO = .33;
	private static final double CLOSE_ME_HEIGHT_RATIO = .33;
	private static final double EXTRA_HEIGHT = 5;
	
	private static final int NBOARD_OPTIONS = 4;
	private static final int NPADDLE_OPTIONS = 7;
	private static final int NBALL_OPTIONS = 6;
	private static final int NBRICK_OPTIONS = 6;
	

	
	
	
}
