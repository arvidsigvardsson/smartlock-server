import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.*;

/**
* LockHandler skapas som tråd av LockServer, och varje objekt hanterar en socketanslutning från en arduino
*
* @author Arvid Sigvardsson
*/
public class LockHandler implements Runnable {
	private Socket client;
	
	/**
	* Konstruerar ett objekt med angiven socket
	* @param client Den socketanslutning som skall hanteras
	*/
	public LockHandler(Socket client) {
		this.client = client;
	}

	/**
	* metod som kallas av systemet när klassen körs som tråd
	*/
	public void run() {
			try {
				// för inläsning av meddelandet över socketen
				InputStream in = client.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));

				String input;
				String response = "";
				DataContainer dataContainer = RootServer.getDataContainer();

				if ((input = reader.readLine()) != null) {
					// ifall låset vill ha en uppdatering
					if(input.charAt(0) == 'u') {
						System.out.println("Arduinon vill ha uppdatering");
						String doorOpenMessage = input.substring(1);

						// ifall klienten vill öppna
						if (dataContainer.getShouldLockBeOpened()) {
							// kod för att skicka meddelande...
							response = "open\n";

							// RootServer.setOpenStatus(false);
							dataContainer.setShouldLockBeOpened(false);
						}
						// annars skicka lista med tillåtna id
						else {
							response = dataContainer.getAcceptanceListArdu();
						}

						// tillståndsmaskin för om dörren är öppen eller stängd, fastställer vilka pushnotiser som ska 
						// skickas
						DoorState nextState = dataContainer.getDoorState();
						switch(dataContainer.getDoorState()) {
							case CLOSED:
								if (doorOpenMessage.equals("open")) {
									System.out.println("Nu öppnas dörren, tyst notis ska skickas");
									RootServer.getPushNotifier().sendSilentDoorOpenPush();
									nextState = DoorState.OPEN;
								} else {
									dataContainer.getTimeoutClock().reset();
								}
								break;
							case OPEN:
								if (doorOpenMessage.equals("closed")) {
									System.out.println("Nu stängs dörren, tyst notis ska skickas");
									RootServer.getPushNotifier().sendSilentDoorClosedPush();
									nextState = DoorState.CLOSED;
								} else if (dataContainer.getTimeoutClock().isTimeUp()) {
									System.out.println("Nu går larmet, synlig notis ska skickas");
									RootServer.getPushNotifier().sendDoorOpenPush(dataContainer.getTimeout());
									nextState = DoorState.OPEN_ALARM;
								}
								break;
							case OPEN_ALARM:
								if (doorOpenMessage.equals("closed")) {
									RootServer.getPushNotifier().sendSilentDoorClosedPush();
									nextState = DoorState.CLOSED;
								}
								break;
							default:
								break;
						}
						dataContainer.setDoorState(nextState);
					}

					// ifall låset skickar ett blip
					else if (input.charAt(0) == 'c') {
						response = "blip received";
						RootServer.getDataContainer().blip(input.substring(1));
					}
					
					// skickar meddelande tillbaka över socketen och stänger den sen
					PrintStream ps = new PrintStream(client.getOutputStream());
					ps.print(response);
					System.out.println("Skickar meddelande till arduinon: " + response);

					client.close();
				}
				reader.close();
				client.close();

			} catch (Exception e) {
				System.out.println(e);
			}

	}
}
