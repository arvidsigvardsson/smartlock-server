import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClientHttpServer implements Runnable {
	public void run() {
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
			HttpContext uContext = server.createContext("/client", new ClientHttpHandler());
			HttpContext aContext =server.createContext("/admin", new ClientAdminHandler());
			HttpContext loginContext = server.createContext("/login", new LoginHandler());
			HttpContext pushtokensContext = server.createContext("/pushtokens", new PushTokensHandler());
			HttpContext logContext = server.createContext("/log", new LogHandler());
			HttpContext backupConxtext = server.createContext("/backup", new BackupHandler());
			// testar push till iOS
			HttpContext iosPushContext = server.createContext("/iospush", new IosPushHandler());


			server.setExecutor(Executors.newFixedThreadPool(30));

			UserAuthentication uAuth = new UserAuthentication("Logga in med ditt anvandarnamn och losenord.");
			uContext.setAuthenticator(uAuth);

			loginContext.setAuthenticator(uAuth);
			logContext.setAuthenticator(uAuth);
			//BYTA MOT ADMIN AUTHENTICATION SENARE (VID BEHOV):
			UserAuthentication aAuth = new UserAuthentication("Logga in med ditt anvandarnamn och losenord.");
			//
			aContext.setAuthenticator(aAuth);

			pushtokensContext.setAuthenticator(uAuth);
			iosPushContext.setAuthenticator(uAuth);

			server.start();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}

