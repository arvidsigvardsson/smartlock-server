import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import javax.swing.JOptionPane;

/**
 * Lagrar id:n och derast status (giltig/ogiltig) i en HashMap<String,Boolean>.
 * Klassen läser och skriver även till en text fil där all data lagras.
 *
 * @author Sebastian, tillägg av Arvid
 *
 */

public class DataContainer {
	private HashMap<String, Boolean> acceptanceMap = new HashMap<String, Boolean>();
	private String filename;
	private BufferedReader bReader;
	private HashMap<String, String> idNameMap = new HashMap<String, String>();
	private String idNameMapFileName;

	public DataContainer(String filename, String idNameMapFileName) {
		this.filename = filename;
		this.idNameMapFileName = idNameMapFileName;
		try {
			readFile(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			readIdNameMapFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void readIdNameMapFile() throws IOException {
		try {
			bReader = new BufferedReader(new InputStreamReader(new FileInputStream(idNameMapFileName)));
		} catch (FileNotFoundException e) {
			System.out.println("Filen fanns inte. (DataContainer, readIdNameMapFile)");
			return;
		}

		String input;
		String key;
		String value;
		String[] data;

		while ((input = bReader.readLine()) != null) {
			data = input.split(",");
			key = data[0];
			value = data[1];
			idNameMap.put(key, value);
		}
		bReader.close();
	}

	public HashMap<String, String> getIdNameMap() {
		return this.idNameMap;
	}

	/**
	 * Läser in datan i en textfil till klassens instans HashMap. Inläst data
	 * parse:as så att ID lagras som nyckel och Status lagras som värde.
	 *
	 * @param filename
	 *            Fil där data ska läsas in från
	 * @throws IOException
	 *             Kastas om filen inte finns
	 */
	private void readFile(String filename) throws IOException {
		try {
			bReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		} catch (FileNotFoundException e) {
			System.out.println("Filen fanns inte. (DataContainer, readFile)");
			return;
		}

		String input;
		String key;
		Boolean value;
		String[] data;

		while ((input = bReader.readLine()) != null) {
			data = input.split(",");
			key = data[0];
			value = data[1].equals("true");
			acceptanceMap.put(key, value);
		}
		bReader.close();
	}

	/**
	 * Då ett kort blip:as på RFID-läsaren anropas denna metod för att lägga
	 * till ID:t till den interna HashMap:en med värdet false om det var första
	 * gången ID:t registrerades samt en tidstämpel. Om ID:t redan är
	 * registrerat skapas endast en tidstämpel.
	 *
	 * @param id
	 *            Kort-id
	 * @throws IOException
	 *             Kastas om fil inte hittas.
	 */
	public void blip(String id) throws IOException {
		String user = id;
		System.out.println("Incoming id is: ");
		System.out.println("START");
		System.out.println(id);
		System.out.println("END");
		/*
		 * Användas för vidare funktionalitet, Servern kan lägga på data på id:T
		 */
		System.out.println("BLIP REGISTERED");
		if(this.getIdNameMap().containsKey(id)){
			user = this.getIdNameMap().get(id); /*Om där finns ett namn mappat till kortets ID, använd den för tidstämpeln*/
		}
		if (!acceptanceMap.containsKey(id)) {/* OM ID INTE FINNS */
			addToAcceptanceMap(id, false);/* LÄGG TILL ID */
			RootServer.getTimestampLog().addTimestamp(id,
					false);/* LOGGA FÖRSÖK */

		} else if (acceptanceMap.containsKey(id)
				&& acceptanceMap.get(id)) {/* OM ID FINNS & GODKÄND */
			RootServer.getTimestampLog().addTimestamp(user,
					true);/* LOGGA LYCKAD */
		} else { /* OM ID FINNS MEN INTE GODKÄND, LOGGA EJLYCKAD */
			RootServer.getTimestampLog().addTimestamp(user, false);
		}
	}

	/**
	 * Ersätter klassens HashMap med den som anges som parameter. Sedan skrivs
	 * den till text filen.
	 *
	 * @param update
	 *            Den nya HashMapen som ska ersätta den befintliga.
	 * @throws IOException
	 */
	public void updateAcceptanceMap(HashMap<String, Boolean> update) throws IOException {
		this.acceptanceMap = update;
		sendAdminPush(); // testar pushnotiser
		write();
	}

	public void saveIdNameMapToDisk() {
		try {
			BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(idNameMapFileName), "UTF-8"));

			for (Entry<String, String> entry : idNameMap.entrySet()) {
				bw.write(entry.getKey() + "," + entry.getValue());
				bw.newLine();
			}

			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setIdNameMap(HashMap<String, String> map) {
		this.idNameMap = map;
		// sendAdminPush(); // testar pushnotiser
		saveIdNameMapToDisk();
	}

	/**
	 * Skriver från HashMapen till filen.
	 *
	 * @throws IOException
	 *             Kastas om filen som data lagras i inte hittas.
	 */
	public void write() throws IOException {
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<Boolean> values = new ArrayList<Boolean>();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
		String key;
		Boolean value;
		Set<String> keySet = acceptanceMap.keySet();
		Iterator<String> iterKeys = keySet.iterator();
		while (iterKeys.hasNext()) {
			key = iterKeys.next();
			bw.write(key + ",");
			keys.add(key);
			value = acceptanceMap.get(key);
			values.add(value);
			bw.write(value.toString() + ",");
			bw.newLine();
		}
		bw.close();
	}

	/**
	 * Lägger till ett ID (nyckel) och en Status (värde) till klassens HashMap
	 * och uppdaterar sedan filen med hjälp av write-metoden.
	 *
	 * @param key
	 *            Kort-id
	 * @param value
	 *            Status (giltigt/ogiltig)
	 * @throws IOException
	 *             Kastas om filen som data lagras i inte hittas.
	 */
	public void addToAcceptanceMap(String key, Boolean value) throws IOException {
		this.acceptanceMap.put(key, value);
		sendAdminPush(); // testar pushnotiser
		write();
	}

	/**
	 * Returnerar klassens HashMap innehållandes alla ID:n (nycklar) och
	 * tillhörande status för varje ID (värden).
	 *
	 * @return acceptanceMap HashMap med ID och Status
	 */
	public HashMap<String, Boolean> getAcceptanceList() {
		return this.acceptanceMap;
	}

	/**
	 * Returnerar alla ID:n och dess statusar i en sträng anpassad för att
	 * mottas av Arduinot så att den kan uppdatera sin interna lista.
	 *
	 * @return String Lista med alla ID:n och statusar.
	 */
	public String getAcceptanceListArdu() {
		String str = "l";
		// System.out.println(acceptanceMap.keySet());
		Iterator<String> iter = acceptanceMap.keySet().iterator();
		while (iter.hasNext()) {
			String id = iter.next();
			// System.out.println(id);
			// System.out.println(acceptanceMap.get(id));
			if (acceptanceMap.get(id)) {
				str += id + ",";
			}
		}
		return str + "\n";
	}

	/**
	 * Testmetod för att testa att klassen fungerar. Denna metod exekveras från
	 * userAuthentication-klassen som hämtar ett DataContainer objekt som
	 * instansierats i RootServer klassen. Metoden anropas i början av
	 * checkCredentials-metoden i userAuthentication klassen.
	 */
	public void test() {
		String input = "";
		while (!(input.equals("0") || input.equals("exit"))) {
			input = JOptionPane.showInputDialog(
					"\n*************************************\n*****^^^TEST PROGRAM^^^*****\n*************************************\n1. getAcceptanceListArdu\n2. getAcceptanceList\n3. blip\n4. addToAcceptanceMap\n5. updateAcceptanceMap\n*************************************\nEnter \"0\" or \"exit\" to exit.\n************************************* ");
			switch (input) {

			case "1":
				System.out.println("RUNNING: getAcceptanceListArdu");
				JOptionPane.showMessageDialog(null, this.getAcceptanceListArdu());
				System.out.println("END");
				break;

			case "2":
				System.out.println("RUNNING: getAcceptanceList");
				HashMap<String, Boolean> map = this.getAcceptanceList();
				String res = "";
				Iterator<String> keyIter = map.keySet().iterator();
				while (keyIter.hasNext()) {
					String key = keyIter.next();
					res += "\n" + key + "  " + map.get(key).toString();
				}
				JOptionPane.showMessageDialog(null, res);
				System.out.println("END");
				break;

			case "3":
				System.out.println("RUNNING: blip");
				String id = JOptionPane.showInputDialog("Kortets ID");
				try {
					blip(id);
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("END");
				break;

			case "4":
				System.out.println("RUNNING: addToAcceptanceMap");
				String key = JOptionPane.showInputDialog("ID");
				Boolean value = Boolean.parseBoolean(JOptionPane.showInputDialog("true eller false"));
				try {
					this.addToAcceptanceMap(key, value);
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("END");
				break;

			case "5":
				System.out.println("RUNNING: updateAcceptanceMap");
				HashMap<String, Boolean> map2 = new HashMap<String, Boolean>();
				map2.put("111", true);
				map2.put("222", true);
				map2.put("333", false);
				try {
					this.updateAcceptanceMap(map2);
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("END");
				break;
			}

		}
	}

	public static void main(String[] args) throws IOException {
		DataContainer dc = new DataContainer("filer/text.txt", "filer/testavidnamn.txt");

		System.out.println("idNameMap: " + dc.getIdNameMap());
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("666", "Number of the beast");
		dc.setIdNameMap(map);
		System.out.println("idNameMap: " + dc.getIdNameMap());

		// System.out.println(dc.getAcceptanceListArdu());
		// HashMap<String,Boolean> map = new HashMap<String,Boolean>();
		// map.put("asdasd1", false);
		// map.put("asdsa2", true);
		// map.put("asddad3", false);
		// map.put("aresr4", false);
		// map.put("dawwa5", true);
		// map.put("wwew6", false);
		// dc.updateAcceptanceMap(map);
		// dc.blip("id");
		// System.out.println(dc.getAcceptanceListArdu());
		// dc.addToAcceptanceMap("bbbb4", true);
		// dc.addToAcceptanceMap("ccccc5", false);
		// System.out.println(dc.getAcceptanceListArdu());
	}

	public void sendAdminPush() {
		RootServer.getPushNotifier().sendAdminPushNotification();

		// för iOS
		RootServer.setIosPushMessage(new LongPollingPushMessage("Change to card id data on server", 2));	}
}
