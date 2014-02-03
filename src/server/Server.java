package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import json.JSONArray;
import json.JSONObject;
import json.JSONPair;
import blerg.Protocol;

class Server {
  private MultiSocket ms;
  private Board board;
  private Zone[] hands;
  private Zone[] discards;
  final static int PORT = 8000;
  final static int NUM_PLAYERS = 1;
  
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
    discards = new Zone[NUM_PLAYERS];
    for (int i=0; i<NUM_PLAYERS; i++) {
    	hands[i] = new Zone();
    	discards[i] = new Zone();
    }
  }
  
  public int getNumPlayers() {
	  return NUM_PLAYERS;
  }
  
  public void go() {
    setupPhase();
    while (board.getDeck().size() > 2) {
    	drawPhase();
    	//selectCardPhase();
    	//for (int player=0; player<NUM_PLAYERS; player++) {
    	//	playPhase(player);
    	//}
    }
  }

  private void updateBoard() {
	  for (int player =0; player<NUM_PLAYERS; player ++) {
		  ms.send(Protocol.UPDATE_BOARD, board.serialize(player), player);
	  }
  }
  
  private void updateAllPlayer() {
	  for (int player =0; player<NUM_PLAYERS; player++) {
	    	updatePlayer(player);
	    }
  }

  private void updatePlayer(int player) {
	  ms.send(Protocol.UPDATE_HAND, hands[player].serialize(player), player);
	  ms.send(Protocol.UPDATE_DISCARD, discards[player].serialize(player), player);
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
	start();
	cardToCenter();
	cardToDeckBottom();
  }

  private void start() {
	  ms.broadcast(Protocol.START_GAME);
	  updateBoard();
	  updateAllPlayer();
  }
  
  private void cardToCenter() {
	//card to center
	ms.broadcast(Protocol.PROMPT_CARD_TO_CENTER);
	JSONObject[] msgs = ms.expectAll(Protocol.CARD);
	Card toCenter;
	    
	for (int player=0; player<msgs.length; player++) {
	   	toCenter = Card.parse((JSONObject) msgs[player].get("extra"));
	   	if (!hands[player].contains(toCenter)) {
	   		//TODO: send error message
	   		System.out.println("don't contain card "+msgs[player].get("name"));
	   	}
	   	hands[player].pop(toCenter);
	   	updatePlayer(player);
	   	board.getCenter().add(toCenter);
	   	updateBoard();
	}
  }
  
  private void cardToDeckBottom() {
	ms.broadcast(Protocol.PROMPT_CARD_TO_DECK_BOTTOM);
	JSONObject[] msgs = ms.expectAll(Protocol.CARD);
	Card toDeck;
	for (int player=0; player<msgs.length; player++) {
	  	toDeck = Card.parse((JSONObject) msgs[player].get("extra"));
	   	if (!hands[player].contains(toDeck)) {
	   		//TODO: send error message
	   		System.out.println("don't contain card "+msgs[player].get("name"));
	   	}
	   	hands[player].pop(toDeck);
	   	updatePlayer(player);
	   	board.getDeck().addBottom(toDeck);
	}
  }
  
  private void drawPhase() {
	  Card[][] drawnCards = new Card[NUM_PLAYERS][2];
	  for (int player=0; player < NUM_PLAYERS; player++) {
		  drawnCards[player]= board.getDeck().popMany(2);
		  for (Card c: drawnCards[player]) {
			  c.changeVisibility(player, true);
		  }
		  JSONObject obj = new JSONObject(
				  new JSONPair("cards", new JSONArray(player, 
						  drawnCards[player]))
				  );
		  ms.send(Protocol.PROMPT_DRAWN_CARDS, obj, player);
	  }
	  JSONObject[] cardKept = ms.expectAll(Protocol.CARD);
	  for (int player=0; player<NUM_PLAYERS; player++) {
		  Card kept = Card.parse((JSONObject)cardKept[player].get("extra"));
		  System.out.println("Server.drawPhase() kept:: "+kept);
		  for (int i=0; i<2; i++) {
			  System.out.println("Server.drawPhase() drawnCards "+i 
					  +" :: "+drawnCards[player][i]);
			  System.out.println(kept.equals(drawnCards[player][i]));
			  if (kept.equals(drawnCards[player][i])) {
				  hands[player].add(kept);
				  discards[player].add(drawnCards[player][(i + 1) % 2]);
				  System.out.println("doin it");
				  updatePlayer(player);
				  continue;
			  }
		  }
	  }
  }
}
