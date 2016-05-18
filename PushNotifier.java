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

public class PushNotifier {
	private String tokensFilePath;
	private String apiKey;
	private ArrayList<String> tokens;

	public PushNotifier(String apiFilePath, String tokensFilePath) {
		this.tokensFilePath = tokensFilePath;

		readApiKey(apiFilePath);
		readTokens(tokensFilePath);
	}

	public void sendDoorOpenPush() {
		System.out.println("Ska skicka dörr öppen push");
		String json = makeJsonPushObjectWithNotification();
		System.out.println("Detta är json: " + json);
		sendPushNotification(json);
	}

	public void sendAdminPushNotification() {
		System.out.println("Nu ska en pushnotis om adminlistan skickas ut");
		String json = makeJsonPushObject("Change to card id data on server");
		sendPushNotification(json);
	}

	public void sendLogUpdatePush() {
		System.out.println("Nu ska en pushnotis om loglistan skickas ut");
		// String json = makeJsonLogPushObject();
		String json = makeJsonPushObject("Change to log list on server");
		sendPushNotification(json);
	}

	public void sendSilentDoorOpenPush() {
		System.out.println("Nu ska en tyst notis att dörren är öppen skickas ut");
		String json = makeJsonPushObject("opened");
		sendPushNotification(json);
	}

	public void sendSilentDoorClosedPush() {
		System.out.println("Nu ska en tyst notis att dörren är stängd skickas ut");
		String json = makeJsonPushObject("closed");
		sendPushNotification(json);
	}

	private void sendPushNotification(String json) {
		// System.out.println("Nu ska en pushnotis om adminlistan skickas ut");
		// String json = makeJsonPushObject();

		try {
			URL url = new URL("https://gcm-http.googleapis.com/gcm/send");
			HttpURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestProperty("Authorization", "key=" + apiKey);
			connection.setRequestProperty("Content-type", "application/json");
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept-Charset", "UTF-8");

			DataOutputStream os = new DataOutputStream(connection.getOutputStream());
			os.write(json.getBytes());
			os.flush();
			os.close();

			InputStream response = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(response));
			String responseStr = "";
			String input;
			while ((input = br.readLine()) != null) {
				responseStr += input;
			}
			System.out.println("Svar: " + responseStr);

			System.out.println("Statuskod: " + connection.getResponseCode());
			connection.disconnect();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public void addToken(String token) {
		// kollar så att token inte redan finns
		if (!tokens.contains(token)) {
			tokens.add(token);
		}
		// spara till textfil
		writeTokensToDisk();
	}

	public void writeTokensToDisk() {
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tokensFilePath),"UTF-8"));

			// for (Entry<String, String> entry : idNameMap.entrySet()) {
			// 	bw.write(entry.getKey() + "," + entry.getValue());
			// 	bw.newLine();
			// }

			for (String token : tokens) {
				bw.write(token);
				bw.newLine();
			}

			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String makeJsonPushObject(String message) {
		// return "{\"to\":\"eeo-s16-1vg:APA91bFcKOQ0UrAf4f8e7SOkysDM_78gIlJunBoq4yFw5KooiSKMIzEiUMbELplw7ksO0Dz4Lft9Ekga47SLH9It_eKG-DgnDN7KKA52LyAzzscSOUMkZ0b9oiHVWnbbvq3HpyEN32n7\",\"data\": {\"score\":\"3x1\"}}";
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonPushData jpd = new JsonPushData();
			jpd.setMessage(message);
			jpd.setSilent(true);
			JsonPush jp = new JsonPush();

			// sätta lista med registration ids
			jp.setRegistration_ids(tokens);
			// jp.setTo(tokens.get(0));
			// jp.setData(mapper.writeValueAsString(jpd));

			jp.setData(jpd);
			String json = mapper.writeValueAsString(jp);
			System.out.println("Json: " + json);
			return json;
		} catch (IOException e) {
			System.out.println(e);
			return null;
		}
	}

	private String makeJsonLogPushObject() {
		// return "{\"to\":\"eeo-s16-1vg:APA91bFcKOQ0UrAf4f8e7SOkysDM_78gIlJunBoq4yFw5KooiSKMIzEiUMbELplw7ksO0Dz4Lft9Ekga47SLH9It_eKG-DgnDN7KKA52LyAzzscSOUMkZ0b9oiHVWnbbvq3HpyEN32n7\",\"data\": {\"score\":\"3x1\"}}";
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonPushData jpd = new JsonPushData();
			jpd.setMessage("Change to log list on server");
			JsonPush jp = new JsonPush();

			// sätta lista med registration ids
			jp.setRegistration_ids(tokens);
			// jp.setTo(tokens.get(0));
			// jp.setData(mapper.writeValueAsString(jpd));

			jp.setData(jpd);
			String json = mapper.writeValueAsString(jp);
			System.out.println("Json: " + json);
			return json;
		} catch (IOException e) {
			System.out.println(e);
			return null;
		}
	}

	private String makeJsonPushObjectWithNotification() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonPushData jpd = new JsonPushData();
			JsonPushNotificationField jpnf = new JsonPushNotificationField("Ny notis från Lockdroidservern", "Dörren är öppen", "icon");

			jpd.setMessage("Door is open");
			jpd.setSilent(false);
			// JsonPush jp = new JsonPush();
			JsonPushWithNotificationField jp = new JsonPushWithNotificationField();

			// sätta lista med registration ids
			jp.setRegistration_ids(tokens);
			// jp.setTo(tokens.get(0));
			// jp.setData(mapper.writeValueAsString(jpd));

			jp.setData(jpd);
			jp.setNotification(jpnf);

			String json = mapper.writeValueAsString(jp);
			// System.out.println("Json: " + json);
			return json;
		} catch (IOException e) {
			System.out.println(e);
			return null;
		}
	}

	private void readApiKey(String apiFilePath) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(apiFilePath)));
			apiKey = br.readLine();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

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

	public String toString() {
		String s = "";
		s += "Api key: " + apiKey + "\nTokens:\n";
		for(String token : tokens) {
			s = s + token + "\n";
		}
		return s;
	}

	public static void main(String[] args) {
		PushNotifier pn = new PushNotifier("filer/apikey.txt", "filer/pushtokens.txt");
		System.out.println(pn);
	}
}