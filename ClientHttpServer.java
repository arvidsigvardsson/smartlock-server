import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClientHttpServer implements Runnable {
	public void run() {
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
			server.createContext("/client", new ClientHttpHandler()); //new MyHandler());
			server.setExecutor(Executors.newFixedThreadPool(30));
			server.start();
		} catch (IOException e) {
			System.out.println(e);
		}

	}

	static class MyHandler implements HttpHandler {
	public void handle(HttpExchange t) throws IOException {

	}
}
}

