package json;
public class JSONString extends JSON {
  String val;
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

  public String toString() {
    return "\"" + val + "\"";
  }
}
