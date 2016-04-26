import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.*;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParseException;

public class ClientAdminHandler implements HttpHandler {
	public void handle(HttpExchange ex) {
		System.out.println("Adminhanterare");
		// avgöra om det är GET eller POST
		if ("POST".equals(ex.getRequestMethod())) {
			try {
				String body = readBody(ex.getRequestBody());
				System.out.println("Post request, detta är body: " + body ); //readBody(ex.getRequestBody()));
				// String body = readBody(ex.getRequestBody());
				JsonRFID jsonRfid = readJSON(body);
				RootServer.getDataContainer().updateAcceptanceMap(jsonRfid.getRfidMap());
				RootServer.getDataContainer().setIdNameMap(jsonRfid.getIdNameMap());

				ex.sendResponseHeaders(200, body.length());
				OutputStream os = ex.getResponseBody();
				os.write(body.getBytes());
				os.close();
			} catch (IOException e) {
				System.out.println(e);
			}
		} else if ("GET".equals(ex.getRequestMethod())) {
			try {
				System.out.println("Get request");

				// String response = "{\"rfidMap\": {\"abcdef12\": true,\"e5a1ea45\": true,\"f1397af0\": false} }";
				JsonRFID jmap = new JsonRFID();
				jmap.setRfidMap(RootServer.getDataContainer().getAcceptanceList());
				jmap.setIdNameMap(RootServer.getDataContainer().getIdNameMap());

				ObjectMapper mapper = new ObjectMapper();

				String response = mapper.writeValueAsString(jmap);

				ex.sendResponseHeaders(200, response.length());

				OutputStream os = ex.getResponseBody();
				os.write(response.getBytes());
				os.close();
			} catch (IOException e) {
				System.out.println(e);
		}
		} else {

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

	public JsonRFID readJSON(String jsonString) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonRFID jobj = mapper.readValue(jsonString, JsonRFID.class);
			return jobj;
		} catch (IOException e) {
			System.out.println("Något fel med jsonparsing: Detta var strängen: " + jsonString + e);
			return null;
		}
	}
}
