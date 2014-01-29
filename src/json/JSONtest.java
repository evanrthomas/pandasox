package json;

import blerg.Protocol;

public class JSONtest {
	public static  void main(String[] asdf) {
		//simpleStringTest();
		//simpleArrayTest();
		//simpleObjectTest();
		//test1();
		//emptyJSONObjectTest();
		//jsonObjectGetTest();
		//jsonStringEqualityTest();
		objIsTypeTest();
	}
	
	public static void objIsTypeTest() {
		JSONObject jobj = new JSONObject(
				new JSONPair("type", Protocol.TEST+"")
				);
		System.out.println(jobj);
		System.out.println(jobj.isType(Protocol.TEST));
	}
	
	public static void jsonObjectGetTest() {
		JSONObject jobj = new JSONObject(
				new JSONPair("hi", "there")
				);
		System.out.println(jobj.get("hi"));
		System.out.println(jobj.get("there"));
	}
	
	public static void jsonStringEqualityTest() {
		JSONString str = new JSONString("hi");
		JSONString test = new JSONString(Protocol.TEST+"");
		System.out.println(str.equals("hi"));
		System.out.println(test.equals(Protocol.TEST+""));
	}
	public static void testParse(JSON json) {
		System.out.println(json.toString());
		System.out.println(JSON.parse(json.toString()).toString());
	}
	
	public static void emptyJSONObjectTest() {
		JSONObject jobj = new JSONObject();
		testParse(jobj);
	}
	
	public static void simpleObjectTest() {
		JSONObject jobj = new JSONObject(
				new JSONPair("a", new JSONString("hi")),
				new JSONPair("b", new JSONString("there"))
				);
		testParse(jobj);
		
	}
	
	public static void simpleStringTest() {
		JSONString js = new JSONString("hi");
		System.out.println(js.toString());
		System.out.println(JSON.parse(js.toString()).toString());
	}
	
	public static void simpleArrayTest() {
		JSONArray json = new JSONArray(
				new JSONString("hi"),
				new JSONString("there")
		);
		System.out.println(json.toString());
		System.out.println(JSON.parse(json.toString()).toString());
	}
	
	public static void test1() {
		JSON json = new JSONObject(
		    new JSONPair(
			    "key", new JSONArray(new JSONString("hi"), new JSONString("there"))
			),
		    new JSONPair(
		    		"key2", new JSONObject()
		    		)
		);
		System.out.println(json.toString());
		System.out.println(JSON.parse(json.toString()).toString());
	}
}
