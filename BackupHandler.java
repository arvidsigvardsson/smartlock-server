import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class BackupHandler implements HttpHandler {
	public void handle(HttpExchange ex) throws IOException {
		System.out.println("Http request received, detta är query: " + ex.getRequestURI().getQuery());
		System.out.println("Detta är request method: " + ex.getRequestMethod());
/*
 * getBackupList
 * loadBackup
 * 
 * */
		
		String response = "";
		String query = ex.getRequestURI().getQuery();
		String[] queries = queryToQueries(query);/* Accepted types */
		if (queries[0].equals("list")
				&& queries[1].length() > 0) {/* Accepted Commands */
				String[] backups = RootServer.getUserContainer().getBackupsList();
				for(String elem:backups){
					response += elem;
				}
			System.out.println("Sending list of userContainer backups available for loading.\n");
			System.out.println("RESPONSE:"+response);
		} else if(queries[0].equals("load")&& queries[1].length() > 0){
			String[] bFiles = RootServer.getUserContainer().getBackupsList();
			for(String elem:bFiles){
				if(elem.equals(queries[1])){
					RootServer.getUserContainer().loadBackup(queries[1]);
					System.out.println("Loading a backup userContainer file.\n");
					response = "Backup file found & loaded\n";
				}else{
					response = "Backup file couldn't be found\n";
				}
			}
			System.out.println("RESPONSE: "+response);
			
			
		}
		
		else {
			System.out.println("Invalid query type");
			response = RootServer.getLog();
		}
		ex.sendResponseHeaders(200, response.length());
		OutputStream os = ex.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

	public static String[] queryToQueries(String query) {
		return query.split("=");
	}

	public static void main(String[] args) {
	
	}
}
