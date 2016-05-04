import java.util.ArrayList;

public class JsonPush {
	// private String to;
	private ArrayList<String> registration_ids;
	private JsonPushData data;

	// public String getTo() {
	// 	return to;
	// }

	// public void setTo(String receiver) {
	// 	this.to = receiver;
	// }

	public JsonPushData getData() {
		return data;
	}

	public void setData(JsonPushData data) {
		this.data = data;
	}

	public ArrayList<String> getRegistration_ids() {
		return registration_ids;
	}

	public void setRegistration_ids(ArrayList<String> list) {
		registration_ids = list;
	}
}