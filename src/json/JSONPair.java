package json;
public class JSONPair{
  String key;
  JSON value;
  public JSONPair(String key, JSON val) {
   this.key = key;
   value = val;
  }
  
  public JSONPair(JSONString s, JSON val) {
	  this(s.value(), val);
  }
  
  public JSONPair(String key, String val) {
	  this(key, new JSONString(val));
  }
}
