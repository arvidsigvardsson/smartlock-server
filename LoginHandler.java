import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.util.*;

/**
* Hanterar endpointen /login, svarar alltid "Login successful" och statuskod 200, men kallas endast om klienten blir 
* godkänd av en authenticator
*
* @author Arvid Sigvardsson
*/
public class LoginHandler implements HttpHandler {
	public void handle(HttpExchange ex) throws IOException {
		System.out.println("Inloggning sker");
		String response = "Login successful";
		// koden 200 innebär lyckad request
		ex.sendResponseHeaders(200, response.length());
		OutputStream os = ex.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
}