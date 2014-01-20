package json;
public class JSONString extends JSON {
  String val;
  public JSONString(String s) {
    val = s;
  }

  public String toString() {
    return val;
  }
}
