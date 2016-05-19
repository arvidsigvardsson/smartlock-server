import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

public class ServerComsInterface implements Runnable {
	private Scanner sc = new Scanner(System.in);
	private final String frame = "\n******************************************************************************\n";

	public void run() {
		System.out.println(
				frame + "*********Mata in \"menu\" för att komma åt fler kommandon för metodanrop.*********" + frame);
		while (true) {
			String input = sc.nextLine();
			switch (input) {

			case "menu":
				System.out.println(
						frame + "menu) Menu\nMata in \"userc\" för att komma åt metoder i UserContainer\nMata in \"tlog\" för att komma åt metoder i TimestampLog\nMata in \"datac\" för att komma åt metoder i DataContainer\nMata in \"doortime\" för att komma åt metoder i TimeoutCounter"
								+ frame);
				break;

			case "doortime":
				System.out.println(
						frame + "doortime) (TimeoutClock & DataContainer)\nMata in korresponderande nummer med bokstav för att aktivera en metod\n4a. getTimeLimit\n4b. timeElapsed\n4c. setTimeLimit\n4d. isTimeUp\n4e. reset\n4f. getHasTimeoutPushBeenSent\n4g. setHasTimeoutPushBeenSent\n4h. runtest"
								+ frame);
				break;

			case "userc":
				System.out.println(
						frame + "userc) (UserContainer)\nMata in korresponderande nummer med bokstav för att aktivera en metod\n1a. addToAcceptanceMap\n1b. getBackupLimit\n1c. getBackupTimeCheck\n1d. setBackupLimit\n1e. setBackupTimeCheck\n1f. printContent\n1g. getBackupslist\n1h. loadBackup\n1i. updateAcceptanceMap\n1j. updateAcceptanceMapNonHashedData"
								+ frame);
				break;

			case "1a":
				System.out.println(frame
						+ "1a) (UserContainer) addToAcceptanceMap(username:String,pass:String)\ntype \"exit\" to exit this method panel.\n\nAnge username...");
				String user = sc.nextLine();
				System.out.println("Ange pass...");
				String pass = sc.nextLine();
				if (!(user.equals("exit") || pass.equals("exit"))) {
					try {
						RootServer.getUserContainer().addToAcceptanceMap(user, pass);
						System.out.println("Method executed.");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				System.out.println(frame);
				break;

			case "1b":
				System.out.println(frame + "1b) (UserContainer) getBackupLimit(void):int\n"
						+ RootServer.getUserContainer().getBackupLimit() + "\nMethod executed." + frame);
				break;

			case "1c":
				System.out.println(frame + "1c) (UserContainer) getBackupTimeCheck()(void):long\n"
						+ RootServer.getUserContainer().getBackupTimeCheck() + "\nMethod executed." + frame);
				break;

			case "1d":
				System.out.println(frame
						+ "1d) (UserContainer) setBackupLimit(backupLimit:int)\ntype \"-1\" to exit this method panel.\n\nAnge backups limit...");
				int limit = -1;
				try {
					limit = sc.nextInt();
				} catch (InputMismatchException ex) {
					System.out.println("Du måste mata in ett tal." + frame);
					break;
				}

				if (limit >= 0) {
					RootServer.getUserContainer().setBackupLimit(limit);
					System.out.println("Method executed.");
				}
				System.out.println(frame);
				break;

			case "1e":
				System.out.println(frame
						+ "1e) (UserContainer) setBackupTimeCheck(time:long)\ntype \"-1\" to exit this method panel.\n\nAnge backup Time Check limit...");
				long time = -1;
				try {
					time = sc.nextInt();
				} catch (InputMismatchException ex) {
					System.out.println("Du måste mata in ett tal." + frame);
					break;
				}
				if (time >= 0) {
					RootServer.getUserContainer().setBackupTimeCheck(time);
					System.out.println("Method executed.");
				}
				System.out.println(frame);
				break;

			case "1f":
				System.out.println(frame + "1f) (UserContainer) printContent(void):void\n");
				RootServer.getUserContainer().printContent();
				System.out.println("\nMethod executed." + frame);
				break;

			case "1g":
				String res = "";
				System.out.println(frame + "1g) (UserContainer) getBackupsList(void):String[]\n");

				if (RootServer.getUserContainer().getBackupsList() != null) {
					String list[] = RootServer.getUserContainer().getBackupsList();
					if (list.length > 0) {
						res = "";
						for (String s : list) {
							res += "\n" + s;
						}
					}
				}
				System.out.println(res + "\nMethod executed." + frame);
				break;

			case "1h":
				System.out.println(frame
						+ "1h) (UserContainer) loadBackup(filename:String)\ntype \"exit\" to exit this method panel.\n\nAnge backupfilens namn och format...");
				input = sc.nextLine();
				if (!(input.equals("exit"))) {
					try {
						RootServer.getUserContainer().loadBackup(input);
						System.out.println("Method executed.");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				System.out.println(frame);
				break;

			case "1i":
				System.out.println(frame
						+ "1i) (UserContainer) updateAcceptanceMap(hashedMap:HashMap<String,String>, backup:Boolean):void\nWARNING! this will replace the current AcceptanceMap \nwith a new one containing the logins of the creators and their super secret passwords.\nPress \"y\" to continue or any other button to exit");
				input = sc.nextLine();
				if (input.equals("y")) {
					HashMap<String, String> hashedMap = new HashMap<String, String>();
					hashedMap.put("93094464", "3433489"); // "arvid", "sebbe"
					hashedMap.put("93623050", "3433489"); // "benji", "hadi"
					hashedMap.put("109311123", "3433489"); // "micke", "viktor"
					hashedMap.put("3194686", "3433489"); // "pass"
					hashedMap.put("103890401", "3433489");
					hashedMap.put("-816455105", "3433489");
					try {
						RootServer.getUserContainer().updateAcceptanceMap(hashedMap, false);
						System.out.println("\nMethod executed.");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				System.out.println(frame);
				break;

			case "1j":
				System.out.println(frame
						+ "1j) (UserContainer) updateAcceptanceMapNonHashedData(hashedMap:HashMap<String,String>, backup:Boolean):void\nWARNING! this will replace the current AcceptanceMap \nwith a new one containing the logins of the creators and their super secret passwords.\nPress \"y\" to continue or any other button to exit");
				input = sc.nextLine();
				if (input.equals("y")) {
					HashMap<String, String> nonHashedMap = new HashMap<String, String>();
					nonHashedMap.put("Arvid", "Pass");
					nonHashedMap.put("Sebbe", "Pass");
					nonHashedMap.put("Benji", "Pass");
					nonHashedMap.put("Hadi", "Pass");
					nonHashedMap.put("Micke", "Pass");
					nonHashedMap.put("Viktor", "Pass");
					try {
						RootServer.getUserContainer().updateAcceptanceMapNonHashedData(nonHashedMap, false);
						System.out.println("\nMethod executed.");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				System.out.println(frame);
				break;

			case "tlog":
				System.out.println(
						frame + "tlog) (TimestampLog)\nMata in korresponderande nummer med bokstav för att aktivera en metod\n2a. getCreated\n2b. getLog(String)\n2c. getLogSize(String)\n2d. toString(String)"
								+ frame);
				break;

			case "2a":
				System.out.println(frame + "2a) (TimestampLog) getCreated(void):String\n"
						+ RootServer.getTimestampLog().getCreated() + "\nMethod executed." + frame);
				break;

			case "2b":
				System.out.println(frame
						+ "2b) (TimestampLog) getLog(searchTerm:String):String[]\ntype \"exit\" to exit this method panel.\n(OPTIONELLT)Ange en sökterm...\n(Det går bra att använda \"&\" som logiskt \"och\" och \"%\" som logiskt \"eller\")");
				input = sc.nextLine();
				if (!(input.equals("exit"))) {
					String[] log = RootServer.getTimestampLog().getLog(input);
					String result = "";
					for (String s : log) {
						result += "\n" + s;
					}
					System.out.println(result + "\nMethod executed.");
				}
				System.out.println(frame);
				break;

			case "2c":
				System.out.println(frame
						+ "2c) (TimestampLog) getLogSize(searchTerm:String):int\ntype \"exit\" to exit this method panel.\n(OPTIONELLT)Ange en sökterm...\nså visas antalet tidstämplar innehållandes söktermen.\n(Det går bra att använda \"&\" som logiskt \"och\" och \"%\" som logiskt \"eller\")");
				input = sc.nextLine();
				if (!(input.equals("exit"))) {
					System.out.println(RootServer.getTimestampLog().getLogSize(input) + "\nMethod executed.");
				}
				System.out.println(frame);
				break;

			case "2d":
				System.out.println(frame
						+ "2d) (TimestampLog) toString(searchTerm:String):String\ntype \"exit\" to exit this method panel.\n(OPTIONELLT)Ange en sökterm...\nså visas antalet tidstämplar innehållandes söktermen.\n(Det går bra att använda \"&\" som logiskt \"och\" och \"%\" som logiskt \"eller\")");
				input = sc.nextLine();
				if (!(input.equals("exit"))) {
					System.out.println(RootServer.getTimestampLog().toString(input) + "\nMethod executed.");
				}
				System.out.println(frame);
				break;

			case "datac":
				System.out.println(
						frame + "datac) (DataContainer)\nMata in korresponderande nummer med bokstav för att aktivera en metod\n3a. getAcceptanceListArdu\n3b. getAcceptanceList\n3c. blip\n3d. addToAcceptanceMap\n3e. updateAcceptanceMap"
								+ frame);
				break;

			case "3a":
				System.out.println(frame + "3a) (DataContainer) 3a. getAcceptanceListArdu(void):String\n"
						+ RootServer.getDataContainer().getAcceptanceListArdu() + "\nMethod executed." + frame);
				break;

			case "3b":
				System.out.println(frame + "3b) (DataContainer) 3b. getAcceptanceList(void):HashMap<String, Boolean>");
				HashMap<String, Boolean> map = RootServer.getDataContainer().getAcceptanceList();
				String res2 = "";
				Iterator<String> keyIter = map.keySet().iterator();
				while (keyIter.hasNext()) {
					String key = keyIter.next();
					res2 += "\n" + key + "  " + map.get(key).toString();
				}
				System.out.println(res2 + "\n\nMethod executed." + frame);
				break;

			case "3c":
				System.out.println(frame
						+ "3c) (DataContainer) blip(id:String):void\ntype \"exit\" to exit this method panel.\nAnge ett virtuellt ID som ska blippas...");
				input = sc.nextLine();
				if (!(input.equals("exit"))) {
					try {
						RootServer.getDataContainer().blip(input);
						System.out.println("\nMethod executed.");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				System.out.println(frame);
				break;

			case "3d":
				System.out.println(frame
						+ "3d) (DataContainer) addToAcceptanceMap(key:String, value:Boolean):void\ntype \"exit\" to exit this method panel.\nAnge ett kort ID som ska läggas till...");
				input = sc.nextLine();
				if (!(input.equals("exit"))) {
					System.out.print("Ange om kortets ID ska vara godkänt (true/false)...");
					String bInput = "";
					while (!bInput.equals("true") && !bInput.equals("false")) {
						bInput = sc.nextLine();
					}
					Boolean bInput2 = bInput.equals("true");
					try {
						RootServer.getDataContainer().addToAcceptanceMap(input, bInput2);
						System.out.println("\nMethod executed.");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				System.out.println(frame);
				break;

			case "3e":
				System.out.println(frame
						+ "3e) (DataContainer) updateAcceptanceMap(map:HashMap<String,Boolean>):void\nWARNING! this will replace the current AcceptanceMap \nwith a new one containing the card IDs of the creators.\nPress \"y\" to continue or any other button to exit");
				input = sc.nextLine();
				if (input.equals("y")) {
					HashMap<String, Boolean> map2 = new HashMap<String, Boolean>();
					map2.put("111", true);
					map2.put("222", true);
					map2.put("333", true);
					map2.put("444", true);
					map2.put("555", true);
					map2.put("666", true);
					try {
						RootServer.getDataContainer().updateAcceptanceMap(map2);
						System.out.println("\nMethod executed.");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				System.out.println(frame);
				break;

			case "4a":
				System.out.println(frame + "4a) (TimeoutClock) getTimeLimit(void):int\n"
						+ RootServer.getDataContainer().getTimeoutClock().getTimeLimit()
						+ " sekunder.\nMethod executed." + frame);
				break;

			case "4b":
				System.out.println(frame + "4b) (TimeoutClock) timeElapsed(void):int\n"
						+ RootServer.getDataContainer().getTimeoutClock().timeElapsed() + " sekunder.\nMethod executed."
						+ frame);
				break;

			case "4c":
				System.out.println(frame
						+ "4c) (TimeoutClock) setTimeLimit(limit:int):void\ntype \"exit\" to exit this method panel.\nAnge i sekunder hur lång tid dörren får vara öppen innan en push-notis skickas ut till app:en.");
				input = sc.nextLine();
				if (!(input.equals("exit"))) {
					try {
						RootServer.getDataContainer().getTimeoutClock().setTimeLimit(Integer.parseInt(input));
					} catch (NumberFormatException ex) {
						System.out.println("Du måste mata in ett heltal.");
					}
					System.out.println("\nMethod executed.");
				}
				System.out.println(frame);
				break;

			case "4d":
				System.out.println(frame + "4d) (TimeoutClock) isTimeUp(void):boolean\n"
						+ RootServer.getDataContainer().getTimeoutClock().isTimeUp() + "\nMethod executed." + frame);
				break;

			case "4e":
				System.out.println(frame
						+ "4e) (TimeoutClock) reset(void):void\nDen här metoden kommer att nollställa räknaren. Tryck \"y\" för att fortsätta eller valfri annan knapp för att avbryta.");
				input = sc.nextLine();
				if (input.equals("y")) {
					RootServer.getDataContainer().getTimeoutClock().reset();
					System.out.println("\nMethod executed.");
				}
				System.out.println(frame);
				break;

			case "4f":
				System.out.println(frame + "4f) (DataContainer) getHasTimeoutPushBeenSent(void):boolean\n"
						+ RootServer.getDataContainer().getHasTimeoutPushBeenSent() + "\nMethod executed." + frame);
				break;

			case "4g":
				System.out.println(frame
						+ "4g) (DataContainer) setHasTimeoutPushBeenSent(state:Boolean):void\ntype \"exit\" to exit this method panel.\ntype \"true\" or \"false\".");
				input = sc.nextLine();
				if (!input.equals("exit")) {
					if (input.equals("true") || input.equals("false")) {
						RootServer.getDataContainer().setHasTimeoutPushBeenSent(Boolean.parseBoolean(input));
						System.out.println("\nMethod executed.");
					}
				} else {
					System.out.println("You need to type either \"true\" or \"false\" as input.");
				}
				System.out.println(frame);
				break;

			case "4h":
				System.out.println(frame
						+ "4h) (TimeoutClock & DataContainer) runtest\nPress any key to get updated info, type \"exit\" to exit.");
				RootServer.getDataContainer().getTimeoutClock().reset();
				RootServer.getDataContainer().setHasTimeoutPushBeenSent(true);
				input = sc.nextLine();
				while (!input.equals("exit")) {
					System.out.println("Time limit: " + RootServer.getDataContainer().getTimeoutClock().getTimeLimit()
							+ "\nTime elapsed: " + RootServer.getDataContainer().getTimeoutClock().timeElapsed()
							+ "\nTime up?: " + RootServer.getDataContainer().getTimeoutClock().isTimeUp()
							+ "\ntype \"exit\" to exit.");
					input = sc.nextLine();
				}
				System.out.println("\nMethod executed." + frame);
				break;
			}

		}
	}

}
