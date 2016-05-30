/**
* RootServer är klassen vars mainmetod startar projektet. Den skapar två servertrådar, en
* för kommunikation med arduinon i låset, och en för kommunikation med 
* klientapplikationer. Dessutom kan den starta en tråd för att interaktivt kunna testa
* servern.
* Dessutom skapar den tre objekt som klassvariabler som håller data i systemet, samt en 
* klass för att skicka pushnotiser, och exponerar dem för alla klasser i systemet med 
* get-metoder. Ett objekt som vill skicka pushnotis kan skriva
* RootServer.getPushNotifier() för att få tillgång till det objektet.
*
* @author Arvid Sigvardsson och Sebastian Sologuren
*/

public class RootServer {
	// dataContainer fungerar till stor del som vår databas. Den sparar data i textfil om 
	// rfid-kort som scannats, om de har access, status om låset ska låsas upp och om 
	// dörren är öppen eller stängd
	private static DataContainer dataContainer = new DataContainer("filer/idList.txt", 
																"filer/idNameMap.txt");
	// används för att skicka pushnotiser
	private static PushNotifier pushNotifier = new PushNotifier("filer/apikey.txt", 
																"filer/pushtokens.txt");
	// lagrar användarnamn och lösenord
	private static UserContainer userContainer = new UserContainer("filer/userList.txt");
	// lagrar händelser för låset i en log
	private static TimestampLog timestampLog = new TimestampLog("filer/timestampLog.txt");

	public static UserContainer getUserContainer() {
		return userContainer;
	}

	public static TimestampLog getTimestampLog() {
		return timestampLog;
	}

	public static PushNotifier getPushNotifier() {
		return pushNotifier;
	}

	public static DataContainer getDataContainer() {
		return dataContainer;
	}

	public static void main(String[] args) {
		// parameter till LockServer och ClientHttpServer är portnumret de ska lyssna på
		Thread lockServerThread = new Thread((Runnable) new LockServer(8888));
		Thread clientHttpServerThread = new Thread((Runnable) new ClientHttpServer(8080));
		Thread interactiveThread = new Thread((Runnable) new ServerComsInterface());
		
		lockServerThread.start();
		clientHttpServerThread.start();
		interactiveThread.start();
	}
}

