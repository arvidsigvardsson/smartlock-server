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
	public void handle(HttpExchange ex) {
		System.out.println("iOS push notis-hanterare");
		// LongPollingPushMessage message = null;
		// boolean sendPush = false;
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

		while (!RootServer.getIosPushMessage().isStillValid()) {
			System.out.println("Message valid? " + RootServer.getIosPushMessage().isStillValid());
		}

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
}