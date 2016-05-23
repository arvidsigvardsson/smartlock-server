import java.util.Scanner;

public class JavaInterface {
	
	
	public static void main (String[] args){
//		RootServer.getDataContainer();
//		RootServer.getTimestampLog();
//		RootServer.getUserContainer();
		Scanner scan = new Scanner(System.in);
		System.out.println("Please enter a ServerComsInterface command");
		String input = scan.next();
		System.out.println("I GOT: "+input);
		ServerComsInterface coms = new ServerComsInterface();
		coms.run(input);
		scan.close();
	}

}
