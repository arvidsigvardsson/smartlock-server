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

import javax.swing.JOptionPane;

/**
 * 
 * @author Admin Skriver och läser användare samt lösenord till en text-fil.
 *         Lagrar användar uppgifter.
 * 
 */

public class UserContainer {
	private HashMap<String, String> acceptanceMap = new HashMap<String, String>();
	private String filename;
	private BufferedReader bReader;

	
	
	
	/**
	 * Konstuktor som läser in data från angiven fil och lagrar
	 * datan i klassens egna HashMap.
	 * @param filename Filen där datan till instans HashMapen ska läsas in från.
	 */
	public UserContainer(String filename) {
		this.filename = filename;
		try {
			readFile(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	
	/**
	 * Läser in datan i en textfil till klassens instans HashMap.
	 * Inläst data parse:as så att användarnamn lagras som nyckel och 
	 * lösenord lagras som värde. 
	 * @param filename Fil där data ska läsas in från
	 * @throws IOException Kastas om filen inte finns
	 */

	private void readFile(String filename) throws IOException {
		try {
			bReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		} catch (FileNotFoundException e) {
			System.out.println("Filen fanns inte.");
			return;
		}

		String input;
		String key;
		String value;
		String[] data;

		while ((input = bReader.readLine()) != null) {
			data = input.split(",");
			if(data[0].isEmpty()){
				System.out.println("Fel: Användarnamn tomt");
				break;
			}
			key = data[0];
			if(data[1].isEmpty()){
				System.out.print("Fel: Lösenord tomt");
				break;
			}
			value = data[1];
			acceptanceMap.put(key, value);
		}
		bReader.close();
	}
	
	
	/**
	 * <html><b>OBS: Nycklar och värden behöver vara Hashkodade. Om inte använd istället 
	 * updateAcceptanceMapnNonHashedData-metoden.</b></html>
	 * Ersätter klassens HashMap med den som anges som parameter.
	 * Sedan skrivs den till text filen.
	 * @param hashedUpdate Den nya HashMapen som ska ersätta den befintliga. OBS Alla nycklar och värden måste vara hashkodade.
	 * Om inte hashadkodade använd istället updateAcceptanceMapNonHashedData-metoden.   
	 * @throws IOException Kastas om filen som datan skrivs till inte finns.
	 */
	public void updateAcceptanceMap(HashMap<String, String> hashedUpdate) throws IOException {
		this.acceptanceMap = hashedUpdate;
		write();
	}
	
	
	/** 
	 * Tar emot en HashMap med nycklar och värden som inte är hashkodade.
	 * Hashkodar varje nyckel och värde och stoppar in en ny HashMap som sedan 
	 * ersätter klassens egna HashMap. Filen som lagrar all data skrivs också över. 
	 * @param nonHashedUpdate HashMap med användarnamn och lösenord som inte är hashkodade.
	 * @throws IOException Kastas om filen som lagrar datan inte hittas.
	 */
	
	public void updateAcceptanceMapNonHashedData(HashMap<String,String> nonHashedUpdate) throws IOException {
		HashMap<String,String> hashedUpdate = new HashMap<String,String>(); 
		Iterator<String> values = nonHashedUpdate.values().iterator();
		Iterator<String> keys = nonHashedUpdate.keySet().iterator();
		while(keys.hasNext()){
			hashedUpdate.put(keys.next().hashCode()+"", values.next().hashCode()+"");
		}
		updateAcceptanceMap(hashedUpdate);
	}	
	
	/**
	 * Skriver om från HashMapen till filen.
	 * @throws IOException Kastas om filen som data lagras i inte hittas.
	 */
	public void write() throws IOException {
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
	}
	
	
	/**
	 * <html><b>OBS: Å,Ä,Ö fungerar inte, hashkoden blir inte korrekt.</b></html>
	 * Lägger till ett användarnamn (nyckel) och ett lösenord (värde) till 
	 * klassens HashMap och uppdaterar sedan filen med hjälp av write-metoden.
	 * 
	 * @param key Användarnamn
	 * @param value Lösenord
	 * @throws IOException Kastas om filen som data lagras i inte hittas.
	 */
	public void addToAcceptanceMap(String key, String value) throws IOException {
		this.acceptanceMap.put(key.hashCode()+"", value.hashCode()+"");
		write();
	}
	
	
	
	/**
	 * Returnerar klassens HashMap innehållandes alla användarnamn (nycklar) och 
	 * lösenord (värden).
	 * @return acceptanceMap HashMap med användarnamn och lösenord
	 */
	public HashMap<String, String> getAcceptanceList() {
		return this.acceptanceMap;
	}

	
	
	/**
	 * Testmetod för att testa att klassen fungerar isolerat.
	 * Dialogfönster öppnas där man kan skriva in användarnamn och lösenord 
	 * som ska sättas in i HashMapen. Skriver man "0" avslutas programmet.
	 * Filen som datan skrivs till är "filer/userList.txt".
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		/*TEST 3 - PASS - Skicka in en ny HashMap med nycklar och värden som ÄR hashkodade*/
		/*
		HashMap<String,String> hashedMap = new HashMap<String,String>();
		hashedMap.put("98615734", "98619139"); //"grass", "green"
		hashedMap.put("3535235", "113101865"); //"snow", "white"
		hashedMap.put("112903447", "3027034"); //"water", "blue"
		UserContainer uc = new UserContainer("filer/userList.txt");
		uc.updateAcceptanceMap(hashedMap);
		*/
		/*TEST 2 - PASS - Skicka in en ny HashMap med nycklar och värden som INTE är hashkodade*/
		/*
		HashMap<String,String> nonHashedMap = new HashMap<String,String>();
		nonHashedMap.put("citron", "gul");
		nonHashedMap.put("apelsin", "orange");
		nonHashedMap.put("snow", "white");
		UserContainer uc = new UserContainer("filer/userList.txt");
		uc.updateAcceptanceMapNonHashedData(nonHashedMap);
		*/
		/*TEST 1 - PASS - Skriv till fil och läs från fil*/
		
		UserContainer uc = new UserContainer("filer/userList.txt");
		String key = JOptionPane.showInputDialog("namn");
		String value = JOptionPane.showInputDialog("lösen");
		while (!(key.equals("0") || value.equals("0"))) {
			uc.addToAcceptanceMap(key, value);
			key = JOptionPane.showInputDialog("namn");
			value = JOptionPane.showInputDialog("lösen");
		}
		
		

	}

}
