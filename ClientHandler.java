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

				String s;
				String response = "";

				while ((s = reader.readLine()) != null) {
					if(s.equals("open")) {
						// RootServer.setOpenStatus(true);
						RootServer.getDataContainer().setShouldLockBeOpened(true);
						response = "Ok, lets open the lock" + "\n";
					} else {
						response = "Server received message: " + s;
					}

					writer.write(response);
					writer.flush();
					System.out.println("Mottaget frn klient: " + s);
				}
				writer.close();
				reader.close();
				client.close();

			} catch (Exception e) {

			}

	}
}
