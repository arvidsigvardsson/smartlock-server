import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* Klassen representerar en server som lyssnar på socketanslutningar på port 8888, där arduinon i vårt låssystem 
* ansluter.
*
* @author Arvid Sigvardsson
*
*/
public class LockServer implements Runnable {
	/**
	* metoden run anropas av systemet när klassen startats som en tråd och körs
	*/
	public void run() {
		ServerSocket server;
		ExecutorService executor = Executors.newFixedThreadPool(30);

		try {
			System.out.println("LockServer startad");
			// anger port som anslutning sker till
			server = new ServerSocket(8888);

			// servern ligger i en oändlig loop och lyssnar på en port. Varje ny anslutning skapar en ny tråd av klassen
			// LockHandler
			while (true) {
				try {
					Socket socket = server.accept();
					executor.execute(new LockHandler(socket));

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}


