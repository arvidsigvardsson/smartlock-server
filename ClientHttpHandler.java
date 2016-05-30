import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.util.*;

/**
* Hanterar anslutning till /client. Här kan olika actions som klienter vill kunna utföra 
* läggas. Den enda i dagsläget är om klienten anger queryn /client?message=open så kommer 
* låset nästa gång det ansluter få meddelandet "open" vilket kommer att öppna låset
*
* @author Arvid Sigvardsson och Sebastian Sologuren
*/
public class ClientHttpHandler implements HttpHandler {
	
	/**
	 * metod som kallas av HttpServer när klassen startas som tråd för att behandla 
	 * inkommande request
	 * 
	 * @params ex objekt som används för att hantera Http-requestet
	 */
	public void handle(HttpExchange ex) throws IOException {
		System.out.println("Http request received, detta är query: " + ex.getRequestURI().getQuery());
		System.out.println("Detta är request method: " + ex.getRequestMethod());

		String response = "";
		int responseCode;
		Map<String, String> params = queryToMap(ex.getRequestURI().getQuery());

		if ("open".equals(params.get("message"))) {
			// signalera till arduinon att låset ska öppnas
			RootServer.getDataContainer().setShouldLockBeOpened(true);
			// lägga till händelse i loggen
			RootServer.getTimestampLog().addTimestamp(ex.getPrincipal().getUsername(), true);
			response = "Opening the lock\n";
			responseCode = 200;
		} else {
			System.out.println("Inte korrekta parametrar");
			response = "Bad Request\n";
			responseCode = 400;
		}
		
		// skicka svar
		ex.sendResponseHeaders(responseCode, response.length());
		OutputStream os = ex.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

	// hjälpmetod för att läsa requestets parametrar och lagra dem i en Map
	private static Map<String, String> queryToMap(String query) {
		Map<String, String> result = new HashMap<String, String>();
		if (query == null) {
			return result;
		}
	    for (String param : query.split("&")) {
	        String pair[] = param.split("=");
	        if (pair.length > 1) {
	            result.put(pair[0], pair[1]);
	        } else {
	            result.put(pair[0], "");
	        }
	    }
	    return result;
  }
}