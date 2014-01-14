package server;
import java.util.ArrayList;
import java.net.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.io.*;

public class MultiSocket {
  ArrayList<Socket> sockets;
  BlockingDeque<String> q;
  ArrayList<PrintWriter> writers;
  public MultiSocket() {
    sockets = new ArrayList<Socket>();
    q = new LinkedBlockingDeque<String>();
    writers = new ArrayList<PrintWriter>();
  }

  public void addSocket(Socket s) {
    sockets.add(s);
    try {
      writers.add(
        new PrintWriter(s.getOutputStream(), true)
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    (new Thread(new SingleListener(s, q))).start();
  }


  public String readline() throws InterruptedException{
    return q.take();
  }

  public void send(String s, int i) {
    writers.get(i).println(s);
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
