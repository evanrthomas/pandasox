package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;

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
  private Card lastCardPlayed;
  private ArrayList<Card> hand;
  private ArrayList<Card> azone;
  private ArrayList<Card> discard;
  private Card[] center;
  private ClientView cv;
  
  public static void main(String[] adsf) throws IOException {
	(new Client()).go();
  }
  
  public Client() throws IOException{
	  Socket socket = new Socket(HOST, PORT);
	  hand = new ArrayList<Card>();
	  discard = new ArrayList<Card>();
	  azone = new ArrayList<Card>();
	  center = new Card[2];
	  
	  
	  cv = new ClientView(hand, discard, azone, center);
	  
	  dispatcher = new Dispatcher(socket, cv);
	  registerHandlers(dispatcher);
  }
  
  public void go() {
	  (new Thread(dispatcher)).start();
	  cv.show();
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
	    		JSON[] elms = ((JSONArray)json.get("center")).getElms();
	    		center = new Card[2];
	    		for (int i=0; i<2; i++) {
	    			if (i <elms.length) {
	    				center[i] = Card.parse((JSONObject)elms[i]);
	    			}
	    		}
	    		cv.repaint();
	    	}
	    });
	  
	  d.register(Protocol.UPDATE_PLAYER, new Handler() {
	    	@Override
	    	public void handle(JSONObject json, Writer w) {
	    		hand.clear();
	    		discard.clear();
	    		JSON[] arr = ((JSONArray) json.get("hand")).getElms();
	    		for (int i=0; i<arr.length; i++) {
	    			hand.add(Card.parse((JSONObject) arr[i]));
	    		}
	    		arr = ((JSONArray) json.get("discard")).getElms();
	    		for (int i=0; i<arr.length; i++) {
	    			discard.add(Card.parse((JSONObject) arr[i]));
	    		}
	    		System.out.println("hand");
	    		printCards(hand);
	    	}
	    });
	  
	  d.register(Protocol.PROMPT_CARD_TO_CENTER, new Handler() {
	    	@Override
	    	public void handle(JSONObject json, Writer writer) {
	    		getCard(); //TODO: bookmark
	    		//writer.send(Protocol.CARD, hand.get(0).serialize());
	    	}
	    });
	  
	  d.register(Protocol.PROMPT_CARD_TO_DECK_BOTTOM, new Handler() {
	    	@Override
	    	public void handle(JSONObject json, Writer writer) {
	    		//writer.send(Protocol.CARD, hand.get(0).serialize());
	    	}
	    });
	  
	  d.register(Protocol.PROMPT_DRAWN_CARDS, new Handler() {
		  @Override
		  public void handle(JSONObject json, Writer writer) {
			  System.out.println("drawn cards handler()");
			  JSONObject card = (JSONObject)(
					  (JSONArray)json.get("cards")).getElms()[0];
			  //writer.send(Protocol.CARD, card);
		  }
	  });
	  
	  d.register(Protocol.PROMPT_CARD_TO_PLAY, new Handler() {
		  @Override
		  public void handle(JSONObject json, Writer writer) {
			System.out.println("prompt car to play handler()");
			//writer.send(Protocol.CARD, 
			//		(lastCardPlayed = hand.get(0)).serialize()); 
		  }
	  });
	  
	  d.register(Protocol.PROMPT_PLAY_DETAILS, new Handler() {
		  @Override
		  public void handle(JSONObject json, Writer writer) {
			  
		  }
	  });
  }
  
  public void printCards(ArrayList<Card> hand) {
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
	
	public void send(Protocol type, JSONObject json) {
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
	private ClientView cv;
	
	public Dispatcher(Socket socket, ClientView cv) throws IOException{
		this.cv = cv;
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
				System.out.println("from server <<< ::" + line);
				JSONObject obj = JSONObject.parse(line);
				Protocol type = Protocol.valueOf(((JSONString) obj.get("type")).value());
				cv.setMessage(type +"");
				if (handlerMap.containsKey(type)) {
					dispatch(type, obj);
				}
			}
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void dispatch(Protocol type, JSONObject obj) {
		Card.unselect();
		handlerMap.get(type).handle((JSONObject) obj.get("extra"), writer);
	}
}

interface Handler {
	public void handle(JSONObject obj, Writer writer);
}

