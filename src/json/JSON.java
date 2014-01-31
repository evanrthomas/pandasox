package json;
public abstract class JSON {
  public abstract String toString();
  public abstract boolean equals(JSON j);
  public static JSON parse(String s) {
	  //System.out.println("\nJSON parse() ::" + s);
	  s = s.replaceAll("\\s+", "");
	  switch (s.charAt(0)) { 
	  case '{':
		  return JSONObject.parse(s);
	  case '[':
		  return JSONArray.parse(s);
	  case '"':
		  return JSONString.parse(s);
	  default:
		  throw new RuntimeException("JSON doesn't start with valid character " + s.charAt(0));
	  }
  }
}

