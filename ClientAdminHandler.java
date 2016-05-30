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

/**
 * Hanterar endpointen /admin. Via den kan mobilklienter ansluta för att antingen läsa 
 * vilka rfidkort som är registrerade och har access och deras namn (GET-request), eller
 * uppdatera den informationen (POST-request)
 *
 * @author Arvid Sigvardsson
 */
public class ClientAdminHandler implements HttpHandler {
	/**
	 * metod som kallas av HttpServer när klassen startas som tråd för att behandla 
	 * inkommande request
	 * 
	 * @params ex objekt som används för att hantera Http-requestet
	 */
	public void handle(HttpExchange ex) {
		System.out.println("Adminhanterare");
		// avgöra om det är GET eller POST
		if ("POST".equals(ex.getRequestMethod())) {
			try {
				String body = readBody(ex.getRequestBody());
				System.out.println("Post request, detta är body: " + body );
				// skapar ett jsonobjekt med innehållet i Http-bodyn, hämta ut parametrar // ur det objektet, och skicka vidare dem till dataContainer
				JsonRFID jsonRfid = readJSON(body);
				RootServer.getDataContainer().setAcceptanceMap(jsonRfid.getRfidMap());
				RootServer.getDataContainer().setIdNameMap(jsonRfid.getIdNameMap());

				// svara på requestet med ett eko av det inkommande, så att klienten kan
				// kontrollera att rätt data kommit fram, om klienten vill
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
				// konstruerar ett jsonobjekt för som svar på GET-request, och hämta data // till detta från dataContainer
				JsonRFID jmap = new JsonRFID();
				jmap.setRfidMap(RootServer.getDataContainer().getAcceptanceMap());
				jmap.setIdNameMap(RootServer.getDataContainer().getIdNameMap());
				
				// kolla om dörren är öppen eller stängd
				if (RootServer.getDataContainer().getDoorState() == DoorState.CLOSED) {
					jmap.setDoorOpen(false);
				} else {
					jmap.setDoorOpen(true);
				}
				ObjectMapper mapper = new ObjectMapper();
				String response = mapper.writeValueAsString(jmap);
				
				// skicka svaret
				ex.sendResponseHeaders(200, response.length());
				OutputStream os = ex.getResponseBody();
				os.write(response.getBytes());
				os.close();
			} catch (IOException e) {
				System.out.println(e);
		}
	}

	// hjälpmetod som läser Http-bodyn och returnerar en jsonsträng
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
	
	// hjälpmetod som konverterar en jsonsträng till ett javaobjekt
	private JsonRFID readJSON(String jsonString) {
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
