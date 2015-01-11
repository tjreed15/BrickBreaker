package basics;

import java.util.LinkedList;
import acm.graphics.GCompound;
import acm.graphics.GObject;

public class View extends GCompound{

	private double width, height;
	private LinkedList<GObject> graphicsOnly;
	
	public View(double width, double height){
		this.width = width;
		this.height = height;
		graphicsOnly = new LinkedList<GObject>();
	}
	
	public double getWidth(){
		return width;
	}
	
	public void setWidth(double width){
		this.width = width;
	}
	
	public double getHeight(){
		return height;
	}
	
	public void setHeight(double height){
		this.height = height;
	}
	
	public void setX(double x){
		setLocation(x, getY());
	}
	
	public void setY(double y){
		setLocation(getX(), y);
	}

	
	public void setBounds(double x, double y, double width, double height){
		setLocation(x, y);
		this.width = width;
		this.height = height;
//		for(int i = 0; i<super.getElementCount(); i++){
//			GObject obj = super.getElement(i);
//			if(((obj.getX()+obj.getWidth())>this.width) || ((obj.getY()+obj.getHeight())>this.height))
//				obj.setVisible(false);
//			else obj.setVisible(true);
//		}
	}
	
	public boolean contains(double x, double y){
		return super.contains(x, y) || (x>super.getX() && y>super.getY() && x<(super.getX()+width) && y<(super.getY()+height));
	}
	
	//Makes it so this is not able to be removed/found by getElement)
	public void addGraphicsElement(GObject obj){
		add(obj);
		graphicsOnly.add(obj);
		if(((obj.getX()+obj.getWidth())>this.width) || ((obj.getY()+obj.getHeight())>this.height))
			obj.setVisible(false);
		else obj.setVisible(true);
	}
	
	public GObject getElementAt(double x, double y){
		GObject toReturn = super.getElementAt(x, y);
		for(int i = 0; i<graphicsOnly.size(); i++){
			if(graphicsOnly.get(i) == toReturn) return null;
		}
		return toReturn;
	}
	
	public void objectMoved(GObject obj){
		if(((obj.getX()+obj.getWidth())>this.width) || ((obj.getY()+obj.getHeight())>this.height))
			obj.setVisible(false);
		else obj.setVisible(true);
	}

	public void setSize(double width, double height) {
		this.width = width;
		this.height = height;
	}
	
}
