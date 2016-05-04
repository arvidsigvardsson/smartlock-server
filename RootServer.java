public class RootServer {
	private static boolean openStatus = false;
	private static DataContainer dataContainer = new DataContainer("filer/idlist.txt", "filer/idNameMap.txt");
	private static PushNotifier pushNotifier = new PushNotifier("filer/apikey.txt", "filer/pushtokens.txt");

	public static PushNotifier getPushNotifier() {
		return pushNotifier;
	}

	public static DataContainer getDataContainer() {
		return dataContainer;
	}

	public static boolean getOpenStatus() {
		return openStatus;
	}

	public static void setOpenStatus(boolean status) {
		openStatus = status;
	}

	public static void main(String[] args) {
		Thread t1 = new Thread((Runnable) new LockServer());
		Thread t2 = new Thread((Runnable) new ClientServer());
		Thread t3 = new Thread((Runnable) new ClientHttpServer());

		t1.start();
		t2.start();
		t3.start();
	}
}

