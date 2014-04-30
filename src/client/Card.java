package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import json.JSONObject;
import json.JSONPair;
import json.JSONString;

public class Card implements ClientSerializable {
	private final int visibleId;
	private final String name;
	private final Affinity affinity;
	private static int defaultWidth, defaultHeight;
	private static Card selected;
	
	private Card(int id, String name, Affinity affinity) {
		this.visibleId = id;
		this.name = name;
		this.affinity = affinity;
	}
	
	public static Card parse(JSONObject json) {
		return new Card(
				Integer.parseInt(((JSONString)json.get("id")).value()),
				((JSONString) json.get("name")).value(),
				Affinity.valueOf(((JSONString)json.get("affinity")).value()));
	}
	
	public static Card peekSelected() {
		return selected;
	}
	
	public void select() {
		System.out.println("clicked "+visibleId);
		selected = this;
	}
	
	public static void unselect() {
		selected = null;
	}
	
	public static void setDefaultWidthHeight(int width, int height) {
		defaultWidth = width; defaultHeight = height;
	}
	
	public void paint(Graphics g, Rectangle r) {
		switch (affinity) {
		case NATURE:
			g.setColor(Color.GREEN);
			break;
		case MIND:
			g.setColor(Color.BLUE);
			break;
		case BODY:
			g.setColor(Color.RED);
			break;
		}
		g.fillRect(r.x, r.y, r.width-2, r.height-2);
		g.setColor(Color.BLACK);
		g.drawString(name, r.x+5, r.y+r.height/4 - 5);
		
		if (selected == this) {
			drawBorder(g, r);
		}
	}
	
	private void drawBorder(Graphics g, Rectangle r) {
		System.out.println("drawBorder()");
		g.setColor(Color.YELLOW);
		g.fillRect(r.x, r.y, r.width, 5);
		g.fillRect(r.x, r.y, 5, r.height);
		g.fillRect(r.x + r.width - 5, r.y, 5, r.height);
		g.fillRect(r.x, r.y + r.height - 5, r.width, 5);
	}
	
	
	
	@Override
	public JSONObject serialize() {
		return new JSONObject(
				//new JSONPair("name", name),
				new JSONPair("id", visibleId+""));
	}
	
	private static enum Affinity {
		NATURE, MIND, BODY
	}
	
}
