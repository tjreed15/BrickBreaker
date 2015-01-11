package menu;

import menu.PauseMenu.PauseSelection;
import acm.graphics.GLabel;
import basics.View;

public class PauseMenuOption {

	private View view, backView;
	private GLabel label;
	private PauseSelection selection;
	
	public PauseMenuOption(double x, double y, double width, double height){
		view = new View(width, height);
		view.setLocation(x, y);
		updateLabel("");
	}
	
	public void addTo(View backView){
		this.backView = backView;
		backView.add(view);
	}
	
	public void remove(){
		backView.remove(view);
	}
	
	public void setValue(String title, PauseSelection selection){
		this.selection = selection;
		updateLabel(title);
	}
	
	public boolean contains(double x, double y){
		return view.contains(x, y);
	}
	
	public PauseSelection select(){
		return selection;
	}
	
	
	
	private void updateLabel(String title){
		try{view.remove(label);}catch(Exception e){}
		label = new GLabel(title);
		view.add(label, (view.getWidth()-label.getWidth())/2, 
				(view.getHeight()-label.getWidth())/2);
	}
	
}
	
