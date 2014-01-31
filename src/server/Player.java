/**
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
  private Zone actionZone, hand;
  private Card awaiting;
  public Player() {
    hand = new Zone();
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


  public JSONObject serialize(int playerId) {
	JSON awaitingJSON;
    if (awaiting == null) {
      awaitingJSON = new JSONString("");
    } else {
      awaitingJSON = awaiting.serialize(playerId);
    }

    return new JSONObject(
        new JSONPair("action zone", actionZone.serialize()),
        new JSONPair("awaiting play zone", awaiting == null ? new JSONString("") : awaiting.serialize()),
        new JSONPair("hand", new JSONArray(hand.toArray(new Card[hand.size()])))
      );
  }

}*/
