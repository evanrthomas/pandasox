package server;
import server.Deck;
import server.Zone;
import server.Player;
public class Board {
  Player[] players;
  Deck deck;
  Zone center;
  int priority;

  public Board() {
    deck = Deck.getStartingDeck();
  }

  public
}
