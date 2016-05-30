import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.*;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.util.*;

/**
 * Hanterar endpointen /pushtokens, som låter mobilklienter ansluta och lägga till sin 
 * unika token som används vid pushnotiser. Dessa skickas till objektet pushNotifier som 
 * lagrar dem
 *
 * @author Arvid Sigvardsson
 */
public class PushTokensHandler implements HttpHandler {
	
	/**
	 * metod som kallas av HttpServer när klassen startas som tråd för att behandla 
	 * inkommande request
	 * 
	 * @params ex objekt som används för att hantera Http-requestet
	 */
	public void handle(HttpExchange ex) {
		System.out.println("PushTokensHandler");
		String response = "";
		int responseCode = 400;
		// avgöra typ av request
		if ("POST".equals(ex.getRequestMethod())) {
			System.out.println("Post-request");
			String body = readBody(ex.getRequestBody());
			if (!body.equals("") && body != null) {
				// skicka token till pushNotifier där de används för att skicka 
				// pushnotiser
				RootServer.getPushNotifier().addToken(body);
				response = body;
				responseCode = 200;
			} else {
				response = "Empty body not allowed";
				responseCode = 400;
			}
		} else {
			response = "Only POST requests permitted";
			responseCode = 403;
		}
		
		// skicka svar
		try {
			ex.sendResponseHeaders(responseCode, response.length());
			OutputStream os = ex.getResponseBody();
			os.write(response.getBytes());
			os.close();
		} catch (IOException e) {
			System.out.println(e);
		}	
	}

	// hjälpmetod som läser http-body och konverterar till sträng
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