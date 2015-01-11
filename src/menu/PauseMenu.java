package menu;

import java.awt.event.MouseEvent;
import java.util.LinkedList;
import basics.Board;
import basics.View;
import acm.program.GraphicsProgram;

public class PauseMenu extends GraphicsProgram{

	private static final long serialVersionUID = 1L;
	private View view;
	private LinkedList<PauseMenuOption> options;
	private Board delegate;

	public PauseMenu(int x, int y, int width, int height){
		setBounds(x, y, width, height);
		view = new View(getWidth(), getHeight());
		add(view);
		options = new LinkedList<PauseMenuOption>();
	}
	
	public void setdelegate(Board delegate){
		this.delegate = delegate;
	}
	
	public void addOption(String title, PauseSelection selection){
		PauseMenuOption toAdd = new PauseMenuOption(0, OPTION_HEIGHT*options.size(), getWidth(), OPTION_HEIGHT);
		toAdd.setValue(title, selection);
		toAdd.addTo(view);
		options.add(toAdd);
	}
	
	public void run(){
		addMouseListeners();
	}
	
	public void mousePressed(MouseEvent e){
		for(int i = 0; i<options.size(); i++){
			if(options.get(i).contains(e.getX(), e.getY()))
				selectOption(options.get(i).select());
		}
	}
	
	private void selectOption(PauseSelection value){
		switch(value){
		case QUIT: break;
		case CONTINUE: delegate.setPaused(false); break;
		}
		setVisible(false);
	}
	
	
	public enum PauseSelection{CONTINUE, QUIT}
	
	public static final double OPTION_HEIGHT = 150;

}