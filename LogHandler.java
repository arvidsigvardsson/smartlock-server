import java.io.IOException;
import java.io.OutputStream;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
/**
 * Denna klass hanterar HttpContextex log där en användare vill få tag på loggen som lagras internt i servern.
 * Man kan då få en logg på två olika sätt, i båda fallen som en lång sträng.
 * 1. /log? - ger hela loggen
 * 2. /log?search=sökterm - ger en log innehållandes endast de timestamps som innehöll söktermen.
 * @author Admin
 *
 */
public class LogHandler implements HttpHandler {
	public void handle(HttpExchange ex) throws IOException {
		System.out.println("Http request received, detta är query: " + ex.getRequestURI().getQuery());
		System.out.println("Detta är request method: " + ex.getRequestMethod());

		String response = "";
		String query = ex.getRequestURI().getQuery();
		String[] queries = queryToQueries(query);
		if (queries[0].equals("search")
				&& queries[1].length() > 0) {/* Accepted Commands */
			System.out.println("Sending log containing seach term\\s.\n");
			response = RootServer.getLog(queries[1]);
		} else {
			System.out.println("Sending complete log\n");
			response = RootServer.getLog();
		}
		ex.sendResponseHeaders(200, response.length());
		OutputStream os = ex.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
	/**
	 * Parse:ar queries så att search läggs in som ett kommand och resterande efter "="-tecknet 
	 * som parametrar till kommandot. 
	 * @param query
	 * @return
	 */
	public static String[] queryToQueries(String query) {
		return query.split("=");
	}
	
	/**
	 * För att testa klassen isolerat.
	 * @param args
	 */
	public static void main(String[] args) {
		String[] arr = LogHandler.queryToQueries("search=E&I");
		// String[] arr = LogHandler.queryToQueries("");
		for (String elem : arr) {
			System.out.println(elem + "    " + elem.length());
		}
		System.out.println(arr[1]);
		String arr2 = RootServer.getLog(arr[1]);
		System.out.println("-----------------------");
		System.out.println(arr2);
	}
}