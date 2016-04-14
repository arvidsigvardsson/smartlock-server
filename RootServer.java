public class RootServer {
	private static boolean openStatus = false;

	public static boolean getOpenStatus() {
		return openStatus;
	}

	public static void setOpenStatus(boolean status) {
		openStatus = status;
	}

	public static void main(String[] args) {
		Thread t1 = new Thread((Runnable) new LockServer());
		Thread t2 = new Thread((Runnable) new ClientServer());

		t1.start();
		t2.start();
	}
}

