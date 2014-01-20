package server;
import server.Hand;
import server.Zone;
class Player {
  Hand hand;
  Zone actionZone, awaitingPlayZone;
  public Player() {
    hand = new Hand();
    actionZone = new Zone();
    awaitingPlayZone = new Zone();
  }
}
