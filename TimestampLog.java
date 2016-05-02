
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

/**
 * En log som samlar Timestamp objekt i en ArrayList. Klassen har funktionalitet
 * för att hämta listan som en sträng, som en sträng-array och även
 * funktionalitet för att endast hämta Timestamp objekt som innehåller vissa
 * karaktärer t.ex. "29/04/2016". Listan lagras även i en textfil med namnet "timestampLog.txt".
 * Vid start läses alla tidstämplar in från den text filen och lagras i klassens ArrayList<Timestamp>.
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
	 * Läser in sparade tidsstämplar från timestampLog.txt och lagrar de i klassens ArrayList<Timestamp>.
	 * @param filename
	 * @throws IOException
	 */
	private void readFile(String filename) throws IOException{
		try{
			bReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		}catch(FileNotFoundException e){
			System.out.println("Filen fanns inte.");
			return;
		}

		String input;
		String[] data;
		//seb 29/4/2016 03:57:18 succeeded
		while((input = bReader.readLine()) != null){
			data = input.split(" ");
			if(data.length==4){
				log.add(new Timestamp(data));
			}else{
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
		return toString(" ");
	}

	/**
	 * Returnerar log med sökta element, i sträng-format.
	 * 
	 * @param only
	 *            Sträng som anger sökta tidsstämplarna
	 * @return str loggen i sträng-format
	 */
	public String toString(String only) {
		String str = "";
		String temp = "";
		Iterator<Timestamp> iter = log.iterator();
		while (iter.hasNext()) {
			temp = iter.next().getTimestamp();
			if (temp.contains(only)) {
				str += "\n" + temp;
			}
		}
		if (str.length() > 0) {
			return str;
		} else {
			System.out.println("Fanns inget i loggen som matchade sökningen");
			return str;
		}

	}

	/**
	 * Returnerar loggens storlek. Tidsstämple med information om när loggen
	 * skapades räknas inte in.
	 * 
	 * @return int loggens storlek
	 */
	public int getLogSize() {
		return getLogSize(" ");
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
		int size = 0;
		String temp;
		Iterator<Timestamp> iter = log.iterator();
		while (iter.hasNext()) {
			temp = iter.next().getTimestamp();
			if (temp.contains(only)) {
				size++;
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
		return getLog(" ");
	}

	/**
	 * Returnerar en log med endast sökta element, som en sträng-array.
	 * 
	 * @param only
	 *            Sökta element t.ex. "seb" eller "29/04/2016".
	 * @return String[] Hela loggen som en sträng-array.
	 */
	public String[] getLog(String only) {
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

	/**
	 * Test metod för att testa att klassen fungerar.
	 * 
	 * @param args
	 * @throws InterruptedException
	 *             Behövs för Thread.sleep anropet
	 * @throws IOException
	 */
	public static void main(String[] args) throws InterruptedException, IOException {

		TimestampLog t = new TimestampLog("filer/timestampLog.txt");
		System.out.println("LOG SIZE: " + t.getLogSize());
		System.out.println(t.getCreated());
		//t.addTimestamp("seb", true);
		//t.addTimestamp("adam", false);
		t.addTimestamp("erik", true);
		// System.out.println("LOG SIZE: " + t.getLogSize());
		System.out.println("LOG LISTING: " + t.toString());

		// Thread.sleep(1000);
		// t.addTimestamp("erik", true);
		// System.out.println("LOG SIZE: " + t.getLogSize());
		// System.out.println("TOSTRING: " + t.toString());
		// t.addTimestamp("hadi", true);
		// t.addTimestamp("arvid", true);
		// System.out.println("LOG SIZE: " + t.getLogSize());
		// System.out.println("LOG LISTING: ");
		// String[] log = t.getLog("seb");
		// for (String s : log) {
		// System.out.println(s);
		// }
		// System.out.println("STRING ARRAY SIZE: " + log.length);
		// System.out.println("LOG SIZE: " + t.getLogSize());
		// System.out.println("END");
		// System.out.println("START TEST 2");
		// System.out.println(t.toString());

	}

}
