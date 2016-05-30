import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.io.OutputStreamWriter;

import java.util.ArrayList;

import java.net.URL;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;

/**
 * Klassen PushNotifier skickar pushnotiser till androidklienter via Googles GCM-service. 
 * Detta görs genom att göra ett POST-request till en GCM-server. I det requestet krävs en
 * api-nyckel, som är lagrad i en textfil, samt en token till varje klient som vill ta 
 * emot notiser, också lagrade i en textfil. Klienter registrerar sig via endpointen 
 * /pushtokens som hanteras av klassen PushTokensHandler, som anropar denna klass via
 * metoden addToken. Till requestet skapas ett jsonobjekt innehållande tokens samt annan 
 * info om notisen. Beroende på om det är en synlig eller tyst notis som skickas används 
 * metoderna makeJsonPushObject eller makeJsonPushObjectWithNotification
 */
public class PushNotifier {
	private String tokensFilePath;
	private String apiKey;
	private ArrayList<String> tokens;

	/**
	 * Konstruerar ett objekt med filnamn till filer med api-nyckel och tokens
	 *
	 * @param apiFilePath fil med api-nyckel
	 * @param tokensFilePath fil med tokens
	 */
	public PushNotifier(String apiFilePath, String tokensFilePath) {
		this.tokensFilePath = tokensFilePath;

		readApiKey(apiFilePath);
		readTokens(tokensFilePath);
	}

	/**
	 * skickar ut en synlig notis med meddelandet att dörren varit öppen en viss angiven
	 * tid
	 *
	 * @param timeOpen den tid som ska visas i meddelandet
	 */
	public void sendDoorOpenPush(int timeOpen) {
		System.out.println("Ska skicka dörr öppen push");
		String json = makeJsonPushObjectWithNotification(timeOpen);
		// System.out.println("Detta är json: " + json);
		sendPushNotification(json);
	}

	/**
	 * skickar ut en tyst notis om att listan med RFID-kort har uppdaterats, så att 
	 * klienter kan liveuppdateras
	 */
	public void sendAdminPushNotification() {
		// System.out.println("Nu ska en pushnotis om adminlistan skickas ut");
		// String json = makeJsonPushObject("Change to card id data on server");
		// sendPushNotification(json);
	}
	
	/**
	 * skickar ut en tyst notis om att loglistan har uppdaterats, så att klienter kan 
	 * liveuppdateras
	 */
	public void sendLogUpdatePush() {
		// System.out.println("Nu ska en pushnotis om loglistan skickas ut");
		// String json = makeJsonPushObject("Change to log list on server");
		// sendPushNotification(json);
	}

	/**
	 * skickar ut en tyst notis om att dörren är öppen, så att klienter kan ändra dörrikon
	 * i ui
	 */
	public void sendSilentDoorOpenPush() {
		System.out.println("Nu ska en tyst notis att dörren är öppen skickas ut");
		String json = makeJsonPushObject("opened");
		sendPushNotification(json);
	}

	/**
	 * skickar ut en tyst notis om att dörren är stängd, så att klienter kan ändra 
	 * dörrikon i ui
	 */
	public void sendSilentDoorClosedPush() {
		System.out.println("Nu ska en tyst notis att dörren är stängd skickas ut");
		String json = makeJsonPushObject("closed");
		sendPushNotification(json);
	}

	// metod för att göra pushnotisrequest, tar json som argument
	private void sendPushNotification(String json) {
		try {
			URL url = new URL("https://gcm-http.googleapis.com/gcm/send");
			HttpURLConnection connection = (HttpsURLConnection) url.openConnection();
			// api-nyckel krävs i Authorization-headern
			connection.setRequestProperty("Authorization", "key=" + apiKey);
			connection.setRequestProperty("Content-type", "application/json");
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept-Charset", "UTF-8");

			// skriver json som http-body
			DataOutputStream os = new DataOutputStream(connection.getOutputStream());
			os.write(json.getBytes());
			os.flush();
			os.close();

			// läser in svar från GCM-servern. Tidigare använde vi svaret i debugsyfte och
			// skrev ut det i konsollen, nu ignorerar vi det, och skriver bara ut 
			// statuskoden
			InputStream response = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(response));
			String responseStr = "";
			String input;
			while ((input = br.readLine()) != null) {
				responseStr += input;
			}
			
			System.out.println("Statuskod: " + connection.getResponseCode());
			connection.disconnect();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * metod för att lägga till tokens, anropas av PushTokensHandler
	 */
	public void addToken(String token) {
		// kollar så att token inte redan finns
		if (!tokens.contains(token)) {
			tokens.add(token);
		}
		// spara till textfil
		writeTokensToDisk();
	}

	// sparar tokens till textfil
	private void writeTokensToDisk() {
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tokensFilePath),"UTF-8"));

			for (String token : tokens) {
				bw.write(token);
				bw.newLine();
			}

			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// skapar ett jsonobjekt som används till notisen
	private String makeJsonPushObject(String message) {
		try {
			// skapar javaobjekt som sedan ska konverteras till jsonobjekt
			JsonPushData jpd = new JsonPushData();
			jpd.setMessage(message);
			jpd.setSilent(true);
			JsonPush jp = new JsonPush();

			// sätta lista med registration ids
			jp.setRegistration_ids(tokens);
			jp.setData(jpd);
			
			// konverterar javaobjekt till jsonobjekt
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(jp);
			return json;
		} catch (IOException e) {
			System.out.println(e);
			return null;
		}
	}

	// skapar ett jsonobjekt som även innehåller info om hur en synlig notis ska visas upp
	// för användare av mobilklient
	private String makeJsonPushObjectWithNotification(int timeOpen) {
		try {
			// skapar javaobjekt som sedan ska konverteras till jsonobjekt
			JsonPushData jpd = new JsonPushData();
			String msg = "Din dörr har varit öppen i " + timeOpen + " sekunder";

			JsonPushNotificationField jpnf = new JsonPushNotificationField("Ny notis från Lockdroidservern", msg, "icon");

			jpd.setMessage(msg);
			jpd.setSilent(false);
			JsonPushWithNotificationField jp = new JsonPushWithNotificationField();

			// sätta lista med registration ids
			jp.setRegistration_ids(tokens);
			jp.setData(jpd);
			jp.setNotification(jpnf);

			// konverterar javaobjekt till jsonobjekt
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(jp);
			return json;
		} catch (IOException e) {
			System.out.println(e);
			return null;
		}
	}
	
	// läser in api-nyckel från textfil
	private void readApiKey(String apiFilePath) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(apiFilePath)));
			apiKey = br.readLine();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	//läser in tokens från textfil
	private void readTokens(String tokensFilePath) {
		tokens = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(tokensFilePath)));
			String input;
			while((input = br.readLine()) != null) {
				tokens.add(input);
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}