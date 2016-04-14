import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientServer implements Runnable{


	public void run() {
		ServerSocket server;
		ExecutorService executor = Executors.newFixedThreadPool(30);

		try {
			System.out.println("ClientServer startad");
			server = new ServerSocket(4466);

			while (true) {
				try {
					Socket socket = server.accept();
					executor.execute(new ClientHandler(socket));

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// System.out.println("Server up");
	}

	}


