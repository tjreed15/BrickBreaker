package creator;

import java.awt.Color;
import acm.graphics.GCompound;
import acm.graphics.GRect;
import basics.Ball;
import basics.BoardItem;
import basics.Brick;
import basics.Paddle;
import basics.View;

public class SideBarOption {
	
	private View option;
	private BoardItem item;
	private View view;
	
	public SideBarOption(double x, double y, double width, double height, BoardItem item){
		option = new View(width, height);
		option.setLocation(x, y);
		this.item = item;
	}
	
	
	public void addTo(View view){
		this.view = view;
		view.add(testOption());
	}
	
	public boolean contains(double x, double y){
		return option.contains(x, y);
	}
	
	public double getX(){
		return option.getX();
	}
	public double getY(){
		return option.getY();
	}
	
	public BoardItem select(){
		BoardItem toReturn = null;
		if(item.isPaddle()){
			toReturn = new Paddle(item.getX(), item.getY(), item.getWidth(), item.getHeight());
			((Paddle)(toReturn)).setDefaultY(item.getY());
		}
		else if(item.isBall()){
			toReturn = new Ball(item.getX(), item.getY(), item.getRadius());
		}
		else if(item.isBrick()){
			toReturn = new Brick(item.getX(), item.getY(), item.getWidth(), item.getHeight());
		}
		// TODO: board
		return toReturn;
	}
	
	public void remove(){
		view.remove(option);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	private GCompound testOption(){
		GCompound toReturn = new GCompound();
		GRect frame = new GRect(option.getX(), option.getY(), option.getWidth(), option.getHeight());
		frame.setFilled(true);
		frame.setColor(Color.red);
		frame.setFillColor(Color.black);
		toReturn.add(frame);
		return toReturn;
	}

}
