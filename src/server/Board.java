package server;
import java.util.ArrayList;
import java.util.Arrays;

import json.JSON;
import json.JSONArray;
import json.JSONObject;
import json.JSONPair;
import json.ServerSerializable;

public class Board implements ServerSerializable {
  private Deck deck;
  private final Zone center;
  int priority;

  public Board(Deck deck) {
	this.deck = deck;    
    center = new Zone();
  }
  
  public Deck getDeck() {
	  return deck;
  }
  
  public Zone getCenter() {
	  return center;
  }
  
  @Override
  public JSONObject serialize(int playerId) {
    return  new JSONObject(
      new JSONPair("center", center.serialize(playerId))
    );
  }
}
