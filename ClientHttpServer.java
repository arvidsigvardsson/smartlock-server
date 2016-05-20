import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* ClientHttpServer hanterar alla inkommande httprequest till servern. En HttpServer instansieras och sätts att lyssna på * angiven port, i vårt fall port 8080, inte port 80 som är standard eftersom vi använder en proxyserver som 
* vidarebefordrar https-trafik till vår server.
* De olika HttpContexterna motsvarar olika endpoints där servern kan kontaktas, och de startar en tråd av en handler för
* varje inkommande request.
*
*/
public class ClientHttpServer implements Runnable {
	private int port;
	
	public ClientHttpServer(int port) {
		this.port = port;
	}
	/**
	* metod som kallas av systemet när klassen körs som tråd
	*/
	public void run() {
		try {
			// instansierar en httpServer
			HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
			
			// endpoint för mobilklienter för att ansluta och signalera till låset att det ska öppnas
			HttpContext uContext = server.createContext("/client", new ClientHttpHandler());
			// endpoint för klienter att hämta och posta info om godkända rfid-kort, samt hantera deras namn
			HttpContext aContext = server.createContext("/admin", new ClientAdminHandler());
			// endpoint för klienter att testa om de kan logga in
			HttpContext loginContext = server.createContext("/login", new LoginHandler());
			// endpoint för klienter att meddela sina pushnotistokens
			HttpContext pushtokensContext = server.createContext("/pushtokens", new PushTokensHandler());
			// endpoint för klienter att hämta loglistor
			HttpContext logContext = server.createContext("/log", new LogHandler());
			
			// anger hur många trådar servern kan skapa. Oklart vad som är rätt mängd, men 30 har räckt än så länge. Om
			// vi skulle bygga ett kommerciellt system skulle det naturligtvis krävas fler trådar
			server.setExecutor(Executors.newFixedThreadPool(30));

			// instansierar objekt för basic authentication, som gör så att klienter behöver ange användarnamn och 
			// lösenord vid anslutning
			UserAuthentication uAuth = new UserAuthentication("Logga in med ditt anvandarnamn och losenord.");
			uContext.setAuthenticator(uAuth);
			loginContext.setAuthenticator(uAuth);
			logContext.setAuthenticator(uAuth);
			//BYTA MOT ADMIN AUTHENTICATION SENARE (VID BEHOV):
			UserAuthentication aAuth = new UserAuthentication("Logga in med ditt anvandarnamn och losenord.");
			aContext.setAuthenticator(aAuth);
			pushtokensContext.setAuthenticator(uAuth);

			// startar servern
			server.start();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}