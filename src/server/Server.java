package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import json.JSONObject;
import json.JSONString;
import blerg.Protocol;

class Server {
  private MultiSocket ms;
  private Board board;
  private Zone[] hands;
  final static int PORT = 8000;
  final static int NUM_PLAYERS = 2;
  
  public static void main(String[] asdfa) throws IOException {
	    System.out.println("Server main()");
	    ServerSocket ss = new ServerSocket(PORT);
	    MultiSocket ms = new MultiSocket();
	    for (int i=0; i<NUM_PLAYERS; i++) {
	      Socket clientSocket = ss.accept();
	      System.out.println(clientSocket);
	      ms.addSocket(clientSocket);
	      System.out.println("recieved client connection");
	    }

	    Server server = new Server(ms);
	    server.go();
  }
  
  public Server(MultiSocket ms) {
    this.ms = ms;
    board = new Board(new Deck(Card.makeAll(NUM_PLAYERS)));
    hands = new Zone[NUM_PLAYERS];
    for (int i=0; i<NUM_PLAYERS; i++) {
    	hands[i] = new Zone();
    }
  }
  
  public int getNumPlayers() {
	  return NUM_PLAYERS;
  }
  
  public void go() {
    setupPhase();
  }

  private void update() {
    updateBoard();
    updateHand();
  }

  private void updateBoard() {
	  for (int player =0; player<NUM_PLAYERS; player ++) {
		  ms.send(Protocol.UPDATE_BOARD, board.serialize(player), player);
	  }
  }

  private void updateHand() {
	  for (int player=0; player<hands.length; player++) {
		  ms.send(Protocol.UPDATE_HAND, hands[player].serialize(player), player);  
	  }
	  
  }

  //game phases
  private void setupPhase() {
	System.out.println("setupPhase()");
	Card card;
	for (int player=0; player<NUM_PLAYERS; player++) {
		for (int i=0; i<5; i++) {
			card = board.getDeck().pop();
			card.changeVisibility(player, true);
			hands[player].add(card);
		}
		
	}
    ms.broadcast(Protocol.START_GAME);
    update();
    
    //card to center
    ms.broadcast(Protocol.PROMPT_CARD_TO_CENTER);
    JSONObject[] msgs = ms.expectAll(Protocol.CARD);
    Card toCenter;
    System.out.println("center ::" + board.getCenter());
    for (int player=0; player<msgs.length; player++) {
    	toCenter = Card.parse((JSONObject) msgs[player].get("extra"));
    	if (!hands[player].contains(toCenter)) {
    		//TODO: send error message
    		System.out.println("don't contain card "+msgs[player].get("name"));
    	}
    	hands[player].pop(toCenter);
    	board.getCenter().add(toCenter);
    }
    update();
    
    //card to bottom of deck
    ms.broadcast(Protocol.PROMPT_CARD_TO_DECK_BOTTOM);
    msgs = ms.expectAll(Protocol.CARD);
    Card toDeck;
    for (int player=0; player<msgs.length; player++) {
    	toDeck = Card.parse((JSONObject) msgs[player].get("extra"));
    	if (!hands[player].contains(toDeck)) {
    		//TODO: send error message
    		System.out.println("don't contain card "+msgs[player].get("name"));
    	}
    	hands[player].pop(toDeck);
    	board.getCenter().add(toDeck);
    }
    updateHand();
    
  }
}
