package server;
import server.Card;
public class Zone {
  ArrayList<Card> cards;
  String name;
  int capacity;

  public Zone(String name, int capacity) {
    this.name = name;
    this.capacity = capacity;
  }
}
