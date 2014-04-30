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
  private BlockingDeque<Tuple<Integer, JSONObject>> q;
  private ArrayList<PrintWriter> writers;
  
  private static MultiSocket publicSocket;
  
  private MultiSocket() {
    sockets = new ArrayList<Socket>();
    q = new LinkedBlockingDeque<Tuple<Integer, JSONObject>>();
    writers = new ArrayList<PrintWriter>();
  }
  
  public static MultiSocket getMultiSocket() {
	  if (publicSocket == null) {
		  return (publicSocket = new MultiSocket());
	  }
	  return publicSocket;
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
  
  private Tuple<Integer, JSONObject> readLineAndPlayer()  {
	  try {
		  return q.take();
	  } catch (InterruptedException e) {
		  throw new RuntimeException(e);
	  }
  }
  
  /**
   * 
   * @param json some json to send over the network
   * @param i to player i
   */
  public void send(Protocol type, JSONObject extra, int i) {
    writers.get(i).println(new JSONObject(
			  new JSONPair("type", new JSONString(type+"")),
			  new JSONPair("extra", extra)
			).toString());
  }
  
  public void send(Protocol type, int i) {
	  writers.get(i).println(new JSONObject(
			  new JSONPair("type", type+"")).toString()
		);
  }
  
  
 /**
  * 
  * @param type the protocol type
  * @param extra extra information to send over the wire
  * 
  */
  public void broadcast(Protocol type, JSONObject extra) {
	  for (int i=0; i<writers.size(); i++) {
		  send(type, extra, i); 
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
   * Hangs until it gets a message with the appropriate type from everyone
   */
  public JSONObject[] expectAll(Protocol type) {
	  Protocol[] arr = new Protocol[sockets.size()];
	  for (int i=0; i< arr.length; i++) {
	    	arr[i] = type;
	    }
	  return expect(arr);
  }
  
  public JSONObject expect(Protocol type, int player) {
	  //TODO: expects only happen after an appropriate message has been sent
	  // and you expect a reply. Make an expect callback to pass into send() instead of
	  // sending then expecting
	  Tuple<Integer, JSONObject> tup;
	  while ((tup = readLineAndPlayer()) != null) {
		  if (tup.getFirst() == player && tup.getSecond().isType(type)) {
			  return tup.getSecond();
		  }
	  }
	  throw new RuntimeException("should never get here"); //TODO: better error message, think about how it could get here
  }
  
  /**
   * Hangs until it gets a message of type1 from p1 and type2 from p2 ..
   * @param type1
   * @param type2
   * @return
   */
  private JSONObject[] expect(Protocol ... types) {
	  //TODO: is this method neccessary? only one method in this class uses
	  // it. I don't think you ever expect multiple different types
	  JSONObject thisMsg;
	  JSONObject[] msgs = new JSONObject[types.length];
	  int player;
	  for (int i=0; i<msgs.length;) {
		  Tuple<Integer, JSONObject> tup = readLineAndPlayer();
		  thisMsg = tup.getSecond();
		  player = tup.getFirst();
		  
		  if (thisMsg.isType(types[player]) && msgs[player] == null) {
			  msgs[player] = thisMsg;
			  i++;
		  }
	  }
	  return msgs;
  }

  private class SingleListener implements Runnable {
    private Socket clientSocket;
    private BlockingDeque<Tuple<Integer, JSONObject>> q;
    private final int id;
    
    public SingleListener(Socket clientSocket, BlockingDeque<Tuple<Integer, JSONObject>> q) {
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
          q.add(new Tuple<Integer, JSONObject>(id, JSONObject.parse(s)));
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
		  JSON[] obj = ms.expectAll(Protocol.TEST);
		  System.out.println("got message ::"+obj);
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
	  Tuple<Integer, JSONObject> tup;
	  while ((tup = ms.readLineAndPlayer()) != null) {
		  System.out.println("Player " + tup.getFirst() + ":: " + tup.getSecond());
	  }
  }
}
