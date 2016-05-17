import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.*;

public class LockHandler implements Runnable {
	private Socket client;

	

	public LockHandler(Socket client) {
		this.client = client;
	}

	public void run() {

			try {
				InputStream in = client.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));

				String s;
				String response = "";
				DataContainer dataContainer = RootServer.getDataContainer();

				if ((s = reader.readLine()) != null) {
					// ifall låset vill ha en uppdatering
					if(s.charAt(0) == 'u') {
						System.out.println("Arduinon vill ha uppdatering");
						String doorOpenMessage = s.substring(1);

						// ifall klienten vill öppna
						// if(RootServer.getOpenStatus()) {
						if (dataContainer.getShouldLockBeOpened()) {
							// kod för att skicka meddelande...
							response = "open\n";

							// RootServer.setOpenStatus(false);
							dataContainer.setShouldLockBeOpened(false);
						}
						// annars skicka lista med tillåtna id
						else {
							// response = "This is a list\n";
							response = dataContainer.getAcceptanceListArdu();
						}
						
						/*
						// för timeoutalarm
						if (doorOpenMessage.equals("open")) {
							System.out.println("Dörren är öppen");
							if (dataContainer.getTimeoutClock().isTimeUp()) {
								if (!dataContainer.getHasTimeoutPushBeenSent()) {
									dataContainer.sendDoorOpenPush();
								}
								dataContainer.setHasTimeoutPushBeenSent(true);
							}
						} else if (doorOpenMessage.equals("closed")) {
							System.out.println("Dörren är stängd");
							dataContainer.getTimeoutClock().reset();
							dataContainer.setHasTimeoutPushBeenSent(false);
						} else {
							// nåt gick fel
						}
						*/
						
						// tillståndsmaskin för om dörren är öppen eller stängd
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
									RootServer.getPushNotifier().sendDoorOpenPush();
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
					else if (s.charAt(0) == 'c') {
						response = "blip received";
						RootServer.getDataContainer().blip(s.substring(1));
					}
					// för timeoutalarm
					// else if (s.charAt(0) == 't') {
					// 	
					// }
					
					// ifall låset signalerar att dörren är öppen
					// else if (s.equals("door open")) {
					// 	RootServer.getPushNotifier().sendDoorOpenPush();
					// }

					// OutputStream out = client.getOutputStream();
					// PrintWriter writer = new PrintWriter(out);
					// writer.write(response);
					PrintStream ps = new PrintStream(client.getOutputStream());
					ps.print(response);
					System.out.println("Skickar meddelande till arduinon: " + response);

					client.close();



					// writer.write(s + "\n");
					// writer.flush();
					// System.out.println("Mottaget frn klient: " + s);
//



				}
				// writer.close();
				reader.close();
				client.close();

			} catch (Exception e) {

			}

	}
}
