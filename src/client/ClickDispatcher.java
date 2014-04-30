package client;

import helper.Tuple;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class ClickDispatcher implements MouseListener {
	private ArrayList<Tuple<Rectangle, Card>> visibleCards;
	private ClientView cv;
	
	public ClickDispatcher(ClientView c) {
		cv = c;
		visibleCards = new ArrayList<Tuple<Rectangle, Card>>();
	}
	
	public void registerZone(Rectangle zone, Card c) {
		visibleCards.add(new Tuple<Rectangle, Card>(zone, c));
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Card.unselect();
		Card c;
		for(int i=0; i<visibleCards.size(); i++) {
			if (visibleCards.get(i).getFirst().contains(e.getX(), e.getY())) {
				visibleCards.get(i).getSecond().select();
				cv.repaint();
				return;
			}
		}
		cv.repaint();
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
