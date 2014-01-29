package server;
import json.JSON;
import json.JSONObject;
import json.JSONPair;
import json.JSONString;
import json.PandaSoxSerializable;
public  abstract class Card implements PandaSoxSerializable {

  private Affinity affinity;
  private String name;
  private int priority;
  private boolean[] visibilityArray; // this array has 1 element for each player, 0 = this.card is not visible to that player. 1 = visible to that player


  public Card(Affinity affinity) {
    this.affinity = affinity;
  }

  // declare boolean face up/face down
  // 	will be set when a card is played
  //    visibleTo will already be set to only the player who played it; multiplayer may differ

  private void initializeVisibilityArray(Server server) {
	// should be used to initialize each cards visiblityArray. By default, the cards will not be visible to any player
	int numPlayers = server.getNumPlayers();
	for (int i = 0; i < numPlayers; i++) {
	  visibilityArray[i] = false;
	}
  }

  public void revealToAll(Server server) {
	int numPlayers = server.getNumPlayers();
	for (int i = 0; i < numPlayers; i++) {
	  visibilityArray[i] = true;
	}
  }
  
  private void changeVisibility(int playerID) {
	visibilityArray[playerID] = !visibilityArray[playerID];
  }
  
/*  public boolean getVisibility(int playerID) {
	//checks visibility attribute of this.card | If playerID is in visibility array, returns true
	return visibilityArray[playerID];
  }
*/
  
  public JSON serialize(int playerID) {
	if (visibilityArray[playerID]) {
	  return new JSONObject(
	    new JSONPair("affinity", new JSONString(affinity + "")),
	    new JSONPair("name", new JSONString(name))
	  );
	}
	
	else() {
	  return new JSONObject(
	    new JSONPair("visibility", new JSONString("false"))
	  );
	}

  }

  public abstract void effect(Board board);

  public static Card[] getAll() {
    return new Card[]{new Move(Affinity.BODY)};
  }

  /*new file supportEffects.java?? Would hold all supporting methods that simplify each card effect
  supporting effects:
  changeZone: change a cards zone    used for "move", "swap", draw steps??, real talk: everything
  
  changeZone method used for... !barrier, bury, move, swap, !gem,
    recall, smuggle, !lockdown, pitch, !rearrange, ?mirror?      
  
  Problem: for swap/recall, how do we place cards into the exact old slot as before
  */
  
  private static class Barrier extends Card {
    public Barrier(Affinity affinity) {
	  super(affinity);
	  name = "Barrier";
	  priority = 1;
	}
	    
    @Override
	public void effect(Board board) {
	  //toggles Barriered boolean of the zone barrier was played into
      //need getZone method for cards?
    }
  }
  
  private static class Bury extends Card {
    public Bury(Affinity affinity) {
	  super(affinity);
	  name = "Bury";
	  priority = 2;
	}
	    
	@Override
	public void effect(Board board) {
	  //changeZone(selectedCard, discard)
	}
  }
  
  private static class Swap extends Card {
	public Swap(Affinity affinity) {
	  super(affinity);
	  name = "Swap";
	  priority = 3;
	}
	
	@Override
	public void effect(Board board) {
	  //oldLocA = selectedCardA.zone
	  //oldLocB = selectedCardB.zone
	  //changeZone(cardA, oldLocB)
	  //changeZone(cardB, oldLocA)
	}
  }
  
  private static class Move extends Card {
    public Move(Affinity affinity) {
      super(affinity);
      name = "Move";
      priority = 4;
    }
    
    @Override
    public void effect(Board board) {
      //CardLocation oldLoc = CardLocation.parse(ms.expect(Protocol.CARD_LOCATION));
      //Zone newLoc = Zone.parse(ms.expect(Protocol.ZONE));
      //Card c = Board.pop(cl);
      //Board.push(c, newZone);
    }
  }
  
  private static class Gem extends Card {
    public Gem(Affinity affinity) {
	  super(affinity);
	  name = "Gem";
	  priority = 5;
	}
	    
	@Override
	public void effect(Board board) {
	  //adds another affinity to itself when played face up
	}
  }
  
  private static class Recall extends Card {
	public Recall(Affinity affinity) {
	  super(affinity);
	  name = "Recall";
	  priority = 6;
	}
  
    @Override
    public void effect(Board board) {
      //userInput, card in hand = selectedCardA
      //userInput, card in center = selectedCardB
      //changeZone(selectedCardA, center)
      //changeZone(selectedCardB, playersHand)
      //selectedCardA.effect()
    }
  }
  
  private static class Smuggle extends Card {
	public Smuggle(Affinity affinity) {
	  super(affinity);
	  name = "Smuggle";
	  priority = 7;
	}
  
    @Override
    public void effect(Board board) {
      //userInput, card in discard = selectedCard
      //changeZone(selectedCard, smuggle.zone)
    }
  }
  
  private static class Lockdown extends Card {
	public Lockdown(Affinity affinity) {
	  super(affinity);
	  name = "Lockdown";
	  priority = 8;
	}
  
    @Override
    public void effect(Board board) {
      //
    }
  }
  
  private static class Pitch extends Card {
	public Pitch(Affinity affinity) {
	  super(affinity);
	  name = "Pitch";
	  priority = 9;
	}
  
    @Override
    public void effect(Board board) {
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
	  super(affinity);
	  name = "Rearrange";
	  priority = 10;
	}
  
    @Override
    public void effect(Board board) {
      //show in order that card were popped from deck
      //user clicks cards in order 
    }
  }

}
