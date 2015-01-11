package powerUps;

import acm.graphics.GCompound;
import acm.graphics.GRect;
import basics.View;

import java.awt.Color;
import java.util.LinkedList;

public class LabelBar{

	private GCompound frame;
	private LinkedList<Label> labels;
	private View view;
	
	public LabelBar(){
		labels = new LinkedList<Label>();
	}
	
	public void addTo(View view){
		this.view = view;
		frame = getFrame();
		view.add(frame);
	}
	
	public void addLabel(String str, int fadeTime){
		double y = LABEL_HEIGHT + DEFAULT_TOP_LABEL_Y + (labels.size() * (LABEL_HEIGHT+DEFAULT_Y_SPACING));
		Label toAdd = new Label(str, DEFAULT_LABEL_X, y);
		toAdd.addTo(view);
		toAdd.addTo(this);
		toAdd.setFadeTime(fadeTime);
		toAdd.start();
		labels.add(toAdd);
	}
	
	
	public void remove(Label label){
		labels.remove(label);
		redoSpacing();
	}
	
	public void remove(){
		view.remove(frame);
		for(int i = 0; i<labels.size(); i++){
			labels.remove();
		}
	}
	
	public void updateLabel(String contains, String newValue){
		for(int i = 0; i<labels.size(); i++){
			if(labels.get(i).contains(contains)){
				labels.get(i).changeLabel(newValue);
			}
		}
	}
	
	
	private void redoSpacing(){
		for(int i = 0; (i<labels.size()); i++){
			double y = LABEL_HEIGHT + DEFAULT_TOP_LABEL_Y + (i * (LABEL_HEIGHT+DEFAULT_Y_SPACING));
			labels.get(i).setY(y);
		}
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
	
	private static final double DEFAULT_LABEL_X = 10;
	private static final double DEFAULT_TOP_LABEL_Y = 10;
	private static final double LABEL_HEIGHT = 16;
	private static final double DEFAULT_Y_SPACING = 5;
	public static final int DEFAULT_FADE_TIME = 5000;
	
	private static final int FRAME_WIDTH = 5;
	private static final Color FRAME_COLOR = Color.white;
	private static final Color SEE_THROUGH_COLOR = new Color(255, 255, 255, 100);
}
