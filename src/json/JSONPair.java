package json;
public class JSONPair{
  JSONString key;
  JSON value;
  public JSONPair(String key, JSON val) {
    this.key = new JSONString(key);
    this.value = val;
  }
  
  public JSONPair(JSONString key, JSON val) {
	  this.key = key;
	  this.value = val;
  }
}
