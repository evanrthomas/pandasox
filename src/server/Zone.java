package server;
import java.util.ArrayList;
import java.util.Collection;

import json.JSONArray;
import json.ServerSerializable;
public class Zone implements ServerSerializable {
  private ArrayList<Card> cards;
  private int capacity;
  public boolean barriered; //toggled on when barrier is played into zone, restricts user input into the zone
  
  public static ArrayList<Zone> zoneList = new ArrayList<Zone>();
  public static int nextId = 0;
  
  public Zone() {
	  this( -1); //capacity of -1 means infinite
  }
  
  public Zone(int capacity) {
    this.capacity = capacity < 0? Integer.MAX_VALUE :  capacity;
    this.cards = new ArrayList<Card>();
    this.barriered = false;
    zoneList.add(this);
    nextId++;
  }
  
  public static Zone find(int id) {
	  return zoneList.get(id);
  }
  
  public static Collection<Zone> findAll() {
	  return zoneList;
  }
  
  public void add(Card card) {
	  if (!barriered && cards.size() < capacity) {
		  cards.add(card);
	  }
  }
  
  public boolean contains(Card card) {
	  for (Card c: cards) {
		  if (card.equals(c)) {
			  return true;
		  }
	  }
	  return false;
  }
  
  public void addAll(Card ... cards) {
	  if (!barriered) {
		  for (int i=0; i<cards.length; i++) {
			  add(cards[i]);
		  }
	  }
  }

  public Card pop(Card card) {
	for (Card c: cards) {
		if (c.equals(card)) {
			//c.
			cards.remove(c);
			return c;
		}
	}
	return null;
  }
  
  public void barrier() {
	  barriered = true;
  }
  
  public void unbarrier() {
	  barriered = false;
  }
  
  public String toString() {
	  return serialize(0).toString();
  }
  public JSONArray serialize(int playerId) {
    return new JSONArray(playerId, cards.toArray(new Card[cards.size()]));
  }
}




