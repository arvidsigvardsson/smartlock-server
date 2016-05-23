import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JOptionPane;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
/**
 * http://localhost:8080/backup?load=lala.txt
 * http://localhost:8080/backup?list
 * @author Admin
 *
 */
public class BackupHandler implements HttpHandler {
	public void handle(HttpExchange ex) throws IOException {
		System.out.println("Http request received, detta är query: " + ex.getRequestURI().getQuery());
		System.out.println("Detta är request method: " + ex.getRequestMethod());

		String response = "";
		String query = ex.getRequestURI().getQuery();
		String[] queries = queryToQueries(query);/* Accepted types */

		if (queries[0].equals("list")) {/* Accepted Commands */
			// response = RootServer.getList();
			System.out.println("Sending list of userContainer backups available for loading.\n");
			System.out.println("RESPONSE:" + response);
		} else if (queries[0].equals("load") && queries[1].length() > 0) {
			String[] bFiles = RootServer.getUserContainer().getBackupsList();
			for (String elem : bFiles) {
				System.out.println("ELEM: "+elem  +"Comparing to searchTerm... "+queries[1]);
				if (elem.equals(queries[1])) {
					RootServer.getUserContainer().loadBackup(queries[1],"userBackups");
					System.out.println("Loading a backup userContainer file.\n");
					response = "Backup file found & loaded\n";
					break; /*Annars blir sista meddelandet "file couldn't be found" när nästa element jämförs*/
				} else {
					response = "Backup file couldn't be found\n";
				}
			}
		}

		else {
			System.out.println("Invalid query type");
		}
		ex.sendResponseHeaders(200, response.length());
		OutputStream os = ex.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

	public static String[] queryToQueries(String query) {
		return query.split("=");
	}

	public static void main(String[] args) throws IOException {
		new RootServer();
		/*PLACERA EN FIL I BACKUP MAPPEN FÖRRE INPUT*/
		String file = JOptionPane.showInputDialog("filnamnet","load=");
		String response = "";
		String queries[] = BackupHandler.queryToQueries(file);
		System.out.println("QUERIES_0:" + queries[0]);
		System.out.println("QUERIES_1:" + queries[1]);
		if (queries[0].equals("load") && queries[1].length() > 0) {
			System.out.println("QUERIES_0:" + queries[0]);
			System.out.println("QUERIES_1:" + queries[1]);
			String[] bFiles = RootServer.getUserContainer().getBackupsList();
			for (String elem : bFiles) {
				System.out.println("ELEM: "+elem  +"Comparing to searchTerm... "+queries[1]);
				if (elem.equals(queries[1])) {
					RootServer.getUserContainer().loadBackup(queries[1],"userBackups");
					System.out.println("Loading a backup userContainer file.\n");
					response = "Backup file found & loaded\n";
				} else {
					response = "Backup file couldn't be found\n";
				}
			}
		
	}
		System.out.print("RESPONESSESESSE: "+response);
	}}
