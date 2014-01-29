package json;

public class JSONtest {
	public static  void main(String[] asdf) {
		//simpleStringTest();
		//simpleArrayTest();
		//simpleObjectTest();
		test1();
		//emptyJSONObjectTest();
	}
	
	public static void test(JSON json) {
		System.out.println(json.toString());
		System.out.println(JSON.parse(json.toString()).toString());
	}
	
	public static void emptyJSONObjectTest() {
		JSONObject jobj = new JSONObject();
		test(jobj);
	}
	
	public static void simpleObjectTest() {
		JSONObject jobj = new JSONObject(
				new JSONPair("a", new JSONString("hi")),
				new JSONPair("b", new JSONString("there"))
				);
		test(jobj);
		
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
