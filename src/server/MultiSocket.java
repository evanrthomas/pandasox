package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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


  private String readline() throws InterruptedException{
    return q.take();
  }

  public void send(JSON json, int i) {
    writers.get(i).println(json.toString());
  }
  
  public void broadcast(JSON j) {
    for (int i=0; i<sockets.size(); i++) {
      send(j, i);
    }
  }
  
  public void broadcast(Protocol type, JSON json) {
	  for (int i=0; i<writers.size(); i++) {
		  send(new JSONObject(
				  new JSONPair("type", new JSONString(type+"")),
				  new JSONPair("message", json)
				),
			i);
	  }
  }
  
  public void broadcast(Protocol type) {
	  broadcast(type, new JSONObject());
  }
  
  public JSON expect(Protocol type) {
	  JSONObject msg;
	  while (true) {
		  msg = JSONObject.parse(readline());
		  if (msg.get("type")  == type +"") {
			  return msg;
		  } else {
			  // send error msg over the network (not expected
		  }
	  }
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
