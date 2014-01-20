package protocol;

abstract class JSON {
  public abstract String toString();
}

class JSONObject extends JSON{
  JSONPair[] pairs;
  public JSONObject(JSONPair ... pairs) {
    this.pairs = pairs;
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

class JSONPair{
  String key;
  JSON value;
  public JSONPair(String key, JSON val) {
    this.key = key;
    this.value = val;
  }
}

class JSONString extends JSON {
  String val;
  public JSONString(String s) {
    val = s;
  }

  public String toString() {
    return val;
  }
}

class JSONArray extends JSON {
  JSON[] elms;
  public JSONArray(JSON ... elms) {
    this.elms = elms;
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

