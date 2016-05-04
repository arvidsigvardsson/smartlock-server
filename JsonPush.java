public class JsonPush {
	private String to;
	private JsonPushData data;

	public String getTo() {
		return to;
	}

	public void setTo(String receiver) {
		this.to = receiver;
	}

	public JsonPushData getData() {
		return data;
	}

	public void setData(JsonPushData data) {
		this.data = data;
	}
}