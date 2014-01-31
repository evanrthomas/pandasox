package json;
public class JSONPair{
  JSONString key;
  JSON value;
  public JSONPair(String key, JSON val) {
	  this(new JSONString(key), val);
  }
  
  public JSONPair(JSONString s, JSON val) {
	  this.key = s;
	  this.value = val;
  }
  
  public JSONPair(String key, String val) {
	  this(key, new JSONString(val));
  }
}
