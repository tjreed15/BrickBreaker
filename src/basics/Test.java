package basics;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Test extends JPanel implements MouseListener{

	private static final long serialVersionUID = 1L;

	public Test(){
        super();
        addMouseListener(this);   
	}

    public void paintComponent(Graphics g){
    	
    }

    public void mouseClicked(MouseEvent mouse){
        System.out.println("clicked");
    }

/* The following methods have to be here to comply
   with the MouseListener interface, but we don't
   use them, so their code blocks are empty. */
    public void mouseEntered(MouseEvent mouse){ }   
    public void mouseExited(MouseEvent mouse){ }
    public void mousePressed(MouseEvent mouse){ }
    public void mouseReleased(MouseEvent mouse){ }

    public static void main(String arg[]){
        JFrame frame = new JFrame("MousePanel");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setAlwaysOnTop(true);
        frame.setSize(500,500);
        int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        frame.setLocation((width-frame.getWidth())/2, (height-frame.getHeight())/2);

        Test panel = new Test();
        frame.setContentPane(panel);
        frame.setVisible(true);
        
        while(true){
        	 if(!frame.isShowing()){
             	System.out.println("NOW!");
             }
        }
    }
}
