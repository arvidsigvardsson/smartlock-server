import java.io.IOException;
import java.io.OutputStream;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Denna klass hanterar HttpContextex log där en användare vill få tag på loggen
 * som lagras internt i servern. Man kan då få en logg på två olika sätt, i båda
 * fallen som en lång sträng. 1. /log? - ger hela loggen 2. /log?search=sökterm
 * - ger en log innehållandes endast de timestamps som innehöll söktermen.
 * 
 * @author Sebastian Sologuren
 *
 */
public class LogHandler implements HttpHandler {
	public void handle(HttpExchange ex) throws IOException {
		System.out.println("Http request received, detta är query: " + ex.getRequestURI().getQuery());
		System.out.println("Detta är request method: " + ex.getRequestMethod());

		String response = "";
		String query = ex.getRequestURI().getQuery();
		String[] initialQueries = query.split("=");
		if (initialQueries[0].equals("search")
				&& initialQueries[1].length() > 0) {/* Accepted Commands */
			System.out.println("Sending log containing seach term\\s:  " + initialQueries[1] + "\n");
			if (initialQueries[1].contains("||")) {
				String secondaryQueries[] = initialQueries[1].split("||");
				System.out.println("secondaryQueries[0]: " + secondaryQueries[0] + "\nsecondaryQueries[1]: "
						+ secondaryQueries[1]);
				for (String elem : secondaryQueries) {
					if (elem.contains("&")) {
						String searchTerms[] = elem.split("&");
						for (String searchTerm : searchTerms) {
							System.out.println("Current search term: " + searchTerm);
							response += RootServer.getTimestampLog().toString(searchTerm);
						}

					}
				}

			} else {
				response = RootServer.getTimestampLog().toString(initialQueries[1]);
			}
			System.out.println("RESULTAT:  " + response);
		} else {
			System.out.println("Sending complete log\n");
			response = RootServer.getTimestampLog().toString();
			System.out.println("START");
			System.out.println(response);
			System.out.println("END");
		}
		ex.sendResponseHeaders(200, response.length());
		OutputStream os = ex.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

	/**
	 * För att testa klassen isolerat utan HttpExchange.
	 * 
	 * @param args
	 */
	public static void test(String query) {
		String response = "";
		String[] initialQueries = query.split("=");
		System.out.println("initialQueries[0]: " + initialQueries[0] + "\ninitialQueries[1]: " + initialQueries[1]);
		if (initialQueries[0].equals("search")
				&& initialQueries[1].length() > 0) {/* Accepted Commands */
			System.out.println("Sending log containing search term\\s:  " + initialQueries[1] + "\n");

			response = RootServer.getTimestampLog().toString(initialQueries[1]);
			System.out.println("RESULTAT:  " + response);
		} else {
			System.out.println("Sending complete log\n");
			response = RootServer.getTimestampLog().toString();
			System.out.println("\nChecking contents of timestamp log that I am about to send to client:");
			System.out.println("START");
			System.out.println(response);
			System.out.println("END");
		}
	}

	/**
	 * För att testa klassen isolerat.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		RootServer.getTimestampLog().addTimestamp("seb", true);
		RootServer.getTimestampLog().addTimestamp("benji", true);
		RootServer.getTimestampLog().addTimestamp("arvid", true);
		RootServer.getTimestampLog().addTimestamp("seb", false);
		RootServer.getTimestampLog().addTimestamp("benji", false);
		RootServer.getTimestampLog().addTimestamp("arvid", false);
		RootServer.main(args);
		test("search=seb&Suc%ben%arv&suc");
	}
}
