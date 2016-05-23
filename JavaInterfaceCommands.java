
import java.util.Scanner;
/**
 * Den här klassen är till för att displaya giltiga commands för att komma åt metoder i olika klasser för lockdroid systemet.
 * Man kan då via Bash terminalen och ett shell script lätt få ut informationen direkt på terminalen.
 * @author Sebastian Sologuren
 *
 */
public class JavaInterfaceCommands {
	
	
	public static void main (String[] args){
		String frame = "\n******************************************************************************\n";
		Scanner scan = new Scanner(System.in);
		System.out.println(
				frame + "menu) Menu\nMata in \"userc\" för att komma åt metoder i UserContainer\nMata in \"tlog\" för att komma åt metoder i TimestampLog\nMata in \"datac\" för att komma åt metoder i DataContainer\nMata in \"doortime\" för att komma åt metoder i TimeoutCounter"
						+ frame);
		System.out.println("Please enter on of the following command to get a list of available commands for that class");
		String input = scan.next();
		System.out.println("I GOT: "+input);
		switch (input) {

		case "datac":
			System.out.println(
					frame + "datac) (DataContainer)\nMata in korresponderande nummer med bokstav för att aktivera en metod\n3a. getAcceptanceListArdu\n3b. getAcceptanceList\n3c. blip\n3d. addToAcceptanceMap\n3e. updateAcceptanceMap"
							+ frame);
			break;
			
		case "tlog":
			System.out.println(
					frame + "tlog) (TimestampLog)\nMata in korresponderande nummer med bokstav för att aktivera en metod\n2a. getCreated\n2b. getLog(String)\n2c. getLogSize(String)\n2d. toString(String)"
							+ frame);
			break;
			
		case "doortime":
			System.out.println(
					frame + "doortime) (TimeoutClock & DataContainer)\nMata in korresponderande nummer med bokstav för att aktivera en metod\n4a. getTimeLimit\n4b. timeElapsed\n4c. setTimeLimit\n4d. isTimeUp\n4e. reset\n4f.AVKOMMENTERAD getHasTimeoutPushBeenSent\n4g.AVKOMMENTERAD setHasTimeoutPushBeenSent\n4h. runtest"
							+ frame);
			break;

		case "userc":
			System.out.println(
					frame + "userc) (UserContainer)\nMata in korresponderande nummer med bokstav för att aktivera en metod\n1a. addToAcceptanceMap\n1b. getBackupLimit\n1c.AVKOMMENTERAD getBackupTimeCheck\n1d. setBackupLimit\n1e.AVKOMMENTERAD setBackupTimeCheck\n1f. printContent\n1g. getBackupslist\n1h. loadBackup\n1i. updateAcceptanceMap\n1j. updateAcceptanceMapNonHashedData\n1k. getUserBackupsList\n1l. getOldUserBackupsList"
							+ frame);
			break;

		}	
		scan.close();
		System.out.println("Run the .sh script javaInterface.sh and when asked insert the command, for example: 1a ");
	}

}