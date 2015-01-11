package basics;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import menu.MainMenu;
import acm.graphics.GImage;
import acm.graphics.GPoint;
import acm.program.GraphicsProgram;



/**
 * Will be used as the main program for the game.
 * Initializes the board, then passes mouse and key events to the board
 */
public class Main extends GraphicsProgram{
	
	private static final long serialVersionUID = 1L;

	private static int STARTING_LEVEL = 0;
	private static final int NLEVELS = 6;
	
	private View view;
	private Board board;
	private MainMenu menu;
	
	// Main Method: When it exits this, the game is over
	public void run(){
		waitForClick();
		FileToBoard ftb = initGame();
		while(true){
			menu = new MainMenu(0, 0, getWidth(), getHeight());
			menu.setMaxValue(NLEVELS-1);
			menu.addTo(view);
			while(menu.levelSelected() == 0); //wait until user selects a level
			int startingLevel = (STARTING_LEVEL == 0)? menu.levelSelected():STARTING_LEVEL;
			menu.remove(); menu = null;
			waitForClick(); //Responds to the click to choose the level
			for(int i = startingLevel; i<NLEVELS; i++){
				board = ftb.getBoard(i);
				board.addTo(view);
				playGame();
				endLevel();
			}
			endGame();
		}
	}
	
	
	// Passes all mouse movements on to the board
	public void mouseMoved(MouseEvent e){
		try{
			board.movePaddles(e.getX()-last.getX(), e.getY()-last.getY());
		}
		catch(Exception a){}
		last = new GPoint(e.getX(), e.getY());
	}
	
	// Passes all key events on to the board
	public void keyPressed(KeyEvent e){
		try{
			if(e.getKeyCode() == KeyEvent.VK_LEFT) board.leftArrow();
			else if(e.getKeyCode() == KeyEvent.VK_RIGHT) board.rightArrow();
			else if(e.getKeyCode() == KeyEvent.VK_UP) board.upArrow();
			else if(e.getKeyCode() == KeyEvent.VK_DOWN) board.downArrow();
			else if(e.getKeyCode() == KeyEvent.VK_SPACE) board.setPaused(true);
			
		}
		catch(Exception a){}
	}
	
	public void mousePressed(MouseEvent e){
		try{
			if(menu != null) menu.mousePressed(e.getX(), e.getY());
			board.mousePressed(e.getX(), e.getY());
		}
		catch(Exception a){}
		last = new GPoint(e.getX(), e.getY());
	}
	
	// Initializes basic variables, returns the FileToBoard that will construct the board
	private FileToBoard initGame(){
		add(BACKGROUND_IMAGE);
		view = new View(getWidth(), getHeight());
		add(view);
		addKeyListeners();
		addMouseListeners();
		return new FileToBoard();
	}
	
	
	
	
	// Allows User to click to start, then plays game
	private boolean playGame(){
		waitForClick();
		board.startGame();
		while(board.gameOn()){
			board.moveObjects();
			pause(Board.PAUSE_TIME);
			while(board.isPaused());
		}
		if (board.victory()){
			System.out.println("Won");
			return true;
		}
		else{
			System.out.println("Lost");
			return false;
		}
	}
	
	
	// Clears board when level ends
	private void endLevel(){
		board.removeAll();
		view.removeAll();
	}
	
	
	// Clears screen when game is over
	private void endGame(){
		view.removeAll();
	}
	
	private GPoint last;
	private static final GImage BACKGROUND_IMAGE = new GImage("C:/Users/tjreed/workspace/BrickBreaker/Pictures/BrickGrassBackground.jpg");
}
