import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

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

	public void sendAdminPushNotification() {
		System.out.println("Nu ska en pushnotis om adminlistan skickas ut");
		String json = makeJsonPushObject();

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

	private String makeJsonPushObject() {
		// return "{\"to\":\"eeo-s16-1vg:APA91bFcKOQ0UrAf4f8e7SOkysDM_78gIlJunBoq4yFw5KooiSKMIzEiUMbELplw7ksO0Dz4Lft9Ekga47SLH9It_eKG-DgnDN7KKA52LyAzzscSOUMkZ0b9oiHVWnbbvq3HpyEN32n7\",\"data\": {\"score\":\"3x1\"}}";
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonPushData jpd = new JsonPushData();
			jpd.setMessage("New data on server");
			JsonPush jp = new JsonPush();
			jp.setTo(tokens.get(0));
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