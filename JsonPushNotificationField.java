public class JsonPushNotificationField {
	private String title;
	private String body;
	private String icon;

	public JsonPushNotificationField(String title, String body, String icon) {
		this.title = title;
		this.body = body;
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	public String getIcon() {
		return icon;
	}
}