import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
	private Socket client;


	public ClientHandler(Socket client) {
		this.client = client;
	}

	public void run() {

			try {
				OutputStream out = client.getOutputStream();
				PrintWriter writer = new PrintWriter(out);

				InputStream in = client.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));

				String s = null;

				while ((s = reader.readLine()) != null) {

					// if(s.equals("on")){
					// 	System.out.println();
					// 	System.out.println("Lampan tnds");
					// }
					// else if(s.equals("off")){
					// 	System.out.println();
					// 	System.out.println("Lampan slcks");
					// }

					if(s.equals("open")) {
						RootServer.setOpenStatus(true);
					}

					writer.write("Ok, lets open the lock" + "\n");
					writer.flush();
					System.out.println("Mottaget frn klient: " + s);
//



				}
				writer.close();
				reader.close();
				client.close();

			} catch (Exception e) {

			}

	}
}
