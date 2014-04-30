
package server;
import java.util.ArrayList;
import json.JSONObject;
import json.JSONPair;
import json.JSONString;
import json.ServerSerializable;
import blerg.Protocol;

class Player implements ServerSerializable {
  private Zone hand, discard;
  private Card awaiting;
  private int id;
  
  private static ArrayList<Player> playerList = new ArrayList<Player>();
  private static int nextId = 0;
  private static MultiSocket ms = MultiSocket.getMultiSocket();
  
  public Player(int id) {
    hand = new Zone();
    discard = new Zone();
    this.id = nextId;
    playerList.add(this);
    nextId++;
  }
  
  public static Player find(int id) {
	  return playerList.get(id);
  }

  public Zone getHand() {
    return hand;
  }
  
  public Zone getDiscard() {
	  return discard;
  }

  public void setAwaiting(Card card) {
	  awaiting = card;
  }
  
  public Card getAwaiting() {
	  return awaiting;
  }
  
  public void useAwaiting() {
	//TODO: consider having something like Protocol.PLAY_DETAILS.BARRIER
	ms.send(Protocol.PROMPT_PLAY_DETAILS, id);
	JSONObject json = ms.expect(Protocol.PLAY_DETAILS, id); //TODO: implement expect from a person. What if the wrong client replies prematurely
	awaiting.effect(id, json);
	awaiting = null;
  }

  @Override
  public JSONObject serialize(int playerId) {
	
    return new JSONObject(
        new JSONPair("hand", hand.serialize(playerId)),
        new JSONPair("discard", discard.serialize(playerId)),
        new JSONPair("awaiting", awaiting == null ? 
        		new JSONString("") : awaiting.serialize(playerId))
        
      );
  }

}
