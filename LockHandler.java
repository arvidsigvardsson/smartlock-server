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

				if ((s = reader.readLine()) != null) {

					// if(s.equals("on")){
					// 	System.out.println();
					// 	System.out.println("Lampan tänds");
					// }
					// else if(s.equals("off")){
					// 	System.out.println();
					// 	System.out.println("Lampan släcks");
					// }

					// ifall låset vill ha en uppdatering
					if(s.equals("u")) {
						// ifall klienten vill öppna
						if(RootServer.getOpenStatus()) {
							// kod för att skicka meddelande...
							response = "open\n";

							RootServer.setOpenStatus(false);
						}
						// annars skicka lista med tillåtna id
						else {
							// response = "This is a list\n";
							response = "labcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,e5a1ea45,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,abcdef12,e5a1ea45,abcdef12,\n";
						}
					}
					// ifall låset skickar ett blip
					else if (s.charAt(0) == 'c') {
						response = "blip received";
						RootServer.getDataContainer().blip(s.substring(1));
					}

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
