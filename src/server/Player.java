package server;
import java.util.ArrayList;

import json.JSON;
import json.JSONArray;
import json.JSONObject;
import json.JSONPair;
import json.JSONString;
import json.PandaSoxSerializable;
class Player implements PandaSoxSerializable{
  public int playerID;
  private ArrayList<Card> hand;
  private Zone actionZone;
  private Card awaiting;
  public Player() {
    hand = new ArrayList<Card>();
    actionZone = new Zone();
  }
  
  public int getPlayerID() {
	return playerID;
  }

  public Zone getActionZone() {
    return actionZone;
  }

  public Card getAwaiting() {
    return awaiting;
  }


  public JSON serialize() {
	JSON awaitingJSON;
    if (awaiting == null) {
      awaitingJSON = new JSONString("");
    } else {
      awaitingJSON = awaiting.serialize();
    }

    return new JSONObject(
        new JSONPair("action zone", actionZone.serialize()),
        new JSONPair("awaiting play zone", awaiting == null ? new JSONString("") : awaiting.serialize()),
        new JSONPair("hand", new JSONArray(hand.toArray(new Card[hand.size()])))
      );
  }

}
