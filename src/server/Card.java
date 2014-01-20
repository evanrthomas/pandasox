package server;
import json.JSON;
import json.JSONObject;
import json.JSONPair;
import json.JSONString;
import json.PandaSoxSerializable;
public  abstract class Card implements PandaSoxSerializable {
 
  Affinity affinity;
  String name;
  int priority;

  public Card(Affinity affinity) {
    this.affinity = affinity;
  }

  public JSON serialize() {
    return new JSONObject(
        new JSONPair("affinity", new JSONString(affinity + "")),
        new JSONPair("name", new JSONString(name))
    );
  }

  public abstract void effect(Board board);

  public static Card[] getAll() {
    return new Card[]{new Move(Affinity.BODY)};
  }

  private static class Move extends Card {
    public Move(Affinity affinity) {
      super(affinity);
      name = "Move";
    }
    
    @Override
    public void effect(Board board) {
      //CardLocation oldLoc = CardLocation.parse(ms.expect(Protocol.CARD_LOCATION));
      //Zone newLoc = Zone.parse(ms.expect(Protocol.ZONE));
      //Card c = Board.pop(cl);
      //Board.push(c, newZone);
    }
  }
}
