package server;
import java.util.ArrayList;
import java.util.Arrays;

import json.JSON;
import json.JSONArray;
import json.JSONObject;
import json.JSONPair;
import json.PandaSoxSerializable;

public class Board implements PandaSoxSerializable {
  private Player[] players;
  private ArrayList<Card> deck;
  private final int NUM_PLAYERS;
  private final Zone center;
  int priority;

  public Board(int numPlayers) {
	NUM_PLAYERS = numPlayers;
    deck = new ArrayList<Card>();
    deck.addAll(Arrays.asList(Card.getAll()));
    center = new Zone();
  }
  
  @Override
  public JSON serialize() {
    return  new JSONObject(
      new JSONPair("center", center.serialize()),
      new JSONPair("players", new JSONArray(players))
    );
  }
}
