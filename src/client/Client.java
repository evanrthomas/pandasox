package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import json.JSON;
import json.JSONArray;
import json.JSONObject;
import json.JSONPair;
import json.JSONString;
import blerg.Protocol;

public class Client {
  private final static int PORT = 8000;
  private final static String  HOST = "localhost";
  private BufferedReader in;
  private Dispatcher dispatcher;
  
  private ArrayList<Card> hand;
  public static void main(String[] adsf) throws IOException {
	(new Client()).go();
    
  }
  
  public Client() throws IOException{
	  Socket socket = new Socket(HOST, PORT);
	  hand = new ArrayList<Card>();
	  dispatcher = new Dispatcher(socket);
	  registerHandlers(dispatcher);
  }
  
  public void go() {
	    Thread dispatcherThread = new Thread(dispatcher);
	    dispatcherThread.start();
  }
  
  public void registerHandlers(Dispatcher d) {
	  d.register(Protocol.START_GAME, new Handler() {
	    	@Override
	    	public void handle(JSONObject json, Writer writer) {
	    		
	    	}
	    });
	  d.register(Protocol.UPDATE_BOARD, new Handler() {
	    	@Override
	    	public void handle(JSONObject json, Writer w) {
	    		
	    	}
	    });
	  
	  d.register(Protocol.UPDATE_HAND, new Handler() {
	    	@Override
	    	public void handle(JSONObject json, Writer w) {
	    		hand = new ArrayList<Card>();
	    		JSON[] arr = ((JSONArray) json.get("cards")).getElms();
	    		for (int i=0; i<arr.length; i++) {
	    			hand.add(Card.parse((JSONObject) arr[i]));
	    		}
	    		printHand(hand);
	    	}
	    });
	  
	  d.register(Protocol.PROMPT_CARD_TO_CENTER, new Handler() {
	    	@Override
	    	public void handle(JSONObject json, Writer writer) {
	    		writer.send(Protocol.CARD, hand.get(0).serialize());
	    	}
	    });
	  
	  d.register(Protocol.PROMPT_CARD_TO_DECK_BOTTOM, new Handler() {
	    	@Override
	    	public void handle(JSONObject json, Writer writer) {
	    		writer.send(Protocol.CARD, hand.get(0).serialize());
	    	}
	    });
  }
  
  public void printHand(ArrayList<Card> hand) {
	  System.out.println("current hand");
	  for(int i=0; i<hand.size(); i++) {
		  System.out.println("\t"+hand.get(i).serialize().toString());
	  }
  }
  
  
}

class Writer {
	PrintWriter out;
	public Writer(Socket socket) throws IOException{
		out = new PrintWriter(socket.getOutputStream(), true);
	}
	
	public void send(Protocol type, JSON json) {
		json = new JSONObject(
				new JSONPair("type", type+""),
				new JSONPair("extra", json));
		System.out.println("sending >>> ::" +json.toString());
		
		out.println(json.toString());
	}
}


class Dispatcher implements Runnable {
	/**
	 * Dispatches incoming messages to the appropriate handler
	 */
	private Map<Protocol, Handler> handlerMap;
	private BufferedReader in;
	private Writer writer;
	
	public Dispatcher(Socket socket) throws IOException{
		in = new BufferedReader(
	            new InputStreamReader(socket.getInputStream()));
		writer = new Writer(socket);
		handlerMap = new HashMap<Protocol, Handler>();
	}
	
	public void register(Protocol type, Handler handler) {
		handlerMap.put(type, handler);
	}
	
	@Override
	public void run() {
		try{
			String line;
			while((line = in.readLine()) != null ) {
				System.out.println("from server <<::" + line);
				JSONObject obj = JSONObject.parse(line);
				Protocol type = Protocol.valueOf(((JSONString) obj.get("type")).value());
				handlerMap.get(type).handle((JSONObject) obj.get("extra"), writer);
			}
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
}

interface Handler {
	public void handle(JSONObject obj, Writer writer);
}

