package server;
import server.MultiSocket;
import server.Protocol;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Server {
  final static int PORT = 8000;
  public static void main(String[] asdfa) throws IOException {
    System.out.println("Server main()");
    ServerSocket ss = new ServerSocket(PORT);
    MultiSocket ms = new MultiSocket();
    System.out.println("here");
    for (int i=0; i<2; i++) {
      Socket clientSocket = ss.accept();
      System.out.println(clientSocket);
      ms.addSocket(clientSocket);
    }

    GameServer gs = new GameServer(ms, 2);
    try {
      String s;
      while ( (s = ms.readline()) != null) {
          System.out.println(s);
        }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    gs.go();

  }
}

class GameServer {
  MultiSocket ms;
  Board board;
  Player[] players;
  public GameServer(MultiSocket ms, int numPlayers) {
    this.ms = ms;
    board = new Board();
    players = new Player[numPlayers];
    for (int i=0; i< numPlayers; i++) {
      players[i] = new Player();
    }
  }

  public void go() {
    setup();
  }

  public void update() {
    updateBoard();
    updateHand();
  }

  public void updateBoard() {
    ms.broadcast(Protocol.serialize(board));
  }


  //game phases
  public void setup() {
    this.ms.broadcast(Protocol.GAME_START);
    updateBoard();
    updateHand();
  }
}
