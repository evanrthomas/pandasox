package server;
import server.MultiSocket;
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

    GameServer gs = new GameServer();
    try {
      String s;
      while ( (s = ms.readline()) != null) {
          System.out.println(s);
        }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

  }
}

class GameServer {
  public GameServer() {
  }
}
