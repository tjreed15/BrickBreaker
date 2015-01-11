package creator;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import basics.Ball;
import basics.Board;
import basics.BoardItem;
import basics.Brick;
import basics.FileToBoard;
import basics.Paddle;
import basics.View;
import acm.graphics.GImage;
import acm.graphics.GPoint;
import acm.program.GraphicsProgram;

public class GraphicalDesign extends GraphicsProgram{
	
	private static final long serialVersionUID = 1L;
	private Board board;
	private View view;
	private GImage cursor;
	private int resize;
	private BoardItem itemSelected;
	private SideBar sideBar;
	private TopBar topBar;
	private File file;
	
	public GraphicalDesign(){}
	
	public GraphicalDesign(Board board){
		this.board = board;
	}
	
	public void run(){
		waitForClick();
		view = new View(getWidth(), getHeight());
		add(view);
		if(board == null) this.board = new Board(-1, -1, FileToBoard.DEFAULT_WIDTH, FileToBoard.DEFAULT_HEIGHT);
		board.addTo(view);
		sideBar = new SideBar(getWidth()-SIDEBAR_WIDTH, 0, SIDEBAR_WIDTH, getHeight()-1);
		sideBar.addTo(view);
		sideBar.setDelegate(this);
		topBar = new TopBar(-1, -1, getWidth()-SIDEBAR_WIDTH, TOPBAR_HEIGHT);
		topBar.addTo(view);
		topBar.newItemSelected(board);
		addMouseListeners();
		addKeyListeners();
	}
	
	public void setFile(File file){
		this.file = file;
	}
	
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			save();
			System.out.println("Saved");
		}
	}
	
	public void mouseMoved(MouseEvent e){
		if(Math.abs(e.getX()-board.getX())<CLOSE_TO && Math.abs(e.getY()-board.getY())<CLOSE_TO){
			setCursor(NW_RESIZE_CURSOR);
		}
		else if(Math.abs(e.getX()-(board.getX()+board.getWidth()))<CLOSE_TO && Math.abs(e.getY()-board.getY())<CLOSE_TO){
			setCursor(NE_RESIZE_CURSOR);
		}
		else if(Math.abs(e.getX()-(board.getX()+board.getWidth()))<CLOSE_TO && Math.abs(e.getY()-(board.getY()+board.getHeight()))<CLOSE_TO){
			setCursor(SE_RESIZE_CURSOR);
		}
		else if(Math.abs(e.getX()-board.getX())<CLOSE_TO && Math.abs(e.getY()-(board.getY()+board.getHeight()))<CLOSE_TO){
			setCursor(SW_RESIZE_CURSOR);
		}
		else setCursor(0);
		if(cursor != null) cursor.setLocation(e.getX()-cursor.getWidth(), e.getY()-cursor.getHeight());
		last = new GPoint(e.getX(), e.getY());
	}
	
	public void mouseClicked(MouseEvent e){
		
	}
	
	public void mousePressed(MouseEvent e){
		if(sideBar.contains(e.getX()-view.getX(), e.getY()-view.getY()))
			sideBar.mousePressed(e.getX()-view.getX(), e.getY()-view.getY());
		else if(topBar.contains(e.getX()-view.getX(), e.getY()-view.getY())) 
			topBar.mousePressed(e.getX()-view.getX(), e.getY()-view.getY());
		else if(board.contains(e.getX(), e.getY())){
			itemSelected = board.itemAt(e.getX()-board.getX(), e.getY()-board.getY());
			if(itemSelected == null) itemSelected = board;
			else{
				itemSelected.remove();
				itemSelected.setLocation(itemSelected.getX()+board.getX(), itemSelected.getY()+board.getY());
				itemSelected.addTo(view); 
				if(itemSelected.isBall())board.removeBall((Ball) itemSelected);
				else if(itemSelected.isPaddle()) board.removePaddle((Paddle) itemSelected);
				else if(itemSelected.isBrick()) board.removeBrick((Brick) itemSelected);
			}
			topBar.newItemSelected(itemSelected);
		}
		else if(Math.abs(e.getX()-board.getX())<CLOSE_TO && Math.abs(e.getY()-board.getY())<CLOSE_TO){
			resize = NW_RESIZE_CURSOR;
		}
		else if(Math.abs(e.getX()-(board.getX()+board.getWidth()))<CLOSE_TO && Math.abs(e.getY()-board.getY())<CLOSE_TO){
			resize = NE_RESIZE_CURSOR;
		}
		else if(Math.abs(e.getX()-(board.getX()+board.getWidth()))<CLOSE_TO && Math.abs(e.getY()-(board.getY()+board.getHeight()))<CLOSE_TO){
			resize = SE_RESIZE_CURSOR;
		}
		else if(Math.abs(e.getX()-board.getX())<CLOSE_TO && Math.abs(e.getY()-(board.getY()+board.getHeight()))<CLOSE_TO){
			resize = SW_RESIZE_CURSOR;
		}
		else resize = 0;
		last = new GPoint(e.getX(), e.getY());
	}
	
	public void mouseDragged(MouseEvent e){
		if(itemSelected != null) itemSelected.move(e.getX()-last.getX(), e.getY()-last.getY());
		else switch(resize){
		case NW_RESIZE_CURSOR: 
			board.setWidth(board.getWidth()-(e.getX()-last.getX())); 
			board.setHeight(board.getHeight()-(e.getY()-last.getY())); 
			board.setX(board.getX()+(e.getX()-last.getX())); 
			board.setY(board.getY()+(e.getY()-last.getY())); 
			break;
		case NE_RESIZE_CURSOR: 
			board.setWidth(board.getWidth()+(e.getX()-last.getX())); 
			board.setHeight(board.getHeight()-(e.getY()-last.getY())); 
			board.setY(board.getY()+(e.getY()-last.getY())); 
			break;
		case SE_RESIZE_CURSOR: 
			board.setWidth(board.getWidth()+(e.getX()-last.getX())); 
			board.setHeight(board.getHeight()+(e.getY()-last.getY())); 
			break;
		case SW_RESIZE_CURSOR: 
			board.setWidth(board.getWidth()-(e.getX()-last.getX())); 
			board.setHeight(board.getHeight()+(e.getY()-last.getY())); 
			board.setX(board.getX()+(e.getX()-last.getX())); 
			break;
		}
		last = new GPoint(e.getX(), e.getY());
	}
	
	public void mouseReleased(MouseEvent e){
		resize = 0;
		if(itemSelected != null && itemSelected.isBoard());
		else if(board.contains(e.getX(), e.getY())){
			addItemToBoard();
		}
		else if(itemSelected != null){
			itemSelected.remove();
		}
		itemSelected = null;
	}
		
	public void save(){
		try{
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.append(board.toString());
			fileWriter.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	
	public void setItem(BoardItem item){
		this.itemSelected = item;
		itemSelected.addTo(view);
		topBar.newItemSelected(itemSelected);
	}
	
	
	
	
	
	
	private void addItemToBoard(){
		if(itemSelected == null) return;
		double x = itemSelected.getX()-board.getX();
		double y =  itemSelected.getY()-board.getY();
		itemSelected.remove();
		itemSelected.setLocation(x, y);
		if(itemSelected.isPaddle()){
			((Paddle)(itemSelected)).setDefaultY(y);
			board.addPaddle((Paddle)itemSelected);
		}
		else if(itemSelected.isBall()){
			board.addBall((Ball)itemSelected);
		}
		else if(itemSelected.isBrick()){
			board.addBrick((Brick)itemSelected);
		}
		else if(itemSelected.isBoard()){
			//TODO: add board
		}
	}
	
	private void setCursor(int type){
		try{view.remove(cursor);}catch(Exception e){}
		switch(type){
		case NW_RESIZE_CURSOR: view.add(cursor = new GImage("C:/Users/tjreed/workspace/BrickBreaker/Pictures/NWCursor.jpg")); break;
		case NE_RESIZE_CURSOR: view.add(cursor = new GImage("C:/Users/tjreed/workspace/BrickBreaker/Pictures/NECursor.jpg")); break;
		case SE_RESIZE_CURSOR: view.add(cursor = new GImage("C:/Users/tjreed/workspace/BrickBreaker/Pictures/NWCursor.jpg")); break;
		case SW_RESIZE_CURSOR: view.add(cursor = new GImage("C:/Users/tjreed/workspace/BrickBreaker/Pictures/NECursor.jpg")); break;
		default: cursor = null;
		}
	}
	
	private static final double CLOSE_TO = 2;
	private static final int NW_RESIZE_CURSOR = 1;
	private static final int NE_RESIZE_CURSOR = 2;
	private static final int SE_RESIZE_CURSOR = 3;
	private static final int SW_RESIZE_CURSOR = 4;
	
	private static final double SIDEBAR_WIDTH = 150;
	private static final double TOPBAR_HEIGHT = 50;
	
	
	private GPoint last;
	
}
