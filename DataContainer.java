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

public class DataContainer {
	private HashMap<String,Boolean> acceptanceMap = new HashMap<String,Boolean>();
	private String filename;
	private BufferedReader bReader;
	public DataContainer(String filename){//Try catch måste implementeras
		this.filename = filename;
		try {
			readFile(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
		addToAcceptanceMap(id,true);//LÄGG TILL ID
		
	}
	
	
	public void updateAcceptanceMap(HashMap<String,Boolean> update) throws IOException{
		this.acceptanceMap = update;
		write();
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
		write();
	}
	
	public HashMap<String,Boolean> getAcceptanceList(){
		return this.acceptanceMap;
	}
	
	public String getAcceptanceListArdu(){
		String str = "";
		System.out.println(acceptanceMap.keySet());
		Iterator<String> iter = acceptanceMap.keySet().iterator();
		while(iter.hasNext()){
			String id = iter.next();
			System.out.println(id);
			System.out.println(acceptanceMap.get(id));
			if(acceptanceMap.get(id)){
				str += id+",";
			}
		}
		return str+"\\n";
	}
	
	
	
	public static void main(String[] args) throws IOException{
		DataContainer dc = new DataContainer("filer/text.txt");
		System.out.println(dc.getAcceptanceListArdu());		
		HashMap<String,Boolean> map = new HashMap<String,Boolean>();
		map.put("asdasd1", false);
		map.put("asdsa2", true);
		map.put("asddad3", false);
		map.put("aresr4", false);
		map.put("dawwa5", true);
		map.put("wwew6", false);
		dc.updateAcceptanceMap(map);
		dc.blip("id");
//		System.out.println(dc.getAcceptanceListArdu());
//		dc.addToAcceptanceMap("bbbb4", true);
//		dc.addToAcceptanceMap("ccccc5", false);
//		System.out.println(dc.getAcceptanceListArdu());
	}

}
