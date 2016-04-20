import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class DataContainer3 {
	HashMap<String,Boolean> acceptanceMap = new HashMap<String,Boolean>();

	public DataContainer3(String filename) {
		ReadFile(filename);

	}

	private void ReadFile(String filename) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
			String input;
			String key;
			Boolean value;
			String[] data;

			while((input = br.readLine()) != null){
				data = input.split(",");

				for(int i = 0; i < (data.length-1);i++){
					key = data[i];
					if(data[i+1].equals("true")){
						value = true;
					}else{
						value = false;
					}
					acceptanceMap.put(key, value);
					i++;
				}
			}
			br.close();
	} catch (IOException e) {
		System.out.println(e);
	}
	}

	public void blip(String id) throws IOException{
		//Användas för vidare funktionalitet, Servern kan lägga på data på id:T
		System.out.println("BLIP REGISTERED: " + id);
		addToAcceptanceMap(id,true);//LÄGG TILL ID

	}

	public void updateAcceptanceMap(HashMap<String,Boolean> update) throws IOException{
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<Boolean> values = new ArrayList<Boolean>();
		File file = new File("filer/text.txt");
		BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile(),false));
		this.acceptanceMap = update;
		String key;
		Boolean value;


		Set<String> keySet = acceptanceMap.keySet();
		Iterator<String> iterKeys =keySet.iterator();
		while(iterKeys.hasNext()){
			key = iterKeys.next();
			System.out.println("key: "+key);
			bw.write(key+",");
			keys.add(key);
			value = acceptanceMap.get(key);
			System.out.println("value: "+value.toString());
			values.add(value);
			bw.write(value.toString()+",");
			bw.newLine();
		}

		bw.close();

	}

	public void addToAcceptanceMap(String key,Boolean value) throws IOException{
		this.acceptanceMap.put(key, value);
		File file = new File("filer/text.txt");
		BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile(),true));
		bw.write(key+","+value+",");
		bw.close();
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



// 	public static void main(String[] args) throws IOException{
// 		DataContainer3 dc = new DataContainer3("filer/arvidtest.txt");
// 		System.out.println("printar acclist:\n" + dc.getAcceptanceListArdu());
// 		// HashMap<String,Boolean> map = new HashMap<String,Boolean>();
// 		// map.put("asdasd1", false);
// 		// map.put("asdsa2", true);
// 		// map.put("asddad3", false);
// 		// map.put("aresr4", false);
// 		// map.put("dawwa5", true);
// 		// map.put("wwew6", false);
// 		// dc.updateAcceptanceMap(map);
// 		dc.blip("id");
// 		System.out.println("printar acclist:\n" + dc.getAcceptanceListArdu());
// //		dc.addToAcceptanceMap("bbbb4", true);
// //		dc.addToAcceptanceMap("ccccc5", false);
// //		System.out.println(dc.getAcceptanceListArdu());
// 	}

}
