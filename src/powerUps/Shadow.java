package powerUps;

import java.awt.Color;

import basics.View;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.graphics.GResizable;
import acm.graphics.GRoundRect;

public class Shadow extends Thread{
	
	private GObject shadow;
	private GObject parent;
	private View view;
	
	public Shadow(GObject parent){
		this.parent = parent;
		if(parent.getClass() == GOVAL.getClass()){
			shadow = new GOval(parent.getX()+SHADOW_X_OFFSET, parent.getY()+SHADOW_Y_OFFSET, parent.getWidth(), parent.getHeight());
			((GOval) shadow).setFilled(true);
			shadow.setColor(SHADOW_COLOR);
		}
		else if(parent.getClass() == GRECT.getClass()){
			shadow = new GRect(parent.getX()+SHADOW_X_OFFSET, parent.getY()+SHADOW_Y_OFFSET, parent.getWidth(), parent.getHeight());
			((GRect) shadow).setFilled(true);
			shadow.setColor(SHADOW_COLOR);
		}
		else if(parent.getClass() == GROUNDRECT.getClass()){
			shadow = new GRoundRect(parent.getX()+SHADOW_X_OFFSET, parent.getY()+SHADOW_Y_OFFSET, parent.getWidth(), parent.getHeight());
			((GRect) shadow).setFilled(true);
			shadow.setColor(SHADOW_COLOR);
		}
	}
	
	public void addPowerUpDelegate(PowerUpDelegate powerUps){
		view = powerUps.addShadow(shadow);
	}
	
	public void run(){
		while(true){
			shadow.setLocation(parent.getX()+SHADOW_X_OFFSET, parent.getY()+SHADOW_Y_OFFSET);
			if(shadow.getX()+shadow.getWidth()>=view.getWidth()) 
				((GResizable) shadow).setSize(view.getWidth()-shadow.getX(), shadow.getHeight());
			else ((GResizable) shadow).setSize(parent.getWidth(), shadow.getHeight());
			if(shadow.getY()+shadow.getHeight()>=view.getHeight()) 
				((GResizable) shadow).setSize(shadow.getWidth(), view.getHeight()-shadow.getY());
			else ((GResizable) shadow).setSize(shadow.getWidth(), parent.getHeight());
		}
	}
	
	public void addName(String name){
		System.out.println(name);
	}

	public void remove(){
		//this.stop();
		view.remove(shadow);
	}
	
	private static final int SHADOW_X_OFFSET = 5;
	private static final int SHADOW_Y_OFFSET = 3;
	private static final Color SHADOW_COLOR = new Color(0, 0, 0, 200);
	

	public static final GRect GRECT = new GRect(0,0);
	public static final GOval GOVAL = new GOval(0,0);
	public static final GRoundRect GROUNDRECT = new GRoundRect(0,0);
}
