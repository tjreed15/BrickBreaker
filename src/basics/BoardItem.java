package basics;

public class BoardItem {

	public void move(double dx, double dy){System.out.println("Move");}
	public void setLocation(double x, double y){System.out.println("Set Location");}
	public void setSize(double width, double height){System.out.println("Set Size");}
	public double getX(){System.out.println("Get X"); return 0;}
	public double getY(){System.out.println("Get Y"); return 0;}
	public double getWidth(){System.out.println("Get Width"); return 0;}
	public double getHeight(){System.out.println("Get Height"); return 0;}
	public void remove(){System.out.println("Remove");}
	public void addTo(View view){System.out.println("Add to");}
	public void setHealth(int health){System.out.println("Set Health");}
	public int getHealth(){System.out.println("Get Health"); return 0;}
	
	public boolean isBrick(){return false;}
	public boolean isMovingBrick(){return false;}
	public boolean isPaddle(){return false;}
	public boolean isBall(){return false;}
	public boolean isBoard(){return false;}
	
	//Brick
	public void setPoints(int points){System.out.println("Set Points");}
	public int getPoints(){System.out.println("Get Points"); return 0;}
	
	//Paddle
	public void setMaxY(double maxY){System.out.println("Set Max");}
	public double getMaxY(){System.out.println("Get Max"); return 0;}
	public void setMinY(double minY){System.out.println("Set Min");}
	public double getMinY(){System.out.println("Get Min"); return 0;}
	
	
	//Ball
	public void setRadius(double radius){System.out.println("Set Radius");}
	public double getRadius(){System.out.println("Get Radius"); return 0;}
	public void setXVel(double xVel){System.out.println("Set xVel");}
	public double getXVel(){System.out.println("Get xVel"); return 0;}
	public void setYVel(double yVel){System.out.println("Set yVel");}
	public double getYVel(){System.out.println("Get yVel"); return 0;}
	
	//Board
	public void setPauseTime(int pauseTime){System.out.println("Set pauseTime");}
	public int getPauseTime(){System.out.println("Get pauseTime"); return 0;}
	
	
}
