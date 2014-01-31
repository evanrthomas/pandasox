package client;

import json.JSONObject;
import json.JSONPair;
import json.JSONString;

public class Card implements ClientSerializable{
	private final int visibleId;
	private final String name;
	public Card(int id, String name) {
		this.visibleId = id;
		this.name = name;
	}
	
	public static Card parse(JSONObject json) {
		return new Card(
				Integer.parseInt(((JSONString)json.get("id")).value()),
				((JSONString) json.get("name")).value());
	}
	
	@Override
	public JSONObject serialize() {
		return new JSONObject(
				new JSONPair("name", name),
				new JSONPair("id", visibleId+""));
	}
}
