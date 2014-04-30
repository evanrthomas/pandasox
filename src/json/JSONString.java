package json;

import blerg.Protocol;

//TODO: Change to JSONValue<E>, can be int, string, boolean
public class JSONString extends JSON {
  private String val;
  public JSONString(String s) {
    val = s;
  }
  
  //{asdf:[asdf, asdf, {dsfasd: sdf, safd: asd}], key:sadfsd}
  public static JSONString parse(String raw) {
	  assert(raw.charAt(0) == '"');
	  int i = raw.indexOf("\"", 1);
	  assert(i != -1);
	  return new JSONString(raw.substring(1, i));
  }

  public boolean equals(JSON s) {
	 if (!(s instanceof JSONString)) {
		 return false;
	 } else {
		 return ((JSONString) s).val.equals(val);
	 }
  }
  
  public boolean isString(String s) {
	  return val.equals(s);
  }
  
  public String value() {
	  return val;
  }
  public String toString() {
    return "\"" + val + "\"";
  }
}
