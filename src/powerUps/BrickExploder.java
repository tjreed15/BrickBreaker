package powerUps;

import java.awt.Color;
import acm.graphics.GObject;
import acm.graphics.GRect;
import acm.graphics.GRoundRect;
import basics.View;

public class BrickExploder extends Thread{

	private GRect[][] pieces;
	private View view;
	private PowerUpDelegate powerUps;
	
	public BrickExploder(GRect brick, PowerUpDelegate powerUps){
		this.powerUps = powerUps;
		pieces = new GRect[NCOLS][NROWS];
		double brickWidth = brick.getWidth()/NCOLS;
		double brickHeight = brick.getHeight()/NROWS;
		for(int col = 0; col<NCOLS; col++){
			for(int row = 0; row<NROWS; row++){
				pieces[col][row] = new GRect(brick.getX()+(col*brickWidth), brick.getY()+(row*brickHeight), 
						brickWidth, brickHeight);
				pieces[col][row].setFilled(true); pieces[col][row].setFillColor(brick.getFillColor());
			}
		}
	}
	
	// "Adding to view" will only give it a reference to other object in that view... its real view is decided by PowerUpDel.
	public void addTo(View view){
		this.view = view;
		for(int col = 0; col<NCOLS; col++){
			for(int row = 0; row<NROWS; row++){
					powerUps.addExplosionPiece(pieces[col][row]);
			}
		}
	}
	
	public void run(){
		for(int n = 0; n<25; n++){
			for(int col = 0; col<NCOLS; col++){
				double xVel = col-((NCOLS-1)/2.0);
				for(int row = 0; row<NROWS; row++){
					double yVel = row-((NROWS-1)/2.0);
					pieces[col][row].move(xVel, yVel);
					fade(col, row, n);
					if(powerUps.brickExplosionShouldCollide()){
						hitObstacles(pieces[col][row]);
					}
				}
			}
			try {Thread.sleep(PAUSE_TIME);} catch (InterruptedException e){e.printStackTrace();}
		}
		for(int col = 0; col<NCOLS; col++){
			for(int row = 0; row<NROWS; row++){
				powerUps.removeExplosionPiece(pieces[col][row]);
			}
		}
	}
	
	
	
	private void fade(int col, int row, int step){
		Color fill = pieces[col][row].getFillColor();
		pieces[col][row].setFillColor(new Color(fill.getRed(), fill.getGreen(), fill.getBlue(), (int) (255-(step*FADE_AMT))));
	}
	
	private void hitObstacles(GRect rect){
		try{
			double rectX = rect.getX(); double rectY = rect.getY();
			GObject hit = (view.getElementAt(rectX-1, rectY-1));
			if(hit != null && hit.getClass() == BRICK.getClass()){ powerUps.explosionCollide((GRoundRect) hit);  return;}
			else hit = (view.getElementAt(rectX+rect.getWidth()+1, rectY-1));
			if(hit != null && hit.getClass() == BRICK.getClass()){powerUps.explosionCollide((GRoundRect) hit);  return;}
			else hit = (view.getElementAt(rectX-1, rectY+rect.getHeight()+1));
			if(hit != null && hit.getClass() == BRICK.getClass()){powerUps.explosionCollide((GRoundRect) hit);  return;}
			else hit = (view.getElementAt(rectX+rect.getWidth()+1, rectY+rect.getHeight()+1));
			if(hit != null && hit.getClass() == BRICK.getClass()){powerUps.explosionCollide((GRoundRect) hit);  return;}
		}
		catch(Error a){}
		
	}
	
	
	private static final int NCOLS = 2;
	private static final int NROWS= 2;
	private static final int NSTEPS = 25;
	private static final double FADE_AMT = 255.0/NSTEPS;
	private static final int PAUSE_TIME = 50;
	
	private static final GRoundRect BRICK = new GRoundRect(0,0);
	
}
