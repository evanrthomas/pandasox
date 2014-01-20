package protocol.json;
public class JSONArray extends JSON {
  PandaSoxSerializable[] elms;
  public JSONArray(PandaSoxSerializable ... elms) {
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
