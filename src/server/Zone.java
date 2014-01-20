package server;
import java.util.ArrayList;

import json.JSON;
import json.JSONArray;
import json.JSONObject;
import json.JSONPair;
import json.PandaSoxSerializable;
public class Zone implements PandaSoxSerializable {
  private ArrayList<Card> cards;
  private String name;
  private int capacity;

  public Zone() {
	  this("", -1); //capacity of -1 means infinite
  }
  public Zone(String name, int capacity) {
    this.name = name;
    this.capacity = capacity;
    this.cards = new ArrayList<Card>();
  }

  public int size() {
    return cards.size();
  }

  public JSON serialize() {
    return new JSONObject(
        new JSONPair("cards", new JSONArray(cards.toArray(new Card[cards.size()])))
    );
  }
}




