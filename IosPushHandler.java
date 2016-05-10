import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.*;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.util.*;

public class IosPushHandler implements HttpHandler {
	private Object lock;

	public void handle(HttpExchange ex) {
		System.out.println("iOS push notis-hanterare");

		// LongPollingPushMessage message = null;
		boolean sendPush = RootServer.getIosPushMessage().isStillValid();
		// while (!RootServer.getIosPushDataAvailable()) {
		// 	// ligger och väntar...
		// 	System.out.println("Väntar...");
		// }

		// while (!sendPush) {
		// 	message = RootServer.getIosPushMessage();
		// 	sendPush = message != null && message.isStillValid();
		// 	System.out.println("message: " + message);
		// 	System.out.println("sendPush = " + sendPush);
		// }

		while (!sendPush) { //!RootServer.getIosPushMessage().isStillValid()) {
			// System.out.println("Message valid? " + RootServer.getIosPushMessage().isStillValid());
			sendPush = RootServer.getIosPushMessage().isStillValid();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}
		// String request = readBody(ex.getRequestBody());
		// System.out.println(request);

		// lock = RootServer.getIosPushLock();
		// try {
		// 	lock.wait();
		// } catch (InterruptedException e) {
		// 	System.out.println(e);
		// }

		System.out.println("Nu slutar tråden att vänta");

		try {
			String response = RootServer.getIosPushMessage().getMessage();
			ex.sendResponseHeaders(200, response.length());
			OutputStream os = ex.getResponseBody();
			os.write(response.getBytes());
			os.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		// RootServer.setIosPushDataAvailable(false);
	}

	private String readBody(InputStream is) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String input;

			while ((input = br.readLine()) != null) {
				sb.append(input);
			}
			return sb.toString();
		} catch (IOException e) {
			return "Error reading body, " + e;
		}
	}
}