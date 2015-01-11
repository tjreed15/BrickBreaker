package creator;

import acm.graphics.GRoundRect;
import basics.View;

public class Button {

	private int value; // +/- 1
	private TopBarTrait delegate;
	private View view, backView;
	
	public Button(double width, double height){
		view = new View(width, height);
		GRoundRect perim = new GRoundRect(0, 0, width, height);
		view.add(perim);
	}
	
	public void setLocation(double x, double y){
		view.setLocation(x, y);
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
	}
	
	public void setDelegate(TopBarTrait delegate){
		this.delegate = delegate;
	}
	
	public boolean contains(double x, double y){
		return view.contains(x, y);
	}
	
	public void mousePressed(double x, double y){
		delegate.buttonPressed(value);
	}
	
	
	public double getWidth(){
		return view.getWidth();
	}
	
	public double getHeight(){
		return view.getHeight();
	}
	
	
}
