import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import javax.swing.JOptionPane;

/**
 * 
 * Skriver och läser användarnamn samt lösenord till en text-fil. Lagrar
 * användar uppgifter och har backupfunktionalitet. En mapp "filer" behöver
 * finnas i projektmappen. Resterande map (userBackups) och fil (userList.txt)
 * skapas automatiskt om de inte hittas.
 * 
 * @author Admin
 * 
 * 
 */

public class UserContainer {
	private HashMap<String, String> acceptanceMap = new HashMap<String, String>();
	private String filename;
	private BufferedReader bReader;
	private int backupLimit = 15;
	// private long timer;
	// private long backupTimeCheck = 0; /*
	// * i ms, skapar inte backup om inte
	// * denna tid gått sedan senaste backup
	// */

	/**
	 * Konstuktor som läser in data från angiven fil och lagrar datan i klassens
	 * egna HashMap. Gräns för antal backupfiler som ska göras och gräns för hur
	 * ofta backups får skapas, anges också i denna konstruktor.
	 * 
	 * @param filename
	 *            Filen där datan till instans HashMapen ska läsas in från.
	 * @param backupLimit
	 *            Gräns för antal backupfiler som görs innan äldsta backupfilen
	 *            börjar skrivas över.
	 * @param backupTimeCheck
	 *            gräns i millisekunder för hur ofta backups får skapas t.ex.
	 *            (1000*60*60) -> inte inom en timme efter en backupskrivning.
	 * @throws IOException
	 *             Kastas om backup mapp inte finns.
	 * 
	 */
	public UserContainer(String filename, int backupLimit, long backupTimeCheck) {
		this(filename);
		// this.backupLimit = backupLimit;
		// this.backupTimeCheck = backupTimeCheck;
	}

	/**
	 * Konstuktor som läser in data från angiven fil och lagrar datan i klassens
	 * egna HashMap. Initieras med standardvärdena backuplimit = 5,
	 * backupTimeCheck = 0.
	 * 
	 * @param filename
	 *            Filen där datan till instans HashMapen ska läsas in från.
	 * @throws IOException
	 *             Kastas om backup mapp inte finns.
	 */
	public UserContainer(String filename) {
		this.filename = filename;
		try {
			readFile(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean state = this.userBackupFolderExists();
		System.out.println("Does the userBackup folder exist?: " + state);
		// if (state) {
		// try {
		// System.out.println("Moving all existing backup files to a new
		// folder");
		// this.moveBackupFiles(1, false);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
	}

	/**
	 * Läser in datan i en textfil till klassens instans HashMap. Inläst data
	 * parse:as så att användarnamn lagras som nyckel och lösenord lagras som
	 * värde.
	 * 
	 * @param filename
	 *            Fil där data ska läsas in från
	 * @throws IOException
	 *             Kastas om filen inte finns
	 */
	private void readFile(String filename) throws IOException {
		readFile(filename, this.acceptanceMap);
	}

	/**
	 * Läser in datan i en textfil till angiven HashMap. Inläst data parse:as så
	 * att användarnamn lagras som nyckel och lösenord lagras som värde.
	 * 
	 * @param filename
	 *            Fil där data ska läsas in från
	 * @param acceptanceMap
	 *            HashMap där inläst data ska lagras
	 * @throws IOException
	 *             Kastas om filen inte finns
	 */

	private void readFile(String filename, HashMap<String, String> acceptanceMap) throws IOException {
		try {
			bReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		} catch (FileNotFoundException e) {
			System.out.println("Filen fanns inte. (UserContainer, readFile()");
			return;
		}

		String input;
		String key;
		String value;
		String[] data;

		while ((input = bReader.readLine()) != null) {
			data = input.split(",");

			try {
				key = data[0];

			} catch (ArrayIndexOutOfBoundsException ex) {
				System.out.println("Fel: Användarnamn tomt");
				break;
			}

			try {
				value = data[1];
			} catch (ArrayIndexOutOfBoundsException ex) {
				System.out.println("Fel: Lösenord tomt");
				break;
			}

			acceptanceMap.put(key, value);
		}
		bReader.close();
	}

	/**
	 * <html><b>OBS: Nycklar och värden behöver vara Hashkodade. Om inte använd
	 * istället updateAcceptanceMapNonHashedData-metoden.</b></html> Ersätter
	 * klassens HashMap med den som anges som parameter. Sedan skrivs den till
	 * text filen.
	 * 
	 * @param backup
	 *            true om backup fil ska skapas, false om inte.
	 * @param hashedUpdate
	 *            Den nya HashMapen som ska ersätta den befintliga. OBS Alla
	 *            nycklar och värden måste vara hashkodade. Om inte hashadkodade
	 *            använd istället updateAcceptanceMapNonHashedData-metoden.
	 * @throws IOException
	 *             Kastas om filen som datan skrivs till inte finns.
	 */
	public void updateAcceptanceMap(HashMap<String, String> hashedUpdate, boolean backup) throws IOException {
		this.acceptanceMap = hashedUpdate;
		write(backup);
	}

	/**
	 * Samma som updateAcceptanceMap(HashMap<String, String> hashedUpdate,
	 * boolean backup)-metoden men med backup satt till false.
	 * 
	 * @param hashedUpdate
	 *            Den nya HashMapen som ska ersätta den befintliga. OBS Alla
	 *            nycklar och värden måste vara hashkodade. Om inte hashadkodade
	 * @throws IOException
	 *             Kastas om filen som datan skrivs till inte finns.
	 */
	public void updateAcceptanceMap(HashMap<String, String> hashedUpdate) throws IOException {
		updateAcceptanceMap(hashedUpdate, false);
	}

	/**
	 * Samma som updateAcceptanceMapNonHashedData(HashMap<String, String>
	 * nonHashedUpdate, boolean backup)-metoden men med backup satt till false.
	 * 
	 * @param nonHashedUpdate
	 *            HashMap med användarnamn och lösenord som inte är hashkodade.
	 * @throws IOException
	 *             Kastas om filen som datan skrivs till inte finns.
	 */
	public void updateAcceptanceMapNonHashedData(HashMap<String, String> nonHashedUpdate) throws IOException {
		updateAcceptanceMapNonHashedData(nonHashedUpdate, false);
	}

	/**
	 * Tar emot en HashMap med nycklar och värden som inte är hashkodade.
	 * Hashkodar varje nyckel och värde och stoppar in en ny HashMap som sedan
	 * ersätter klassens egna HashMap. Filen som lagrar all data skrivs också
	 * över.
	 * 
	 * @param backup
	 *            true om backup fil ska skapas, false om inte.
	 * @param nonHashedUpdate
	 *            HashMap med användarnamn och lösenord som inte är hashkodade.
	 * @throws IOException
	 *             Kastas om filen som lagrar datan inte hittas.
	 */

	public void updateAcceptanceMapNonHashedData(HashMap<String, String> nonHashedUpdate, boolean backup)
			throws IOException {
		HashMap<String, String> hashedUpdate = new HashMap<String, String>();
		Iterator<String> values = nonHashedUpdate.values().iterator();
		Iterator<String> keys = nonHashedUpdate.keySet().iterator();
		while (keys.hasNext()) {
			hashedUpdate.put(keys.next().hashCode() + "", values.next().hashCode() + "");
		}
		updateAcceptanceMap(hashedUpdate, backup);
	}

	/**
	 * Skriver från HashMapen till filen. Gör även en backup.
	 * 
	 * @throws IOException
	 *             Kastas om filen som data lagras i inte hittas.
	 */
	public void write() throws IOException {
		write(true);
	}

	/**
	 * Skriver från HashMapen till filen.
	 * 
	 * @param backup
	 *            true om backup fil ska skapas, false om inte.
	 * @throws IOException
	 *             Kastas om filen som data lagras i inte hittas.
	 */
	public void write(boolean backup) throws IOException {
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
		String key;
		String value;
		Set<String> keySet = acceptanceMap.keySet();
		Iterator<String> iterKeys = keySet.iterator();
		while (iterKeys.hasNext()) {
			key = iterKeys.next();
			bw.write(key + ",");
			keys.add(key);
			value = acceptanceMap.get(key);
			values.add(value);
			bw.write(value.toString());
			bw.newLine();
		}
		bw.close();
		if (backup) {
			// backup(System.currentTimeMillis() - this.timer);
			backup();
		}
	}

	/**
	 * Lägger till ett användarnamn (nyckel) och ett lösenord (värde) till
	 * klassens HashMap och uppdaterar sedan filen med hjälp av write-metoden.
	 * 
	 * @param key
	 *            Användarnamn
	 * @param value
	 *            Lösenord
	 * @throws IOException
	 *             Kastas om filen som data lagras i inte hittas.
	 */
	public void addToAcceptanceMap(String key, String value) throws IOException {
		if (this.acceptanceMap.containsKey(key.hashCode() + "")) {
			System.out.println("Användarnamnet är upptaget!");
			return;
		} else if (!characterCheck(key, false)) {
			System.out.println("Fel: Användarnamnet innehåller ett ogiltigt tecken");
			return;
		} else if (!characterCheck(value, true)) {
			System.out.println("Fel: Lösenordet innehåller ett ogiltigt tecken");
			return;
		}
		System.out.println("Användare läggs till i systemet!");
		this.acceptanceMap.put(key.hashCode() + "", value.hashCode() + "");
		write(true);

	}

	/**
	 * Kollar ifall en sträng är tom eller innehåller ett ogilitigt tecken:
	 * 'å','ä','ö','Å','Ä','Ö',' ',',' Returnerar true om strängen passerar
	 * checken (inte innehåller ovanstående).
	 * 
	 * @param str
	 *            Strängen som ska kontrolleras
	 * @return boolean true om pass, false om fail
	 */
	public static boolean characterCheck(String str, boolean pass) {
		int a;
		try {
			a = (int) str.charAt(0);
		} catch (StringIndexOutOfBoundsException ex) {
			return false;
		}

		if (a == 65533) { /* Code for uknown char */
			return false;
		}
		if (pass) {
			if (str.isEmpty() || str.contains(" ")) {
				return false;
			}
		} else {
			if (str.isEmpty() || str.contains(" ") || str.contains(",") || str.contains("å") || str.contains("ä")
					|| str.contains("ö") || str.contains("Å") || str.contains("Ä") || str.contains("Ö")) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returnerar klassens HashMap innehållandes alla användarnamn (nycklar) och
	 * lösenord (värden).
	 * 
	 * @return acceptanceMap HashMap med användarnamn och lösenord
	 */
	public HashMap<String, String> getAcceptanceList() {
		return this.acceptanceMap;
	}

	/**
	 * Tar reda på alla filer som existerar i en mapp som angivits som argument
	 * och returnerar det som en sträng-array.
	 * 
	 * @param folder
	 *            Mappen vars filnamn ska sparar i en sträng array
	 * @return result Sträng-array innehållandes alla filnamn
	 */
	private String[] listFiles(String folder) {
		File backupFolder = new File("filer/" + folder + "/");
		File[] backups = backupFolder.listFiles();
		if (backups.length > 0) {
			String[] result = new String[backups.length + 1];
			result[0] = "Available user backup files in the " + folder + " folder";
			int i = 1;

			for (File f : backups) {
				result[i] = f.getName();
				i++;
			}
			return result;
		}
		System.out.println("Finns inga backup filer i " + folder + " mappen.");
		return null;
	}

	/**
	 * Returnerar en String-array med filsökvägarna inkl. namnen till aktiva
	 * backupfilerna och gamla backupfilerna, om där finns några.
	 * 
	 * @return result String[] där varje element har filsökvägen till en av
	 *         backupfilerna.
	 */
	public String[] getBackupsList() {
		int size = 0;
		String[] active = null;
		String[] old = null;
		try {
			active = getUserBackupsList();
		} catch (NullPointerException ex) {
			System.out.println("userBackups mappen är tom.");
		}
		try {
			old = getOldUserBackupsList();
		} catch (NullPointerException ex) {
			System.out.println("oldUserBackups mappen är tom.");
		}
		if (active != null && old != null) {
			size = active.length + old.length;
			String[] res = new String[size];
			for (int i = 0; i < active.length; i++) {
				res[i] = active[i];
			}
			int j = 0;
			for (int i = active.length; i < size; i++) {
				res[i] = old[j];
				j++;
			}
			return res;
		}
		System.out.println(
				"Anropa istället metoden för den specifika mappen: getUserBackupsList eller getOldUserBackupsList");
		return null;
	}

	/**
	 * Returnerar en String-array med filsökvägarna inkl. namnen till aktiva
	 * backupfilerna om där finns några.
	 * 
	 * @return result String[] där varje element har filsökvägen till en av
	 *         backupfilerna. Returnerar null om där inte fanns backup filer.
	 */
	public String[] getUserBackupsList() {
		if (userBackupFolderExists()) {
			return listFiles("userBackups");
		} else {
			System.out.println("Fanns ingen userBackups mapp men den har skapats nu.");
			return null;
		}
	}

	/**
	 * Returnerar en String-array med filsökvägarna inkl. namnen till gamla
	 * backupfilerna om där finns några.
	 * 
	 * @return result String[] där varje element har filsökvägen till en av
	 *         backupfilerna. Returnerar null om där inte fanns backup filer.
	 */

	public String[] getOldUserBackupsList() {
		if (oldUserBackupFolderExists()) {
			return listFiles("oldUserBackups");
		} else {
			System.out.println("Fanns ingen oldUserBackups mapp men den har skapats nu.");
			return null;
		}
	}

	/**
	 * Laddar in en angiven backupfil i realtid (server omstart behövs inte) och
	 * uppdaterar klassens egna HashMap med backup filens värden samt skriver
	 * över text filen som lagrar all data. Om backupfilen som angets inte
	 * existerar händer inget med nuvarande HashMap och lagringst textfil.
	 * 
	 * @param backupname
	 *            backupfilen som ska laddas in
	 * @throws IOException
	 *             Kastas om filen inte hittas
	 */
	public void loadBackup(String backupname, String folder) throws IOException {
		String name = "";
		File backupFolder = new File("filer/" + folder + "/");
		File[] backups = backupFolder.listFiles();
		boolean state = false;
		if (backups.length > 0) {

			for (File f : backups) {
				name = f.getName();
				if (name.contains(backupname) && state == false) {
					state = true;
				}
			}

			if (state) {
				HashMap<String, String> newMap = new HashMap<String, String>();
				String filename = "filer/" + folder + "/";
				filename += name;
				readFile(filename, newMap);
				this.updateAcceptanceMap(newMap,
						false); /*
								 * Gör inte en backup eftersom denna backupen
								 * redan finns
								 */
			} else {
				System.out.println("Backup filen existerar inte.");
			}
		} else {
			System.out.println("Inga backup filer existerar.");
		}
	}

	// /**
	// * Kollar upp om tillräcklig tid passerat sedan senaste backupskrivningen
	// * (Ställs in i klassens instansvariabel) och om tillräcklig tid passerat
	// så
	// * anropas backupCheckLimit()-metoden och sedan skrivs en ny backupfil med
	// * hjälp av anrop till createBackup()-metoden.
	// *
	// * @param timePassed
	// * Tiden som gått sedan senaste backupskrivningen (räknas ut
	// * automatiskt av klassen).
	// * @throws IOException
	// * Kastas om filer inte hittas för backup-funktionaliteten.
	// */
	// public void backup(long timePassed) throws IOException {
	// if (timePassed > this.backupTimeCheck) {
	// System.out.println("yes within backup time check");
	// backupLimitCheck();
	// createBackup();
	// }
	// }
	/**
	 * Kollar upp om gränsen för backupskrivningar i userBackups mappen har
	 * uppnåtts (Ställs in i klassens instansvariabel) och skapar sedan en ny
	 * backupfil med hjälp av anrop till createBackup()-metoden.
	 *
	 * @throws IOException
	 *             Kastas om filer inte hittas för backup-funktionaliteten.
	 */
	public void backup() throws IOException {
		backupLimitCheck();
		createBackup();
	}

	/**
	 * Skapar en backupfil av lagrings textfilen i mappen "filer/userBackups/"
	 * och namger den enligt formatet
	 * userBackup_*dag*_*månad*_*år*_*timme*_*minut*_*sekund*.txt
	 * 
	 * @throws IOException
	 *             Kastas om filer inte hittas
	 */
	public void createBackup() throws IOException {
		System.out.println("Creating backup");
		SimpleDateFormat sdf = new SimpleDateFormat("dd_M_YYYY_HH_mm_ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm"));
		String backupName = "filer/userBackups/userBackup_" + sdf.format(new Date()) + ".txt";
		File backupFile = new File(backupName);
		File activeFile = new File(this.filename);
		copyFile(activeFile, backupFile);
		// this.timer = System.currentTimeMillis();
	}

	/**
	 * Lagrar all data som finns i en fil till den andra angivna filen. Kopierar
	 * över datan från en fill till en annan.
	 * 
	 * @param originalFile
	 *            Filen med datan som ska kopieras
	 * @param newFile
	 *            Filen dit den kopierade datan ska skrivas
	 * @throws IOException
	 *             Kastas om fil inte finns.
	 */
	private void copyFile(File originalFile, File newFile) throws IOException {
		FileInputStream inStream = new FileInputStream(originalFile);
		FileOutputStream outStream = new FileOutputStream(newFile);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inStream.read(buffer)) > 0) {
			outStream.write(buffer, 0, length);
		}
		inStream.close();
		outStream.close();
	}

	/**
	 * Kontrollerar om antalet backupfiler som angets som max antal (anges i
	 * klassens instansvariabel) har uppnåts. Om gränsen har uppnåts raderas den
	 * äldsta filen. Kontrollerar även ifall mappen "userBackups" existerar i
	 * mappen "filer", om inte så skapas mappen, det gör med hjälp av anropet
	 * till backupFolerExists()-metoden.
	 * 
	 * @throws IOException
	 *             Kastas om fil inte finns.
	 */
	public void backupLimitCheck() throws IOException {
		// System.out.println("Inne");

		userBackupFolderExists();
		File backupsFolder = new File("filer/userBackups/");
		int backups = backupsFolder.list().length;

		System.out.println("backups in userBackups folder: " + backups + " out of maximum: " + this.backupLimit);

		if (backups == this.backupLimit) {

			System.out.println("Backup limit for UserBackups folder Exceeded.");
			moveBackupFiles(1, false);

		} else if (backups > this.backupLimit) {
			moveBackupFiles((backups + 1) - this.backupLimit, false);

		} else {
			System.out.println("yes within backups limit");
		}
	}

	/**
	 * Kontrollerar om mappen "userBackups" existerar i mappen "filer", om inte
	 * så skapas mappen.
	 * 
	 */
	public boolean userBackupFolderExists() {
		return backupFolderExists("userBackups");
	}

	/**
	 * Kontrollerar om mappen "oldUserBackups" existerar i mappen "filer", om
	 * inte så skapas mappen.
	 * 
	 */
	public boolean oldUserBackupFolderExists() {
		return backupFolderExists("oldUserBackups");
	}

	/**
	 * Kontrollerar om en backup mapp existerar inuti "filer" mappen, och om
	 * inte så skapas mappen.
	 * 
	 * @param folder
	 *            mappens namn
	 */
	public boolean backupFolderExists(String folder) {
		boolean backupFolderExists = true;
		try {
			@SuppressWarnings("unused")
			int backups = new File("filer/" + folder + "/").list().length;
		} catch (NullPointerException ex) {
			backupFolderExists = false;
		}

		if (!backupFolderExists) {
			File newBackupsFolder = new File("filer/" + folder + "/");
			newBackupsFolder.mkdir();
		}
		return backupFolderExists;
	}

	/**
	 * Kontrollerar om antalet existerande backupfiler i backup mappen är lika
	 * med eller större än det angivna värdet. Om ja flyttas alla de
	 * backupfilerna till en ny separat Backup mapp.
	 * 
	 * @param howMany
	 *            Hur många filer som ska flyttas över till oldUserBackups
	 *            mappen eller bara delete:as.
	 * @param justDelete
	 *            Om true så flyttas inte filerna till en ny mapp, de delete:as
	 *            bara.
	 * @throws IOException
	 *             Kastas om mapp inte finns.
	 */
	private void moveBackupFiles(int howMany, boolean justDelete) throws IOException {
		userBackupFolderExists();
		File backupsFolder = new File("filer/userBackups/");
		File[] backupFiles = backupsFolder.listFiles();

		if (backupFiles.length != 0) {
			// int backups = new File("filer/userBackups/").list().length;

			// SimpleDateFormat sdf = new
			// SimpleDateFormat("dd_M_YYYY_HH_mm_ss");
			// sdf.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm"));
			// String extraBackupFolderName = "filer/OldBackupFolder_" +
			// sdf.format(new Date());
			// File extraBackupFolder = new File(extraBackupFolderName);
			// extraBackupFolder.mkdir();
			oldUserBackupFolderExists();

			System.out.println("backupFiles.length: " + backupFiles.length);
			for (int i = 0; i < howMany; i++) {
				if (!justDelete) {
					String movedFileName = "filer/oldUserBackups/" + backupFiles[i].getName();
					String originalFileName = "filer/userBackups/" + backupFiles[i].getName();
					File originalFile = new File(originalFileName);
					File movedFile = new File(movedFileName);
					copyFile(originalFile, movedFile);
				}
				backupFiles[i].delete();

			}
		}
	}

	/**
	 * Sätter gränsen för antal backupfiler som får göras innan äldsta
	 * backupfilen börjar skrivas över.
	 * 
	 * @param backupLimit
	 *            gräns för antal backupfiler som får göras innan äldsta
	 *            backupfilen börjar skrivas över.
	 */
	public void setBackupLimit(int backupLimit) {
		this.backupLimit = backupLimit;
	}

	// /**
	// * Sätter inom hur ofta en backup får göras. Skrivningar till lagrings
	// * textfilen triggar möjlighet till backup.
	// *
	// * @param backupTimeCheck
	// * gräns i millisekunder för hur ofta backups får skapas t.ex.
	// * (1000*60*60) -> inte inom en timme efter en backupskrivning.
	// */
	// public void setBackupTimeCheck(long backupTimeCheck) {
	// this.backupTimeCheck = backupTimeCheck;
	// }

	// /**
	// * Returnerar värdet för inom hur ofta en backup får göras.
	// *
	// * @return backupTimeCheck värdet för inom hur ofta en backup får göras
	// */
	// public long getBackupTimeCheck() {
	// return this.backupTimeCheck;
	// }

	/**
	 * Returnerar värdet för gränsen för antal backupfiler som får göras innan
	 * äldsta backupfilen börjar skrivas över.
	 * 
	 * @return backupLimit värdet för antal backupfiler som får göras innan
	 *         äldsta backupfilen börjar skrivas över.
	 */
	public int getBackupLimit() {
		return this.backupLimit;
	}

	/**
	 * Skriver ut innehållet i klassens HashMap i konsolen.
	 */
	public void printContent() {
		Iterator<String> iter = getAcceptanceList().keySet().iterator();
		while (iter.hasNext()) {
			String keyw = "" + iter.next();
			System.out.print(keyw + ",");
			System.out.print(getAcceptanceList().get(keyw));
			System.out.println();
		}
	}

	/**
	 * Testmetod för att testa att klassen fungerar. Denna metod exekveras från
	 * userAuthentication-klassen som har instansierat ett UserContainer objekt.
	 * Metoden anropas i början av checkCredentials-metoden i userAuthentication
	 * klassen.
	 */
	public void test() {
		String input = "";
		while (!(input.equals("0") || input.equals("exit"))) {
			input = JOptionPane.showInputDialog(
					"\n*************************************\n*****^^^TEST PROGRAM^^^*****\n*************************************\n1. add\n2. getmax\n3. gettime\n4. setmax\n5. settime\n6. print\n7. getBackupslist\n8. loadlist\n9. updatehashed\n10. updatenonhashed\n*************************************\nEnter \"0\" or \"exit\" to exit.\n************************************* ");
			switch (input) {

			case "1":
				System.out.println("RUNNING: add");
				String key = JOptionPane.showInputDialog("namn");
				String value = JOptionPane.showInputDialog("lösen");
				while (!(key.equals("0") || value.equals("0"))) {
					try {
						addToAcceptanceMap(key, value);
					} catch (IOException e) {
						e.printStackTrace();
					}
					key = JOptionPane.showInputDialog("namn, mata in \"0\" för att avsluta");
					value = JOptionPane.showInputDialog("lösen, mata in \"0\" för att avsluta");
				}
				System.out.println("END");
				break;

			case "2":
				System.out.println("RUNNING: getmax");
				JOptionPane.showMessageDialog(null, getBackupLimit() + "");
				System.out.println("END");
				break;

			// case "3":
			// System.out.println("RUNNING: gettime");
			// JOptionPane.showMessageDialog(null, getBackupTimeCheck() + "");
			// System.out.println("END");
			// break;

			case "4":
				System.out.println("RUNNING: setmax");
				int backupLimit = Integer.parseInt(JOptionPane.showInputDialog("Mata in backup limit"));
				setBackupLimit(backupLimit);
				System.out.println("END");
				break;

			// case "5":
			// System.out.println("RUNNING: settime");
			// int backupTimeCheck =
			// Integer.parseInt(JOptionPane.showInputDialog("Mata in backup
			// time"));
			// setBackupTimeCheck(backupTimeCheck);
			// System.out.println("END");
			// break;

			case "6":
				System.out.println("RUNNING: print");
				printContent();
				System.out.println("END");
				break;

			case "7":
				System.out.println("RUNNING: getBackupslist");
				String list[] = getBackupsList();
				String res = "";
				for (String s : list) {
					res += "\n" + s;
				}
				JOptionPane.showMessageDialog(null, res);
				System.out.println("END");
				break;

			// case "8":
			// System.out.println("RUNNING: loadlist");
			// String backupName = JOptionPane.showInputDialog("Namn på
			// backupfil som ska laddas in");
			// try {
			// loadBackup(backupName);
			// } catch (IOException e1) {
			// e1.printStackTrace();
			// }
			// System.out.println("END");
			// break;

			case "9":
				System.out.println("RUNNING: updatehashed");
				HashMap<String, String> hashedMap = new HashMap<String, String>();
				hashedMap.put("98615734", "98619139"); // "grass", "green"
				hashedMap.put("3535235", "113101865"); // "snow", "white"
				hashedMap.put("112903447", "3027034"); // "water", "blue"
				try {
					updateAcceptanceMap(hashedMap, false);
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("END");
				break;

			case "10":
				System.out.println("RUNNING: updatenonhashed");
				HashMap<String, String> nonHashedMap = new HashMap<String, String>();
				nonHashedMap.put("mango", "red");
				nonHashedMap.put("banana", "yellow");
				nonHashedMap.put("papaya", "green");
				nonHashedMap.put("orange", "orange");
				try {
					updateAcceptanceMapNonHashedData(nonHashedMap, false);
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("END");
				break;
			}

		}
	}

	public static void main(String[] args) throws IOException {
		/*
		 * TEST 5 - OMÖJLIG ATT PASSA KOMPLETT EFTERSOM ATT
		 * userAuthentication-klassen som instansierar UserContainer behöver
		 * exekvera testningen, det gör den med hjälp av test()-metoden i
		 * UserContainer-klassen. Då verkar allt fungera riktigt bra PASS! -
		 * TESTA HELA KLASSEN OCH DESS FUNKTIONER OCH SE OM SERVERN PÅVERKAS I
		 * REALTID SOM DEN BÖR. OBS:TEST 5 körs genom RootServer-klassen med
		 * anropet till test()-metoden avkommenterad.
		 */

		/*
		 * TEST 4 - PASS - FUNGERAR MEN SERVERN MÅSTE STARTAS OM FÖR ATT
		 * ÄNDRINGEN SKA GÄLLA :/ LÖSTE DET: FICK ÄNDRA I UserAuthentication så
		 * att en ny UserContainer instansierar vid anrop av
		 * checkCredentials-metoden istället för vid instansieringen av
		 * userAuthentication. - Testa backupfunktionalitet: att skapa och ladda
		 * in
		 */
		/*
		 * UserContainer uc = new UserContainer("filer/userList.txt"); // Filen
		 * // skapas String key = JOptionPane.showInputDialog("namn"); //
		 * aaa....ccc String value = JOptionPane.showInputDialog("lösen");//
		 * 111....333 while (!(key.equals("0") || value.equals("0"))) {
		 * uc.addToAcceptanceMap(key, value); key =
		 * JOptionPane.showInputDialog("namn"); value =
		 * JOptionPane.showInputDialog("lösen"); } Iterator<String> iter2 =
		 * uc.getAcceptanceList().keySet().iterator(); System.out.println(
		 * "VID START"); while (iter2.hasNext()) { String keyw = "" +
		 * iter2.next(); System.out.print(keyw + ",");
		 * System.out.print(uc.getAcceptanceList().get(keyw));
		 * System.out.println(); } JOptionPane.showMessageDialog(null,
		 * uc.getBackupsList()); String backup = JOptionPane.showInputDialog(
		 * "backup filnamn", "userBackup_27_4_2016_06_12_51.txt");
		 * uc.loadBackup(backup); Iterator<String> iter =
		 * uc.getAcceptanceList().keySet().iterator(); System.out.println(
		 * "VID AVSLUT"); while (iter.hasNext()) { String keyw = "" +
		 * iter.next(); System.out.print(keyw + ",");
		 * System.out.print(uc.getAcceptanceList().get(keyw));
		 * System.out.println(); }
		 */
		/*
		 * TEST 3 - PASS - Skicka in en ny HashMap med nycklar och värden som ÄR
		 * hashkodade
		 */
		/*
		 * HashMap<String,String> hashedMap = new HashMap<String,String>();
		 * hashedMap.put("98615734", "98619139"); //"grass", "green"
		 * hashedMap.put("3535235", "113101865"); //"snow", "white"
		 * hashedMap.put("112903447", "3027034"); //"water", "blue"
		 * UserContainer uc = new UserContainer("filer/userList.txt");
		 * uc.updateAcceptanceMap(hashedMap);
		 */
		/*
		 * TEST 2 - PASS - Skicka in en ny HashMap med nycklar och värden som
		 * INTE är hashkodade
		 */

		// HashMap<String, String> nonHashedMap = new HashMap<String, String>();
		// nonHashedMap.put("citron", "gul");
		// nonHashedMap.put("apelsin", "orange");
		// nonHashedMap.put("snow", "white");
		// nonHashedMap.put("snww", "whiewaw2");
		// nonHashedMap.put("s23w", "w3213ite");
		// nonHashedMap.put("sdsa", "121");
		// nonHashedMap.put("sda", "231");
		// UserContainer uc = new UserContainer("filer/userList.txt");
		// uc.updateAcceptanceMapNonHashedData(nonHashedMap);

		/* TEST 1 - PASS - Skriv till fil och läs från fil */
		/*
		 * UserContainer uc = new UserContainer("filer/userList.txt"); String
		 * key = JOptionPane.showInputDialog("namn"); String value =
		 * JOptionPane.showInputDialog("lösen"); while (!(key.equals("0") ||
		 * value.equals("0"))) { uc.addToAcceptanceMap(key, value); key =
		 * JOptionPane.showInputDialog("namn"); value =
		 * JOptionPane.showInputDialog("lösen"); }
		 */

	}
}
