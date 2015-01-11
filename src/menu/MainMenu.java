package menu;

import java.awt.Color;
import java.awt.Font;
import acm.graphics.GCompound;
import acm.graphics.GLabel;
import acm.graphics.GRect;
import basics.View;

public class MainMenu{
	
	private View view, backView;
	private GLabel title;
	private GCompound perim;
	private int levelSelected;
	private MainMenuOption[][] options;
	
	public MainMenu(double x, double y, double width, double height){
		view = new View(width, height);
		view.setLocation(x, y);
		levelSelected = 0;
		setUp();
	}
	
	public void addTo(View backView){
		this.backView = backView;
		addBackground();
		backView.add(view);
	}
	
	public void remove(){
		backView.remove(view);
	}
	
	public void mousePressed(double x, double y){
		for(int row = 0; row<NROWS; row++){
			for(int col = 0; col<NOPTIONS_PER_ROW; col++){
				if(options[row][col].contains(x, y))
					levelSelected = options[row][col].getValue();
			}
		}
	}
	
	public int levelSelected(){
		return levelSelected;
	}
	
	public void setMaxValue(int value){
		for(int row = 0; row<NROWS; row++){
			for(int col = 0; col<NOPTIONS_PER_ROW; col++){
				if(options[row][col].getValue()>value){
					options[row][col].remove();
					options[row][col] = null;
					
				}
			}
		}
	}
	
	
	
	
	private void setUp(){
		//Decide number to fit on screen
		NROWS = (int) ((view.getHeight()-TITLE_DIVIDER)/OPTION_HEIGHT);
		NOPTIONS_PER_ROW = (int) (view.getWidth()/OPTION_WIDTH);
		
		//Resize to fill screen
		OPTION_HEIGHT = (view.getHeight()-TITLE_DIVIDER)/NROWS;
		OPTION_WIDTH = view.getWidth()/NOPTIONS_PER_ROW;
		options = new MainMenuOption[NROWS][NOPTIONS_PER_ROW];
		for(int row = 0; row<NROWS; row++){
			for(int col = 0; col<NOPTIONS_PER_ROW; col++){
				MainMenuOption newOption = options[row][col] = new MainMenuOption(col*OPTION_WIDTH, 
						TITLE_DIVIDER+(row*OPTION_HEIGHT), OPTION_WIDTH, OPTION_HEIGHT);
				newOption.setValue((row*NOPTIONS_PER_ROW)+col+1);
				newOption.addTo(view);
			}
		}
	}
	

	
	
	
	
	
	private void addBackground(){
		try{backView.remove(perim); backView.remove(title);}catch(Exception e){}
		
		title = new GLabel("Main Menu");
		title.setFont(new Font("Serif", Font.BOLD, 18));
		title.setColor(Color.white);
		view.add(title, (view.getWidth()-title.getWidth())/2, TITLE_DIVIDER/2+title.getAscent()/2);
		
		perim = new GCompound();
		GRect background = new GRect(0, TITLE_DIVIDER, 
				view.getWidth(), view.getHeight()-TITLE_DIVIDER);
		background.setFilled(true); background.setColor(SEE_THROUGH_COLOR);
		perim.add(background);
		view.add(perim);
		for(int i = 0; i<FRAME_WIDTH; i++){
			GRect base = new GRect(background.getX()+i, background.getY()+i, 
					background.getWidth()-(2*i), background.getHeight()-(2*i));
			if(i>0)base.setColor(FRAME_COLOR);
			perim.add(base);
		}
		perim.sendToBack();
	}
	
	
	private static final double TITLE_DIVIDER = 50;
	private static final int FRAME_WIDTH = 5;
	private static final Color FRAME_COLOR = Color.red;
	private static final Color SEE_THROUGH_COLOR = new Color(255, 255, 255, 100);
	
	private double OPTION_WIDTH = 100;
	private double OPTION_HEIGHT = 200;
	private int NROWS;
	private int NOPTIONS_PER_ROW;
	
}
