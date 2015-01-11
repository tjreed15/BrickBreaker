package creator;

import java.awt.Color;
import java.util.LinkedList;
import basics.Ball;
import basics.BoardItem;
import basics.Brick;
import basics.FileToBoard;
import basics.Paddle;
import basics.View;
import acm.graphics.GCompound;
import acm.graphics.GRect;
import acm.graphics.GRoundRect;

public class SideBar {

	private GCompound closeMe;
	private boolean open;
	private LinkedList<SideBarOption> options;
	private GraphicalDesign delegate;
	private View view;
	
	
	public SideBar(double x, double y, double width, double height){
		view = new View(width, height);
		view.setLocation(x, y);
		createCloseMe(-(width*CLOSE_ME_WIDTH_RATIO), (height-(height*CLOSE_ME_HEIGHT_RATIO))/2,
				width*CLOSE_ME_WIDTH_RATIO, height*CLOSE_ME_HEIGHT_RATIO);
		view.add(getFrame());
		open = true;
		options = new LinkedList<SideBarOption>();
			//test
			options.add(new SideBarOption(0, 0, width, height/5, new Paddle(0, 0, FileToBoard.DEFAULT_PADDLE_WIDTH, FileToBoard.DEFAULT_PADDLE_HEIGHT)));
			options.add(new SideBarOption(0, (1*height/5), width, height/5, new Brick(0, 0, FileToBoard.DEFAULT_BRICK_WIDTH, FileToBoard.DEFAULT_BRICK_HEIGHT)));
			options.add(new SideBarOption(0, (2*height/5), width, height/5, new Ball(0, 0, FileToBoard.DEFAULT_BALL_RADIUS)));
			for(int i = 0; i<options.size(); i++){
				options.get(i).addTo(view);
			}
	}
	
	public void addTo(View backView){
		backView.add(view);
	}
	
	public void setDelegate(GraphicalDesign delegate){
		this.delegate = delegate;
	}
	
	public void openClose(){
		open = !open;
		view.move(open? -view.getWidth():view.getWidth(), 0);
	}
	
	public boolean contains(double x, double y){
		return view.contains(x, y);
	}
	
	public void mousePressed(double x, double y){
		if(closeMe.contains(x-view.getX(), y-view.getY())){
			openClose();
		}
		else{
			for(int i = 0; i<options.size(); i++){
				if(options.get(i).contains(x-view.getX(), y-view.getY())){
					BoardItem selected = options.get(i).select();
					selected.setLocation(x, y);
					delegate.setItem(selected);
					break;
				}
			}
		}
	}
	
	
	
	
	
	
	
	
	
	private void createCloseMe(double x, double y, double width, double height){
		closeMe = new GCompound();
		GRoundRect rect = new GRoundRect(x, y, width+EXTRA_WIDTH, height);
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
	private static final double EXTRA_WIDTH = 5;
	

	
	
	
}
