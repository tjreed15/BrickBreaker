package creator;

import basics.BoardItem;
import basics.View;
import acm.graphics.GLabel;
import acm.graphics.GRect;

public class TopBarTrait {
	
	private GLabel label, valueLabel;
	private GRect box;
	private View backView, view;
	private Button upButton, downButton;
	private double value;
	private Type type;
	private BoardItem delegate;
	
	public TopBarTrait(double x, double y, double width, double height){
		view = new View(width, height);
		view.setLocation(x, y);
		box = new GRect(width*BOX_WIDTH_RATIO, height*BOX_HEIGHT_RATIO);
		view.add(box, width*BOX_X_OFFSET_RATIO, (height-box.getHeight())*BOX_Y_LACATION_RATIO);
		label = new GLabel("Label");
		view.add(label, box.getX()+box.getWidth()-label.getWidth(), box.getY()+box.getHeight()+label.getHeight());
		value = 0;
		valueLabel = new GLabel(""+value);
		view.add(valueLabel, box.getX()+box.getWidth()-valueLabel.getWidth()-VALUE_LABEL_X_OFFSET_RATIO*box.getWidth(), 
				box.getY()+((box.getHeight()-valueLabel.getHeight())/2)+valueLabel.getHeight());
		addButtons();
	}
	
	public void addTo(View backView){
		this.backView = backView;
		backView.add(this.view);
	}
	
	public void remove(){
		backView.remove(view);
	}
	
	public void setValueType(Type type, String str, double value){
		label.setLabel(str);
		label.setLocation(box.getX()+box.getWidth()-label.getWidth(), box.getY()+box.getHeight()+label.getHeight());
		this.type = type;
		setValue(value);
	}
	
	public void setDelegate(BoardItem delegate){
		this.delegate = delegate;
	}
	
	public boolean contains(double x, double y){
		return view.contains(x, y);
	}
	
	public void mousePressed(double x, double y){
		if(upButton.contains(x-view.getX(), y-view.getY())) upButton.mousePressed(x, y);
		if(downButton.contains(x-view.getX(), y-view.getY())) downButton.mousePressed(x, y);
		//TODO: allow user to type value into bar directly
	}
	
	
	
	
	//Value holds 1 or -1 to go up or down, this holds info on item and trait to change
	public void buttonPressed(double value){
		switch(type){
		case X: addX(value); break;
		case Y: addY(value); break;
		case WIDTH: addWidth(value); break;
		case HEIGHT: addHeight(value); break;
		case HEALTH: addHealth((int) value); break;
		case POINTS: addPoints((int) value); break;
		case MAXY: addMaxY(value); break;
		case MINY: addMinY(value); break;
		case RADIUS: addRadius(value); break;
		case XVEL: addXVel(value); break;
		case YVEL: addYVel(value); break;
		case PAUSE_TIME: addPauseTime((int) value); break;
		}
	}
	
	
	
	
	
	
	
	
	private void addButtons(){
		upButton = new Button(BUTTON_WIDTH_RATIO*view.getWidth(), BUTTON_HEIGHT_RATIO*view.getHeight());
		upButton.setLocation(box.getX()+box.getWidth()+((view.getWidth()-(box.getX()+box.getWidth()))-upButton.getWidth())/2,
				(view.getHeight()-upButton.getHeight())/4);
		upButton.addTo(view);
		upButton.setDelegate(this);
		upButton.setValue(1);
		
		downButton = new Button(BUTTON_WIDTH_RATIO*view.getWidth(), BUTTON_HEIGHT_RATIO*view.getHeight());
		downButton.setLocation(box.getX()+box.getWidth()+((view.getWidth()-(box.getX()+box.getWidth()))-upButton.getWidth())/2,
				3*(view.getHeight()-upButton.getHeight())/4);
		downButton.addTo(view);
		downButton.setDelegate(this);
		downButton.setValue(-1);
	}
	
	
	
	
	
	
	
	
	private void addX(double value){
		delegate.setLocation(delegate.getX()+value, delegate.getY());
		setValue(delegate.getX());
	}
	
	private void addY(double value){
		delegate.setLocation(delegate.getX(), delegate.getY()+value);
		setValue(delegate.getY());
	}
	
	private void addWidth(double value){
		delegate.setSize(delegate.getWidth()+value, delegate.getHeight());
		setValue(delegate.getWidth());
	}
	
	private void addHeight(double value){
		delegate.setSize(delegate.getWidth(), delegate.getHeight()+value);
		setValue(delegate.getHeight());
	}
	
	private void addHealth(int value){
		delegate.setHealth(delegate.getHealth()+value);
		setValue(delegate.getHealth());
	}
	
	private void addPoints(int value){
		delegate.setPoints(delegate.getPoints()+value);
		setValue(delegate.getPoints());
	}
	
	private void addMaxY(double value){
		delegate.setMaxY(delegate.getMaxY()+value);
		setValue(delegate.getMaxY());
	}
	
	private void addMinY(double value){
		delegate.setMaxY(delegate.getMinY()+value);
		setValue(delegate.getMinY());
	}
	
	private void addRadius(double value){
		delegate.setRadius(delegate.getRadius()+value);
		setValue(delegate.getRadius());
	}
	
	private void addXVel(double value){
		delegate.setXVel(delegate.getXVel()+value);
		setValue(delegate.getXVel());
	}
	
	private void addYVel(double value){
		delegate.setYVel(delegate.getYVel()+value);
		setValue(delegate.getYVel());
	}
	
	private void addPauseTime(int value){
		delegate.setPauseTime(delegate.getPauseTime()+value);
		setValue(delegate.getPauseTime());
	}
	
	
	
	
	private void setValue(double value){
		this.value = value;
		valueLabel.setLabel(""+value);
		valueLabel.setLocation(box.getX()+box.getWidth()-valueLabel.getWidth()-VALUE_LABEL_X_OFFSET_RATIO*box.getWidth(), 
				box.getY()+((box.getHeight()-valueLabel.getHeight())/2)+valueLabel.getHeight());
	}
	
	private static final double BOX_WIDTH_RATIO = .5;
	private static final double BOX_HEIGHT_RATIO = .5;
	private static final double BOX_Y_LACATION_RATIO = .25;
	private static final double BOX_X_OFFSET_RATIO = .1;
	private static final double VALUE_LABEL_X_OFFSET_RATIO = .05;
	
	private static final double BUTTON_WIDTH_RATIO = .2;
	private static final double BUTTON_HEIGHT_RATIO = .25;
	
	
	public enum Type {X, Y, WIDTH, HEIGHT, HEALTH, POINTS, PAUSE_TIME, MAXY, MINY, RADIUS, XVEL, YVEL} 
	
	
	
}
