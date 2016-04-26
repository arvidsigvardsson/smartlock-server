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

public class UserContainer {
	private HashMap<String,String> acceptanceMap = new HashMap<String,String>();
	private String filename;
	private BufferedReader bReader;
	public UserContainer(String filename){//Try catch måste implementeras
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
		String value;
		String[] data;

		while((input = bReader.readLine()) != null){
			data = input.split(",");
				key = data[0].hashCode()+"";
				value = data[1].hashCode()+"";
				acceptanceMap.put(key, value);
		}
		bReader.close();
	}


	public void updateAcceptanceMap(HashMap<String,String> update) throws IOException{
		this.acceptanceMap = update;
		write();
	}

	public void write() throws IOException{
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),"UTF-8"));
		String key;
		String value;
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

	public void addToAcceptanceMap(String key,String value) throws IOException{
		this.acceptanceMap.put(key, value);
		write();
	}

	public HashMap<String,String> getAcceptanceList(){
		return this.acceptanceMap;
	}

}

