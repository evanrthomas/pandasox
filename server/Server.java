package server;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;


public class Server {
  final static int PORT = 8000;
  public static void main(String[] asdfa) throws IOException {
    ServerSocket ss = new ServerSocket(PORT);
    MultiSocket ms = new MultiSocket();
    for (int i=0; i<2; i++) {
      Socket clientSocket = ss.accept();
      ms.addSocket(clientSocket);
    }

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


class MultiSocket {
  ArrayList<Socket> sockets;
  BlockingDeque<String> q;
  public MultiSocket() {
    sockets = new ArrayList<Socket>();
    q = new LinkedBlockingDeque<String>();
  }

  public void addSocket(Socket s) {
    sockets.add(s);
    (new Thread(new SingleListener(s, q))).start();
  }


  public String readline() throws InterruptedException{
    return q.take();
  }



  private class SingleListener implements Runnable {
    Socket clientSocket;
    BlockingDeque<String> q;

    public SingleListener(Socket clientSocket, BlockingDeque<String> q) {
      this.clientSocket = clientSocket;
      this.q = q;
    }

    @Override
    public void run() {
      try {
        BufferedReader in = new BufferedReader(
          new InputStreamReader(clientSocket.getInputStream()));
        String s;
        while ((s = in.readLine()) != null) {
          q.add(s);
        }
      } catch (IOException e) {
        throw new RuntimeException("io excpetion dawg");
      }
    }
  }
}
