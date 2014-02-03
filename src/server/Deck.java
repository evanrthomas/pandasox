package server;

import helper.Helper;

import java.util.Arrays;
import java.util.LinkedList;

import json.JSONObject;
import json.JSONPair;
import json.ServerSerializable;

public class Deck implements ServerSerializable {
	LinkedList<Card> cards;
	public Deck(Card[] cards) {
		Helper.shuffle(cards);
		this.cards = new LinkedList<Card>(Arrays.asList(cards));
	}
	
	
	public Card pop() {
		return cards.poll();
	}
	
	public Card[] popMany(int n) {
		Card[] cards = new Card[n];
		for (int i=0; i<n; i++) {
			cards[i] = pop();
		}
		return cards;
	}
	
	public int size() {
		return cards.size();
	}
	
	public void addBottom(Card c) {
		cards.addLast(c);
	}
	
	@Override
	public JSONObject serialize(int playerId) {
		return new JSONObject(
				new JSONPair("top", cards.getFirst().serialize(playerId)),
				new JSONPair("size", cards.size()+"")
				);
	}
}
