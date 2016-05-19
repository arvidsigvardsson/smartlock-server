
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

/**
 * En log som samlar Timestamp objekt i en ArrayList. Klassen har funktionalitet
 * för att hämta listan som en sträng, som en sträng-array och även
 * funktionalitet för att endast hämta Timestamp objekt som innehåller vissa
 * karaktärer t.ex. "29/04/2016". Listan lagras även i en textfil med namnet
 * "timestampLog.txt". Vid start läses alla tidstämplar in från den text filen
 * och lagras i klassens ArrayList<Timestamp>.
 *
 * @author Admin
 *
 */
public class TimestampLog {
	private ArrayList<Timestamp> log = new ArrayList<Timestamp>();
	private Timestamp created;
	private String filename;
	private BufferedReader bReader;

	/**
	 * Konstruktor som skapar en ny tidstämpel logg.
	 *
	 * @param filename
	 *            namnet på txt filen där loggen även ska lagras.
	 */
	public TimestampLog(String filename) {
		created = new Timestamp("Log created", true);
		this.filename = filename;
		try {
			readFile(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Läser in sparade tidsstämplar från timestampLog.txt och lagrar de i
	 * klassens ArrayList<Timestamp>.
	 * 
	 * @param filename
	 * @throws IOException
	 */
	private void readFile(String filename) throws IOException {
		try {
			bReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		} catch (FileNotFoundException e) {
			System.out.println("Filen fanns inte. (TimestampLog, readFile)");
			return;
		}

		String input;
		String[] data;
		while ((input = bReader.readLine()) != null) {
			data = input.split(",");
			if (data.length == 4) {

				log.add(new Timestamp(data));
			} else {
				System.out.println("This row couldn't be read into the ArrayList. It doesn't follow the format.");
			}

		}
		bReader.close();
	}

	/**
	 * Skriver till text filen där alla tidstämplar också lagras.
	 *
	 * @throws IOException
	 *             Kastas om filen inte finns.
	 */
	public void write() throws IOException {
		// seb 29/4/2016 03:57:18 succeeded
		ArrayList<Timestamp> stamps = this.getTimestampList();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
		Iterator<Timestamp> iterStamps = stamps.iterator();
		while (iterStamps.hasNext()) {
			bw.write(iterStamps.next().getTimestamp());
			bw.newLine();
		}
		bw.close();
	}

	/**
	 * Skapar en tidsstämpel och lagrar den i loggen.
	 *
	 * @param user
	 *            Användarnamn
	 * @param success
	 *            Huruvida inloggningen lyckades
	 * @param write
	 *            Om tidstämpeln ska skrivas till lagrings text filen
	 * @throws IOException
	 *             Kastas om filen inte finns.
	 */
	public void addTimestamp(String user, boolean success, boolean write) {
		log.add(new Timestamp(user, success));

		// pushnotis om loglista ska skickas ut
		RootServer.getPushNotifier().sendLogUpdatePush();

		if (write) {
			try {
				write();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Skapar en tidstämpel och lagrar den i loggen samt skriver in den i
	 * lagrings text filen.
	 *
	 * @param user
	 *            Användarnamn
	 * @param success
	 *            Huruvida inloggningen lyckades
	 * @throws IOException
	 *             Kastas om filen inte finns.
	 */
	public void addTimestamp(String user, boolean success) {
		addTimestamp(user, success, true);
	}

	/**
	 * Returnerar logggen som en ArrayList<Timestamp>.
	 *
	 * @return log ArrayList<Timestamp> loggen
	 */
	public ArrayList<Timestamp> getTimestampList() {
		return this.log;
	}

	/**
	 * Returnerar en sträng med en tidsstämpel som innehåller information om när
	 * loggen skapades.
	 *
	 * @return created information om när loggen skapades
	 */
	public String getCreated() {
		return this.created.getTimestamp();
	}

	/**
	 * Returnerar hela loggen i sträng-format.
	 *
	 * @return str loggen i sträng-format
	 */
	public String toString() {
		return toString(",");
	}

	/**
	 * Returnerar listan av timestamps med sökta term/termer, i sträng-format.
	 * Fungerar med logiska uttryck "och"(&) och "eller"(%) för att stapla flera
	 * söktermer. T.ex. "seb&18/5%seb&19/5" kommer att returnera en sträng med
	 * timestamps som innehåller seb och datumen 18/5 och 19/5. Man kan högst
	 * använda sig av 3 del-söktermer per sökterm, t.ex. "seb&18/5&Suc" som
	 * skulle returnera alla timestamps som innehåller "seb" och "18/5" och
	 * "Suc". Metoden ser även till att inga dupliceringar av timestamps
	 * returneras i strängen.
	 * 
	 * @param only
	 *            Sträng med söktermer som anger sökta tidsstämplarna
	 * @return str listan av tidstämplar i sträng-format
	 */
	public String toString(String only) {
		if (only.length() == 0) {
			only = ",";
		}
		String res = "";
		ArrayList<String> duplicateTimestamps = new ArrayList<String>();
		/* För att mappa sökterm nr med del-söktermer */
		HashMap<Integer, String[]> searchStrings = new HashMap<Integer, String[]>();
		/* Om flera söktermer skickats in */
		if (only.contains("%")) {
			/* Lagra varje sökterm i ett element */
			String searches[] = only.split("%");
			/* FÖR varje sökterm */
			for (int i = 0; i < searches.length; i++) {
				/* OM söktermen består av del-söktermer */
				if (searches[i].contains("&")) {
					/* Lagra varje del-sökterm i ett element */
					String searchTerms[] = searches[i].split("&");
					/* Mappa söktermen med dess del-söktermer */
					searchStrings.put(i, searchTerms);
					/* OM söktermen inte består av del-söktermer */
				} else {
					/* Skapa en tom array */
					String arr[] = new String[] { searches[i] };
					/* Mappa söktermen till ett tomt element */
					searchStrings.put(i, arr);
				}
			}

			Iterator<Integer> keyIter = searchStrings.keySet().iterator();
			while (keyIter.hasNext()) {
				int theKey = keyIter.next();
				String resu = search(searchStrings.get(theKey));
				String tempArr[] = resu.split("¨");
				for (String str : tempArr) {
					if (!(duplicateTimestamps.contains(str))) {
						res += str;
						duplicateTimestamps.add(str);
					}
				}
			}
		} else if (only.contains("&")) {
			String searchTerms[] = only.split("&");
			String resu2 = search(searchTerms);

			String tempArr[] = resu2.split("¨");
			for (String str : tempArr) {
				if (!(duplicateTimestamps.contains(str))) {
					res += str;
					duplicateTimestamps.add(str);
				}
			}

		} else {
			String searchTerms[] = new String[] { only };
			String resu3 = search(searchTerms);
			String tempArr[] = resu3.split("¨");
			for (String str : tempArr) {
				if (!(duplicateTimestamps.contains(str))) {
					res += str;
					duplicateTimestamps.add(str);
				}
			}
		}
		if (res.length() > 0) {
			return res;
		} else {
			System.out.println("Fanns inget i loggen som matchade sökningen");
			return "";
		}

	}

	/**
	 * Den här metoden används av toString()-metoderna för att returnera en
	 * lista av timestamps i sträng format.
	 * 
	 * @param terms
	 *            Array av del-söktermer som bygger upp en sökterm.
	 * @return Sträng innehållandes alla timestamps med angivna del-söktermer.
	 */
	private String search(String[] terms) {
		String str = "";
		String temp = "";
		boolean infoMessage = false;
		Iterator<Timestamp> iter = log.iterator();
		// System.out.println(terms[0]+" "+terms[1]+" "+terms[2]);
		while (iter.hasNext()) {
			temp = iter.next().getTimestamp();
			// System.out.println(temp);
			if (terms.length == 3) {
				if (temp.contains(terms[0]) && temp.contains(terms[1]) && temp.contains(terms[2])) {
					str += "\n" + temp + "¨";
				}
			} else if (terms.length == 2) {
				if (temp.contains(terms[0]) && temp.contains(terms[1])) {
					str += "\n" + temp + "¨";
				}
			} else if (terms.length == 1) {
				if (temp.contains(terms[0])) {
					str += "\n" + temp + "¨";
				}
			} else if (terms.length > 3 && !infoMessage) {
				System.out.println("Du kan högst använda dig av 3 del-söktermer per sökterm, t.ex. \"seb&18/5&Suc\" ");
				infoMessage = true;
			}
		}
		if (str.length() > 0) {
			return str;
		} else {
			System.out.println("Fanns inget i loggen som matchade sökningen");
			return "";
		}

	}

	/**
	 * Returnerar loggens storlek. Tidsstämple med information om när loggen
	 * skapades räknas inte in.
	 *
	 * @return int loggens storlek
	 */
	public int getLogSize() {
		return getLogSize(":");
	}

	/**
	 * Returnerar antalet element i loggen med sökta karaktärer. T.ex. kan "seb"
	 * returnera storleken 5 även om hela loggens storlek är 20.
	 *
	 * @param only
	 *            element som avgör storlek
	 * @return size int Antal element av sök karaktär i loggen
	 */
	public int getLogSize(String only) {
		String terms[] = null;
		if (only.contains("&")) {
			terms = only.split("&");
		}
		int size = 0;
		String temp;
		Iterator<Timestamp> iter = log.iterator();
		while (iter.hasNext()) {
			temp = iter.next().getTimestamp();
			if (temp.contains(only)) {
				size++;
			}
			if (terms != null) {
				for (String elem : terms) {
					if (temp.contains(elem)) {
						size++;
					}
				}
			}

		}
		return size;
	}

	/**
	 * Returnerar loggen som en sträng-array.
	 *
	 * @return String[] Hela loggen som en sträng-array.
	 */
	public String[] getLog() {
		return getLog(":");
	}

	/**
	 * Returnerar en log med endast sökta element, som en sträng-array.
	 *
	 * @param only
	 *            Sökta element t.ex. "seb" eller "29/04/2016".
	 * @return String[] Hela loggen som en sträng-array.
	 */
	public String[] getLog(String only) {
		String searchTerms[] = null;
		if (only.contains("&")) {
			searchTerms = only.split("&");
		}
		String[] stampLog;
		String temp;
		int size = getLogSize(only);
		stampLog = new String[size];
		Iterator<Timestamp> iter = log.iterator();
		int i = 0;
		while (iter.hasNext()) {
			temp = iter.next().getTimestamp();
			if (temp.contains(only)) {
				stampLog[i] = temp;
				i++;
			}
			if (searchTerms != null) {
				for (String elem : searchTerms) {
					if (temp.contains(elem)) {
						stampLog[i] = temp;
						i++;
					}
				}
			}

		}
		if (i > 0) {
			return stampLog;
		} else {
			System.out.println("Fanns inget i loggen som matchade sökningen");
			return stampLog;
		}

	}

	/**
	 * Test metod som anropas från userAuthentication klassen. Testar alla
	 * metoder i TimestampLog i samband med systemet.
	 */
	public void test() {
		String input = "";
		while (!(input.equals("0") || input.equals("exit"))) {
			input = JOptionPane.showInputDialog(
					"\n*************************************\n*****^^^TEST PROGRAM^^^*****\n*************************************\n1. getCreated\n2. getLog\n3. getLog(String)\n4. getLogSize\n5. getLogSize(String)\n6. getTimestampList\n7. toString\n8. toString(String)\n*************************************\nEnter \"0\" or \"exit\" to exit.\n************************************* ");
			switch (input) {

			case "1":
				System.out.println("RUNNING: getCreated");
				JOptionPane.showMessageDialog(null, this.getCreated());
				System.out.println("END");
				break;

			case "2":
				System.out.println("RUNNING: getLog");
				String[] log = this.getLog();
				String res = "";
				for (String s : log) {
					res += "\n" + s;
				}
				JOptionPane.showMessageDialog(null, res);
				System.out.println("END");
				break;

			case "3":
				System.out.println("RUNNING: getLog(String)");
				String only = JOptionPane.showInputDialog("Mata in sträng som avgör vilka tidsstämplar som visas.");
				String[] log2 = this.getLog(only);
				String res2 = "";
				for (String s : log2) {
					res2 += "\n" + s;
				}
				JOptionPane.showMessageDialog(null, res2);
				System.out.println("END");
				break;

			case "4":
				System.out.println("RUNNING: getLogSize");
				JOptionPane.showMessageDialog(null, this.getLogSize());
				System.out.println("END");
				break;

			case "5":
				System.out.println("RUNNING: getLogSize(String)");
				String only2 = JOptionPane
						.showInputDialog("Mata in sträng så att antalet tidsstämplar med den infon visas.");
				JOptionPane.showMessageDialog(null, this.getLogSize(only2));
				System.out.println("END");
				break;

			case "6":
				System.out.println("RUNNING: getTimestampList");
				ArrayList<Timestamp> list = this.getTimestampList();
				Iterator<Timestamp> iter = list.iterator();
				String res3 = "";
				while (iter.hasNext()) {
					res3 += "\n" + iter.next().getTimestamp();
				}
				JOptionPane.showMessageDialog(null, res3);
				System.out.println("END");
				break;

			case "7":
				System.out.println("RUNNING: toString");
				JOptionPane.showMessageDialog(null, this.toString());
				System.out.println("END");
				break;

			case "8":
				System.out.println("RUNNING: toString(String)");
				String only3 = JOptionPane.showInputDialog("Mata in sträng som avgör vilka tidsstämplar som visas.");
				JOptionPane.showMessageDialog(null, this.toString(only3));
				System.out.println("END");
				break;
			}
		}
	}
}
