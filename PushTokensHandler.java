import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.*;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.util.*;

public class PushTokensHandler implements HttpHandler {
	public void handle(HttpExchange ex) {
		System.out.println("PushTokensHandler");
		// avgöra typ av request
		if ("POST".equals(ex.getRequestMethod())) {
			try {
				String body = readBody(ex.getRequestBody());
				if (!body.equals("") && body != null) {
					RootServer.getPushNotifier().addToken(body);
					ex.sendResponseHeaders(200, body.length());
					OutputStream os = ex.getResponseBody();
					os.write(body.getBytes());
					os.close();
				}
			} catch (IOException e) {
				System.out.println(e);
			}
		} else {
			try {
				String response = "Only POST requests permitted";
				ex.sendResponseHeaders(403, response.length());
				OutputStream os = ex.getResponseBody();
				os.write(response.getBytes());
				os.close();
			} catch (IOException e) {
				System.out.println(e);
			}

		}
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