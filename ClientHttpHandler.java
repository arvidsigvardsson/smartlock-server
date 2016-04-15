import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.util.*;

public class ClientHttpHandler implements HttpHandler {
	public void handle(HttpExchange ex) throws IOException {
		System.out.println("Http request received, detta är query: " + ex.getRequestURI().getQuery());
		String response = "";
		Map<String, String> params = queryToMap(ex.getRequestURI().getQuery());
		if ("open".equals(params.get("message"))) {
			RootServer.setOpenStatus(true);
			response = "Opening the lock";
		} else {
			response = "Server received your message\n";
		}

		ex.sendResponseHeaders(200, response.length());
		OutputStream os = ex.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

	public static Map<String, String> queryToMap(String query){
    Map<String, String> result = new HashMap<String, String>();
    for (String param : query.split("&")) {
        String pair[] = param.split("=");
        if (pair.length>1) {
            result.put(pair[0], pair[1]);
        }else{
            result.put(pair[0], "");
        }
    }
    return result;
  }

}