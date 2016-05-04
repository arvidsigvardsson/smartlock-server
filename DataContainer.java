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

public class DataContainer {
	private HashMap<String,Boolean> acceptanceMap = new HashMap<String,Boolean>();
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
		try{
			bReader = new BufferedReader(new InputStreamReader(new FileInputStream(idNameMapFileName)));
		}catch(FileNotFoundException e){
			System.out.println("Filen fanns inte.");
			return;
		}

		String input;
		String key;
		String value;
		String[] data;

		while((input = bReader.readLine()) != null){
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

	private void readFile(String filename) throws IOException{
		try{
			bReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		}catch(FileNotFoundException e){
			System.out.println("Filen fanns inte.");
			return;
		}

		String input;
		String key;
		Boolean value;
		String[] data;

		while((input = bReader.readLine()) != null){
			data = input.split(",");
				key = data[0];
				value = data[1].equals("true");
				acceptanceMap.put(key, value);
		}
		bReader.close();
	}

	public void blip(String id) throws IOException{
		//Användas för vidare funktionalitet, Servern kan lägga på data på id:T
		System.out.println("BLIP REGISTERED");
		if (!acceptanceMap.containsKey(id)) {
			addToAcceptanceMap(id,false);//LÄGG TILL ID
		}

	}


	public void updateAcceptanceMap(HashMap<String,Boolean> update) throws IOException{
		this.acceptanceMap = update;
		sendAdminPush(); // testar pushnotiser
		write();
	}

	public void saveIdNameMapToDisk() {
		try {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(idNameMapFileName),"UTF-8"));

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

	public void write() throws IOException{
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<Boolean> values = new ArrayList<Boolean>();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),"UTF-8"));
		String key;
		Boolean value;
		Set<String> keySet = acceptanceMap.keySet();
		Iterator<String> iterKeys =keySet.iterator();
		while(iterKeys.hasNext()){
			key = iterKeys.next();
			bw.write(key+",");
			keys.add(key);
			value = acceptanceMap.get(key);
			values.add(value);
			bw.write(value.toString()+",");
			bw.newLine();
		}
		bw.close();
	}

	public void addToAcceptanceMap(String key,Boolean value) throws IOException{
		this.acceptanceMap.put(key, value);
		sendAdminPush(); // testar pushnotiser
		write();
	}

	public HashMap<String,Boolean> getAcceptanceList(){
		return this.acceptanceMap;
	}

	public String getAcceptanceListArdu(){
		String str = "l";
		// System.out.println(acceptanceMap.keySet());
		Iterator<String> iter = acceptanceMap.keySet().iterator();
		while(iter.hasNext()){
			String id = iter.next();
			// System.out.println(id);
			// System.out.println(acceptanceMap.get(id));
			if(acceptanceMap.get(id)){
				str += id+",";
			}
		}
		return str+"\n";
	}



	public static void main(String[] args) throws IOException{
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
//		System.out.println(dc.getAcceptanceListArdu());
//		dc.addToAcceptanceMap("bbbb4", true);
//		dc.addToAcceptanceMap("ccccc5", false);
//		System.out.println(dc.getAcceptanceListArdu());
	}

	public void sendAdminPush() {
		RootServer.getPushNotifier().sendAdminPushNotification();
	}
}
