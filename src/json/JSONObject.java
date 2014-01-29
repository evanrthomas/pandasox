package json;

import java.util.ArrayList;

public class JSONObject extends JSON{
  JSONPair[] pairs;
  public JSONObject(JSONPair ... pairs) {
    this.pairs = pairs == null ? new JSONPair[] {} : pairs;
  }
  
  public JSON get(String key) {
	  for (JSONPair p: pairs) {
		  if (p.key.toString() == key) {
			  return p.value;
		  }
	  }
	  return null;
  }
  
  public static JSONObject parse(String raw) {
	  System.out.println("\njson object parse() ::" + raw);
	  raw = raw.replaceAll("\\s+", "");
	  ArrayList<JSONPair> pairs = new ArrayList<JSONPair>();
	  int colonLoc, commaLoc;
	  JSONString key;
	  JSON val;
	  assert(raw.charAt(0) == '{');
	  if (raw.charAt(1) == '}') {
		  return new JSONObject(pairs.toArray(new JSONPair[pairs.size()]));
	  }
	  for (int i = 1; i<raw.length();) {
		 
		  key = JSONString.parse(raw.substring(i));
		  colonLoc = i + key.toString().length();
		  assert raw.charAt(colonLoc) == ':' : 
			  "colonLoc is " + raw.charAt(colonLoc);
		  val = JSON.parse(raw.substring(colonLoc + 1));
		  commaLoc = colonLoc + val.toString().length() + 1;
		  assert raw.charAt(commaLoc) == '}' || raw.charAt(commaLoc) == ',' : 
			  "bad comma loc " + raw.charAt(commaLoc);
		  pairs.add(new JSONPair(key, val));
		  if (raw.charAt(commaLoc) == '}') {
			  return new JSONObject(pairs.toArray(new JSONPair[pairs.size()]));
		  }
		  i += key.toString().length() + val.toString().length() + 2;
	  }
	  throw new RuntimeException("no closing } in this json object");
  }

  public String toString() {
    String s = "{";
    JSONPair p;
    for (int i=0; i< pairs.length; i++) {
      p = pairs[i];
      s += p.key + ":" + p.value.toString();
      if (i != pairs.length - 1) {
        s += ",";
      }
    }
    s += "}";
    return s;
  }
}
