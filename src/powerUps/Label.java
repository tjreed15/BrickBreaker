package powerUps;

import java.awt.Color;
import java.awt.Font;

import basics.View;
import acm.graphics.GLabel;

public class Label extends Thread{
	
	private GLabel label;
	private View view;
	private int time;
	private int fade;
	private LabelBar labelBar;
	
	public Label(String str, double x, double y){
		label = new GLabel(str, x, y);
		label.setFont(new Font("Serif", Font.BOLD, 18));
		fade = 255;
		labelBar = null;
	}
	
	public void addTo(View view){
		this.view = view;
		view.add(label);
	}
	
	public void addTo(LabelBar labelBar){
		this.labelBar = labelBar;
	}
	
	public void setFadeTime(int time){
		this.time = time;
	}

	public void run(){
		if(time<0) return;
		try{Thread.sleep(time);}
		catch(Exception e){e.printStackTrace(); System.exit(1);}
		while(fade>0){
			label.setColor(new Color(0, 0, 0, fade));
			try{Thread.sleep(PAUSE_TIME);} catch(Exception e){e.printStackTrace(); System.exit(1);}
			fade -= FADE_AMOUNT;
		}
		if(labelBar != null) labelBar.remove(this);
		view.remove(label);
	}
	
	public void setY(double y){
		label.setLocation(label.getX(), y);
	}
	
	public boolean contains(String str){
		return label.getLabel().contains(str);
	}
	
	public void changeLabel(String newValue){
		label.setLabel(newValue);
	}
	
	private static final int PAUSE_TIME = 50;
	private static final int FADE_AMOUNT = 15;
	
}
