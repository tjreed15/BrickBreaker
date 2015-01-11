package menu;

import java.awt.Color;

import acm.graphics.GCompound;
import acm.graphics.GLabel;
import acm.graphics.GRect;
import basics.View;

public class MainMenuOption {

	private View view, backView;
	private int value;
	private GCompound perim;
	private GLabel label;
	
	public MainMenuOption(double x, double y, double width, double height){
		view = new View(width, height);
		view.setLocation(x, y);
		addBackground();
	}
	
	public void addTo(View backView){
		this.backView = backView;
		backView.add(view);
	}
	
	public void remove(){
		backView.remove(view);
	}
	
	public void setValue(int value){
		this.value = value;
		addLabel();
	}
	
	public int getValue(){
		return value;
	}
	
	public boolean contains(double x, double y){
		return view.contains(x, y);
	}
	
	private void addBackground(){
		perim = new GCompound();
		GRect background = new GRect(0, 0, view.getWidth(), view.getHeight());
		background.setFilled(true); background.setColor(SEE_THROUGH_COLOR);
		perim.add(background);
		view.add(perim);
		for(int i = 0; i<FRAME_WIDTH; i++){
			GRect base = new GRect(background.getX()+i, background.getY()+i, 
					background.getWidth()-(2*i), background.getHeight()-(2*i));
			if(i>0)base.setColor(FRAME_COLOR);
			perim.add(base);
		}
		addLabel();
	}
	
	private void addLabel(){
		try{view.remove(label);} catch(Exception e){}
		view.add(label = new GLabel("Level " + value), (view.getWidth()-label.getWidth())/2, (view.getHeight()-label.getHeight())/2);
	}
	
	private static final int FRAME_WIDTH = 5;
	private static final Color FRAME_COLOR = Color.red;
	private static final Color SEE_THROUGH_COLOR = new Color(255, 255, 255, 100);
	
}
