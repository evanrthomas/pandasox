package json;

import java.util.ArrayList;

public class JSONArray extends JSON {
  JSON[] elms;
  public JSONArray(JSON ... elms) {
    this.elms = elms == null ? new JSON[] {} : elms ;
  }
  
  public static JSONArray parse(String raw) {
	  System.out.println("\narray parse() ::" + raw);
	  assert(raw.charAt(0) == '[');
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
		 
		  System.out.println("commaLoc " + commaLoc + " " + 
				  raw.substring(commaLoc -2, commaLoc + 2));
		  
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
}
