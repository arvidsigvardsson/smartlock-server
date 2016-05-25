import java.util.Scanner;

public class JavaInterface implements Runnable{
	
	public void run(){
		Scanner scan = new Scanner(System.in);
		System.out.println("Please enter a ServerComsInterface command");
		String input = scan.next();
		System.out.println("I GOT: "+input);
		ServerComsInterface coms = new ServerComsInterface();
		coms.run(input);
		scan.close();
	}
	public static void main (String[] args){
		JavaInterface j = new JavaInterface();
		j.run();
	}

}
