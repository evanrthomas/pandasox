package server;
import helper.Tuple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import json.JSON;
import json.JSONObject;
import json.JSONPair;
import json.JSONString;
import blerg.Protocol;

public class MultiSocket {
  private ArrayList<Socket> sockets;
  private BlockingDeque<Tuple<Integer, String>> q;
  private ArrayList<PrintWriter> writers;
  public MultiSocket() {
    sockets = new ArrayList<Socket>();
    q = new LinkedBlockingDeque<Tuple<Integer, String>>();
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


  private String readline() throws InterruptedException{
	  //TODO: return json not string
    return readLineAndPlayer().getSecond();
  }
  
  private Tuple<Integer, String> readLineAndPlayer() throws InterruptedException {
	  //TODO: return invalid json message if q.getSecond is invalid json
	  return q.take();
  }
  
  /**
   * 
   * @param json some json to send over the network
   * @param i to player i
   */
  public void send(JSON json, int i) {
    writers.get(i).println(json.toString());
  }
  
  
 /**
  * 
  * @param type the protocol type
  * @param json extra information to send over the wire
  * 
  */
  public void broadcast(Protocol type, JSON json) {
	  for (int i=0; i<writers.size(); i++) {
		  send(new JSONObject(
				  new JSONPair("type", new JSONString(type+"")),
				  new JSONPair("message", json)
				),
			i); 
	  }
  }
  
  /**
   * 
   * @param type broadcasts this protocol message to everyone
   */
  public void broadcast(Protocol type) {
	  broadcast(type, new JSONObject());
  }
  
  /**
   * 
   * @param type
   * Hangs until it gets a message with the appropriate type
   */
  public JSON expect(Protocol type) throws InterruptedException{
	  JSONObject msg;
	  while (true) {
		  Tuple<Integer, String> tup = readLineAndPlayer();
		  System.out.println("raw ::"+tup.getSecond());
		  msg = JSONObject.parse(tup.getSecond());
		 
		  if (msg.isType(type)) {
			  return msg;
		  } else {
			  JSONObject obj = new JSONObject( new JSONPair("type",
					  new JSONString(Protocol.UNEXPECTED_MESSAGE_ERROR+"")));
			  send(obj, tup.getFirst());
		  }
	  }
  }

  private class SingleListener implements Runnable {
    private Socket clientSocket;
    private BlockingDeque<Tuple<Integer, String>> q;
    private final int id;
    
    public SingleListener(Socket clientSocket, BlockingDeque<Tuple<Integer, String>> q) {
      synchronized(sockets) {
    	  id = sockets.size() - 1;
      }
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
          q.add(new Tuple<Integer, String>(id, s));
        }
      } catch (IOException e) {
        throw new RuntimeException("io excpetion dawg");
      }
    }
  }
  
  public static void main(String[] args)throws IOException, InterruptedException {
	  System.out.println(Protocol.TEST+"");
	  expectTest();
  }
  
  private static void expectTest() throws IOException, InterruptedException{
	  ServerSocket ss = new ServerSocket(8000);
	  MultiSocket ms = new MultiSocket();
	  for (int i=0; i<1; i++) {
	    Socket clientSocket = ss.accept();
	    System.out.println(clientSocket);
	    ms.addSocket(clientSocket);
	    System.out.println("recieved client connection");
	  }
	  while (true) {
		  JSON obj = ms.expect(Protocol.TEST);
		  System.out.println("got message ::"+obj.toString());
	  }
  }
  
  private static void readLineAndPLayerTest() throws IOException, InterruptedException {
	  ServerSocket ss = new ServerSocket(8000);
	  MultiSocket ms = new MultiSocket();
	  for (int i=0; i<2; i++) {
	    Socket clientSocket = ss.accept();
	    System.out.println(clientSocket);
	    ms.addSocket(clientSocket);
	    System.out.println("recieved client connection");
	  }
	  Tuple<Integer, String> tup;
	  while ((tup = ms.readLineAndPlayer()) != null) {
		  System.out.println("Player " + tup.getFirst() + ":: " + tup.getSecond());
	  }
  }
}
