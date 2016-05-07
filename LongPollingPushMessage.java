import java.util.Date;

public class LongPollingPushMessage {
	private Date createdAt;
	private int validSeconds;
	private String message;

	public LongPollingPushMessage(String message) {
		this(message, 2); // default 2 sekunder
	}

	public LongPollingPushMessage(String message, int validSeconds) {
		System.out.println("Ny pushnotis skapad");
		createdAt = new Date();
		this.validSeconds = validSeconds;
		this.message = message;
	}

	public boolean isStillValid() {
		Date now = new Date();
		Date timeout = new Date(createdAt.getTime() + validSeconds * 1000);
		return now.before(timeout);
	}

	public String getMessage() {
		return message;
	}

	public static void main(String[] args) {
		LongPollingPushMessage msg = new LongPollingPushMessage("Meddelande", 1);
		for (int i = 0; i < 20; i++) {
			System.out.println("Timeout? " + msg.isStillValid());
			try {
				Thread.sleep(250);
			} catch(InterruptedException ex) {
    			Thread.currentThread().interrupt();
			}
		}
	}
}