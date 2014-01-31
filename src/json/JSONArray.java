package json;

import java.util.ArrayList;

public class JSONArray extends JSON {
  private JSON[] elms;
  public JSONArray(JSON ... elms) {
    this.elms = elms == null ? new JSON[] {} : elms ;
  }
  
  public JSONArray(int player, ServerSerializable ... elms) {
	  this.elms = new JSON[elms.length];
	  for (int i=0; i<elms.length; i++) {
		  this.elms[i] = elms[i].serialize(player);
	  }
  }
  
  public JSON[] getElms() {
	  return elms;
  }
  
  public static JSONArray parse(String raw) {
	  assert(raw.charAt(0) == '[');
	  if (raw.charAt(1) == ']') {
		  return new JSONArray();

	  }
	  ArrayList<JSON> elms = new ArrayList<JSON>();
	  int commaLoc;
	  for (int i=1; i<raw.length();) {
		  JSON js = JSON.parse(raw.substring(i));
		  commaLoc = i+js.toString().length();
		  assert raw.charAt(commaLoc) == ',' || raw.charAt(commaLoc) == ']' : 
			  "commaLoc is " + raw.charAt(commaLoc);
		  elms.add(JSON.parse(raw.substring(i, commaLoc)));
		  if (raw.charAt(commaLoc) == ']') {
			  return new JSONArray(elms.toArray(new JSON[elms.size()]));
		  }
		  i = commaLoc + 1;
		  
	  }
	  throw new RuntimeException("array has no ]");
  }
  
  public String toString() {
    String s = "[";
    for (int i=0; i<elms.length; i++) {
      s += elms[i].toString();
      if (i != elms.length -1) {
        s += ",";
      }
    }
    s += "]";
    return s;
  }
  
  @Override
  public boolean equals(JSON json) {
	  return false;
  }
}
