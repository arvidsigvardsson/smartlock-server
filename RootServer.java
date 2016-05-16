public class RootServer {
	private static boolean openStatus = false;
	private static boolean iosPushDataAvailable = false;
	private static DataContainer dataContainer = new DataContainer("filer/idlist.txt", "filer/idNameMap.txt");
	private static PushNotifier pushNotifier = new PushNotifier("filer/apikey.txt", "filer/pushtokens.txt");
	private static UserContainer userContainer = new UserContainer("filer/userList.txt");
	private static TimestampLog timestampLog = new TimestampLog("filer/timestampLog.txt");

	public static UserContainer getUserContainer(){
		return userContainer;
	}
	public static String getList(){
		String res ="";
		for(String elem:userContainer.getBackupsList()){
			res += "\n"+elem;
		}
		return res;
	}

	public static TimestampLog getTimestampLog() {
		return timestampLog;
	}

	public static String getLog(){
		return getTimestampLog().toString();
	}

	public static String getLog(String only){
		return getTimestampLog().toString(only);
	}

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

	public static boolean getIosPushDataAvailable() {
		return iosPushDataAvailable;
	}

	public static void setIosPushDataAvailable(boolean state) {
		iosPushDataAvailable = state;
	}

	public static void main(String[] args) {
		Thread t1 = new Thread((Runnable) new LockServer());
		// Thread t2 = new Thread((Runnable) new ClientServer());
		Thread t3 = new Thread((Runnable) new ClientHttpServer());
		// Thread t4 = new Thread((Runnable) new ServerComsInterface());
		t1.start();
		// t2.start();
		t3.start();
		// t4.start();
	}
}

