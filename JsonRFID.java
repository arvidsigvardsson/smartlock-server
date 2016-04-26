import java.util.HashMap;
// import com.fasterxml.jackson.databind.*;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.core.JsonParseException;
// import java.io.IOException;


public class JsonRFID {
	private HashMap<String, Boolean> rfidMap;
	private HashMap<String, String> idNameMap;

	public HashMap<String, String> getIdNameMap() {
		return idNameMap;
	}

	public void setIdNameMap(HashMap<String, String> map) {
		idNameMap = map;
	}

	public HashMap<String, Boolean> getRfidMap() {
		return rfidMap;
	}

	public void setRfidMap(HashMap<String, Boolean> map) {
		rfidMap = map;
	}

	// public static void main(String[] args) throws IOException {
	// 	String json = "{\"rfidMap\": {\"abcdef12\": true,\"e5a1ea45\": true,\"f1397af0\": false} }";
	// 	ObjectMapper mapper = new ObjectMapper();
	// 	JsonRFID obj = mapper.readValue(json, JsonRFID.class);
	// 	System.out.println(obj.getRfidMap().toString());

	// 	String jsonOutput = mapper.writeValueAsString(obj);
	// 	System.out.println(jsonOutput);
	// }
}