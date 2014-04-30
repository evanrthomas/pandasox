package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ClientView extends JPanel{
	 /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private final int WINDOW_WIDTH;
    private final int WINDOW_HEIGHT;
    private JFrame frame;
    
    private final ArrayList<Card> hand;
    private ArrayList<Card> discard;
    private ArrayList<Card> azone;
    private Card[] center;
    private String message;
    private ClickDispatcher cd;
    
    
    private ViewParams params;
    private final int defaultCardHeight = 100, defaultCardWidth = 75;

    public ClientView(ArrayList<Card> hand, ArrayList<Card> discard, 
    		ArrayList<Card> azone, Card[] center) {
    	WINDOW_WIDTH = 1100;
    	WINDOW_HEIGHT = 700;
    	this.hand = hand;
    	this.discard = discard;
    	this.azone = azone;
    	this.center = center;
    	setMessage("");
    	Card.setDefaultWidthHeight(defaultCardWidth, defaultCardHeight);
    	initializeParams();
    }
    
    private void initializeParams() {
    	params = new ViewParams();
    	int height = defaultCardHeight, width = defaultCardWidth;
    	params.DISCARD_X = 900; params.DISCARD_Y = 550;
    	params.DISCARD_WIDTH = width; params.DISCARD_HEIGHT = height;
    	
    	params.AZONE_X = 50; params.AZONE_Y = 550;
    	params.AZONE_WIDTH = width*5; params.AZONE_HEIGHT = height;
    	
    	params.CENTER_X = 400; params.CENTER_Y = 300;
    	params.CENTER_WIDTH = width*3; params.CENTER_HEIGHT = height;
    	
    	params.HAND_X = 200; params.HAND_Y = 400;
    	params.HAND_WIDTH = width*4; params.HAND_HEIGHT = height;
    }

    public void show () {
       	//Create and set up the window.
        frame = new JFrame("ClientView");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
       
        frame.getContentPane().add(this);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        addMouseListener(cd = new ClickDispatcher(this));
    }
    
    public void setMessage(String s) {
    	//System.out.println("set message "+s);
    	message = s;
    	repaint();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        System.out.println("drawing message "+ message);
        g.drawString(message, WINDOW_WIDTH/2, 50);
        drawHand(g);
        drawAZone(g);
        drawDiscard(g);
        drawCenter(g);
    }
    
    public void drawHand(Graphics g) {
    	Rectangle r;
    	for (int i=0; i<hand.size(); i++) {
    		r = new Rectangle(params.HAND_X +i*defaultCardWidth, params.HAND_Y, 
    				defaultCardWidth, defaultCardHeight);
    		hand.get(i).paint(g,r);
    		cd.registerZone(r, hand.get(i));
    	}
    }
    
    public void drawAZone(Graphics g) {
    	Rectangle r;
    	for (int i=0; i<azone.size(); i++) {
    		r = new Rectangle(params.AZONE_X + i*defaultCardWidth, params.AZONE_Y, 
    				params.AZONE_WIDTH, params.AZONE_HEIGHT);
    		azone.get(i).paint(g, r);
    		cd.registerZone(r, azone.get(i));
    	}
    }
    
    public void drawDiscard(Graphics g) {
    	g.setColor(Color.BLACK);
    	g.fillRect(params.DISCARD_X, params.DISCARD_Y,
    			params.DISCARD_WIDTH, params.DISCARD_HEIGHT);
    }
    
    public void drawCenter(Graphics g) {
    	for (int i=0; i<3; i++) {
    		if (i != 1) { //draw cards on outside
    			if (center[i/2] != null) {
    				center[i/2].paint(g, new Rectangle(//i/2 maps 0 -> 0 and 2 -> 1
    					params.CENTER_X + i*defaultCardWidth, 
    					params.CENTER_Y, defaultCardWidth, defaultCardHeight));
    			}
    		} else { //draw deck
    			g.setColor(Color.GRAY);
    			g.fillRect(params.CENTER_X + i*defaultCardWidth, 
    					params.CENTER_Y, defaultCardWidth, defaultCardHeight);
    		}
    	}
    }
    
    private static class ViewParams {
    	int HAND_X, HAND_Y, HAND_WIDTH, HAND_HEIGHT;
    	int DISCARD_X, DISCARD_Y, DISCARD_WIDTH, DISCARD_HEIGHT;
    	int CENTER_X, CENTER_Y, CENTER_WIDTH, CENTER_HEIGHT;
    	int AZONE_X, AZONE_Y, AZONE_WIDTH, AZONE_HEIGHT;
    	
    	public void ViewParams(){}
    }
}
