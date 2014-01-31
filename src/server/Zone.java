package server;
import java.util.ArrayList;

import json.JSON;
import json.JSONArray;
import json.JSONObject;
import json.JSONPair;
import json.ServerSerializable;
public class Zone implements ServerSerializable {
  private ArrayList<Card> cards;
  private String name;
  private int capacity;
  public boolean barriered; //toggled on when barrier is played into zone, restricts user input into the zone

  public Zone() {
	  this("", -1); //capacity of -1 means infinite
  }
  public Zone(String name, int capacity) {
    this.name = name;
    this.capacity = capacity;
    this.cards = new ArrayList<Card>();
  }
  
  public void add(Card card) {
	  cards.add(card);
  }
  
  public boolean contains(Card card) {
	  for (Card c: cards) {
		  if (card.equals(card)) {
			  return true;
		  }
	  }
	  return false;
  }
  
  public void addAll(Card ... cards) {
	  for (int i=0; i<cards.length; i++) {
		  add(cards[i]);
	  }
  }

  public Card pop(Card card) {
	for (Card c: cards) {
		if (c.equals(card)) {
			cards.remove(c);
			return c;
		}
	}
	return null;
  }
  
  public String toString() {
	  return serialize(0).toString();
  }
  public JSONObject serialize(int playerId) {
    return new JSONObject(
        new JSONPair("cards", new JSONArray(playerId, cards.toArray(new Card[cards.size()])))
    );
  }
}




