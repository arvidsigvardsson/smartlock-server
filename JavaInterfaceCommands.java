
import java.util.Scanner;
/**
 * Den här klassen är till för att displaya giltiga commands för att komma åt metoder i olika klasser för lockdroid systemet.
 * Man kan då via Bash terminalen och ett shell script lätt få ut informationen direkt på terminalen.
 * @author Sebastian Sologuren
 *
 */
public class JavaInterfaceCommands {
	
	
	public static void main (String[] args){
		Scanner scan = new Scanner(System.in);
		System.out.println(ComsText.info);
		System.out.println("Please enter on of the following command to get a list of available commands for that class");
		String input = scan.next();
		System.out.println("I GOT: "+input);
		switch (input) {

		case "datac":
			System.out.println(ComsText.datac);
			break;
			
		case "tlog":
			System.out.println(ComsText.tlog);
			break;
			
		case "doortime":
			System.out.println(ComsText.doortime);
			break;

		case "userc":
			System.out.println(ComsText.userc);
			break;

		}	
		scan.close();
		System.out.println("Run the .sh script javaInterface.sh and when asked insert the command, for example: 1a ");
	}

}