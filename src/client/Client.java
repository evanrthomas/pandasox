package client;
import java.net.*;
import java.io.*;

public class Client {

  final static int PORT = 8000;
  final static String  HOST = "localhost";
  public static void main(String[] adsf) throws IOException {
    Socket socket = new Socket(HOST, PORT);
    PrintWriter out =
      new PrintWriter(socket.getOutputStream(), true);
    out.println("hello");


  }
}

