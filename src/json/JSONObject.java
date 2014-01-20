package json;
public class JSONObject extends JSON{
  JSONPair[] pairs;
  public JSONObject(JSONPair ... pairs) {
    this.pairs = pairs == null ? new JSONPair[] {} : pairs;
  }

  public String toString() {
    String s = "{";
    JSONPair p;
    for (int i=0; i< pairs.length; i++) {
      p = pairs[i];
      s += "\"" + p.key + "\":" + p.value.toString();
      if (i != pairs.length - 1) {
        s += ",";
      }
    }
    s += "}";
    return s;
  }
}
