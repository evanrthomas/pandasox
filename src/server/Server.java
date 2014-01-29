package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import blerg.Protocol;

public class Server {
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

    GameServer gs = new GameServer(ms, NUM_PLAYERS);
    gs.go();
  }
}

class GameServer {
  private MultiSocket ms;
  private Board board;
  private Player[] players;
  private final int NUM_PLAYERS;
  public GameServer(MultiSocket ms, int numPlayers) {
    this.ms = ms;
    board = new Board(numPlayers);
    players = new Player[numPlayers];
    NUM_PLAYERS = numPlayers;
    for (int i=0; i< numPlayers; i++) {
      players[i] = new Player();
    }
  }
  
  public int getNumPlayers() {
	  return NUM_PLAYERS;
  }
  
  public void go() {
	System.out.println("game server begin");
    setupPhase();
  }

  public void update() {
    updateBoard();
    updateHand();
  }

  public void updateBoard() {
    ms.broadcast(Protocol.UPDATE_BOARD, board.serialize());
  }

  public void updateHand() {
  }

  //game phases
  public void setupPhase() {
	System.out.println("setupPhase()");
    ms.broadcast(Protocol.START_GAME);
    update();
    
    ms.broadcast(Protocol.PROMPT_CARD_TO_CENTER);
    ms.expect(Protocol.PROMPT_CARD_TO_CENTER);
    ms.expect(Protocol.CARD_TO_CENTER);
  }
}
