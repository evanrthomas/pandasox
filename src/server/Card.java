package server;
import server.Affinity;
import server.Zone;
public  abstract class Card {
  Affinity affinity;
  String name;
  int priority;

  public Card(Affinity affinity) {
    this.affinity = affinity;
  }

  public abstract void effect(Board board);

  private class Move extends Card {
    public Move(Affinity affinity) {
      super(affinity);
      name = "Move";
    }

    public void effect(Board board, MultiSocket ms) {
      //CardLocation oldLoc = CardLocation.parse(ms.expect(Protocol.CARD_LOCATION));
      //Zone newLoc = Zone.parse(ms.expect(Protocol.ZONE));
      //Card c = Board.pop(cl);
      //Board.push(c, newZone);
    }
  }
}
