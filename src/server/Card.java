package server;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import json.JSONObject;
import json.JSONPair;
import json.JSONString;
import json.ServerSerializable;
public  abstract class Card implements ServerSerializable, Comparable<Card> {

  private final Affinity affinity;
  private final String name;
  private final int priority;
  private boolean[] visibilityArray; // this array has 1 element for each player, 0 = this.card is not visible to that player. 1 = visible to that player
  private int visibleId; //i'd like to make this final, but the compiler won't let me
  private Zone zone;
  
  private static MultiSocket ms = MultiSocket.getMultiSocket();
  private static int NUM_PLAYERS;
  private static ArrayList<Card> cards;
  private static Set<Integer> visibleIds = new HashSet<Integer>();
  private static Map<Integer, Card> visibleIdMap = new HashMap<Integer, Card>();
  
  public abstract void effect(int playedby, JSONObject playdetails);
  
  public Card(String name, Affinity affinity, int priority) {
    this.affinity = affinity;
    this.name = name;
    this.priority = priority;
    //this.ms = MultiSocket.getMultiSocket();
    
    visibilityArray = new boolean[NUM_PLAYERS];
    for (int i = 0; i < visibilityArray.length; i++) {
  	  visibilityArray[i] = false;
  	}
    
    int i;
    while (visibleIds.contains(i = (int)(Math.random()*1000))) {
    	//hang
    }
    visibleId = i;
	visibleIds.add(i);
  }
  
  @Override
  public int compareTo(Card other) {
	  if (priority < other.priority) {
		  return -1;
	  } else {
		  return (Affinity.MIND == this.affinity && Affinity.BODY == other.affinity) ||
				 (Affinity.BODY == this.affinity && Affinity.NATURE == other.affinity) ||
				 (Affinity.NATURE == this.affinity && Affinity.MIND == other.affinity) ?
						 1 : -1;
	  }
	  
  }

  /**
  public void revealToAll() {
	for (int i = 0; i < visibilityArray.length; i++) {
	  visibilityArray[i] = true;
	}
  }
  */
  
  public void changeVisibility(int playerID, boolean visibility) {
	visibilityArray[playerID] = visibility;
  }
  
  public JSONObject serialize(int playerID) {
	if (visibilityArray[playerID] || playerID < 0) {
	  return new JSONObject(
			  new JSONPair("name", name),
			  new JSONPair("affinity", affinity+""),
			  new JSONPair("id", visibleId+"")
			  );
	} else {
	  return new JSONObject(
		new JSONPair("id", visibleId+""),
	    new JSONPair("invisible", new JSONString("true"))
	  );
	}

  }
  
  public static Card parse(JSONObject json) {
	  int id = Integer.parseInt(((JSONString)json.get("id")).value());
	  for (Card c: cards) {
		  if (c.visibleId == id) {
			  return c;
		  }
	  }
	  return null;
  }

  public static Card[] makeAll(int numPlayers) {
	if (cards != null) {
		return cards.toArray(new Card[cards.size()]);
	}
	NUM_PLAYERS = numPlayers;
	cards  = new ArrayList<Card>();
	Affinity[] affinities = {Affinity.MIND, Affinity.BODY, Affinity.NATURE};
	Affinity affinity;
	for (int i=0; i<affinities.length; i++) {
		affinity = affinities[i];
		cards.add(new Barrier(affinity));
		cards.add(new Bury(affinity));
		cards.add(new Swap(affinity));
		cards.add(new Move(affinity));
		cards.add(new Gem(affinity));
		cards.add(new Recall(affinity));
		cards.add(new Smuggle(affinity));
		cards.add(new Lockdown(affinity));
		cards.add(new Pitch(affinity));
		cards.add(new Rearrange(affinity));
	}
	return cards.toArray(new Card[cards.size()]);
  }
  
  private static class Barrier extends Card {
    public Barrier(Affinity affinity) {
	  super("Barrier", affinity, 1);
	}
	    
    @Override
	public void effect(int playedby, JSONObject playdetails) { 
    	int zoneid = Integer.parseInt((
    			(JSONString)playdetails.get("zoneid")).value());
    	Zone.find(zoneid).barrier();
    }
  }
  
  private static class Bury extends Card {
    public Bury(Affinity affinity) {
	  super("Bury", affinity, 2);
	}
	    
	@Override
	public void effect(int playedby, JSONObject playdetails) {
		//need to know the zone the affected card is in (to pop it) and the player 
		//who played this card
		int visibleid = Integer.parseInt((
    			(JSONString)playdetails.get("visibleid")).value());
		Card c = visibleIdMap.get(visibleid);
		
		c.zone.pop(c);
		Player.find(playedby).getDiscard().add(c);
		
	}
  }
  
  private static class Swap extends Card {
	public Swap(Affinity affinity) {
	  super("Swap", affinity, 3);
	}
	
	@Override
	public void effect(int playedby, JSONObject playdetails) {
	  //oldLocA = selectedCardA.zone
	  //oldLocB = selectedCardB.zone
	  //changeZone(cardA, oldLocB)
	  //changeZone(cardB, oldLocA)
	}
  }
  
  private static class Move extends Card {
    public Move(Affinity affinity) {
      super("Move", affinity, 4);
    }
    
    @Override
    public void effect(int playedby, JSONObject playdetails) {
      //CardLocation oldLoc = CardLocation.parse(ms.expect(Protocol.CARD_LOCATION));
      //Zone newLoc = Zone.parse(ms.expect(Protocol.ZONE));
      //Card c = Board.pop(cl);
      //Board.push(c, newZone);
    }
  }
  
  private static class Gem extends Card {
    public Gem(Affinity affinity) {
	  super("Gem", affinity, 5);
	}
	    
	@Override
	public void effect(int playedby, JSONObject playdetails) {
	  //adds another affinity to itself when played face up
	}
  }
  
  private static class Recall extends Card {
	public Recall(Affinity affinity) {
	  super("Recall", affinity, 6);
	}
  
    @Override
    public void effect(int playedby, JSONObject playdetails) {
      //userInput, card in hand = selectedCardA
      //userInput, card in center = selectedCardB
      //changeZone(selectedCardA, center)
      //changeZone(selectedCardB, playersHand)
      //selectedCardA.effect()
    }
  }
  
  private static class Smuggle extends Card {
	public Smuggle(Affinity affinity) {
	  super("Smuggle", affinity, 7);
	}
  
    @Override
    public void effect(int playedby, JSONObject playdetails) {
      //userInput, card in discard = selectedCard
      //changeZone(selectedCard, smuggle.zone)
    }
  }
  
  private static class Lockdown extends Card {
	public Lockdown(Affinity affinity) {
	  super("Lockdown", affinity, 8);
	}
  
    @Override
    public void effect(int playedby, JSONObject playdetails) {
      //
    }
  }
  
  private static class Pitch extends Card {
	public Pitch(Affinity affinity) {
	  super("Pitch", affinity, 9);
	}
  
    @Override
    public void effect(int playedby, JSONObject playdetails) {
      //can't play Pitch if 0 cards in discard. If only 1 card in discard, user only selects 1 card from Hand
      //userInput, selects up to 2 cards from hand = selectedCardA + B
      //userInput, selects same number from discard = selectedCardC + D
      //changeZone(selectedCardA, discard)
      //changeZone(selectedCardB, discard)
      //changeZone(selectedCardC, playersHand)
      //changeZone(selectedCardD, playersHand)
    }
  }
  
  private static class Rearrange extends Card {
	public Rearrange(Affinity affinity) {
	  super("Rearrange", affinity, 10);
	}
  
    @Override
    public void effect(int playedby, JSONObject playdetails) {
      //show in order that card were popped from deck
      //user clicks cards in order 
    }
  }

}
