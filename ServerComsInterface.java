import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * För att kommunicera med serverns instansierade java klasser genom en java
 * console. Även bra för testning av klassernas funktioner.
 * 
 * @author Sebastian Sologuren
 *
 */
public class ServerComsInterface implements Runnable {
	private Scanner sc = new Scanner(System.in);
	private String input;
	private boolean serverTerminal = false;
	private boolean testing = false;
	private String commandsArr[];

	/**
	 * Sätter igång interfacet från en server terminal.
	 * 
	 * @param command
	 *            Kommandot man skickar till interfacet.
	 */
	public void run(String command) {
		input = command;
		serverTerminal = true;
		run();
	}

	/**
	 * Sätter igång interfacet. Om det körs från en server terminal så stängs
	 * interfacet av efter varje command. Om det kör genom en java terminal så
	 * kommer interfacet alltid att efterfråga commands till det att man skrivit
	 * in "stop".
	 */
	public void run() {
		if (serverTerminal) {
			comsInterface(this.serverTerminal);
		} else {
			input = "";
			while (true && !input.equals("stop")) {
				comsInterface(this.serverTerminal);
			}
		}
	}

	/**
	 * För att testa alla commands.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ServerComsInterface s = new ServerComsInterface();
		s.input = "";
		s.testAll();
	}

	/**
	 * Metoden kör igenom alla commands och utgör därmed ett komplett test.
	 */
	public void testAll() {
		commandsArr = new String[] { "1a", "1b", "1c", "1d", "1e", "1g", "1f", "1h", "1f", "1i", "1j", "2a", "2b", "2c",
				"2d", "2e", "2b", "2c", "3a", "3b", "3c", "3d", "3e", "3f", "3g", "3h", "3i", "3j", "3k", "3l", "3m",
				"4a", "4b", "4c", "4d", "4e" };
		test();
	}

	/**
	 * Metoden kör igenom metoderna som är laddade i commandsArr och ett test.
	 */
	private void test() {
		String title = input;
		testing = true;
		int testNbr = 1;
		for (int i = 0; i < commandsArr.length && !input.equals("q"); i++) {
			System.out.println("Press any button to continue..or 'q' to stop the testing run.\n" + title.toUpperCase() + ": part "
					+ testNbr + " out of " + commandsArr.length + " parts.");
			input = sc.nextLine();
			if (!input.equals("q")) {
				run(commandsArr[i]);
				testNbr++;
			}
		}
		serverTerminal = false;
		testing = false;
		System.out.println("Testing run ended. Tests performed: " + (testNbr - 1 )+" out of " + commandsArr.length);
	}

	/**
	 * En switch-case sats som känner igen alla commands och exekverar enligt
	 * angivna instruktioner.
	 * 
	 * @param terminal
	 */
	public void comsInterface(boolean serverTerminal) {
		if (!testing) {
			System.out.println(ComsText.info);
		}
		if (!serverTerminal) {
			input = sc.nextLine();
		}
		switch (input) {

		case "test1":
			commandsArr = new String[] { "1a", "1a", "1a", "1a", "1a", "1a", "1a","1a" };
			test();
			break;

		case "test2":
			commandsArr = new String[] { "1d", "1a", "1d", "1g", "1d" };
			test();
			break;

		case "test3":
			commandsArr = new String[] { "1d", "1a", "1d", "1h", "1d" };
			test();
			break;

		case "test4":
			commandsArr = new String[] { "1b", "1i", "1c", "1a", "1i", "1a", "1i","1a","1i","1j","1f" };
			test();
			break;

		case "test5":
			commandsArr = new String[] { "1a","2a", "2b", "2c", "2d", "2e","2d", "2b", "2c", "2e","2d", "3c","2b","3d","2b","2a" };
			test();
			break;

		case "test6":
			commandsArr = new String[] { "3a", "3b", "3c", "2d", "3d","3c", "2d", "3g", "3f", "3e","3b","3c","2d" };
			test();
			break;

		case "test7":
			commandsArr = new String[] { "3l", "3m", "3h", "3h", "3h","3k","4b","4d","3m","4a","3h","4b","4b","3h","4b","3k","3h","3k" };
			test();
			break;

		case "stop":
			System.out.println(ComsText.frame);
			break;

		case "menu":
			System.out.println(ComsText.menu);
			break;
		/* Klasser: DataContainer, TimestampLog, UserContainer, TimeoutClock */
		case "doortime":
			System.out.println(ComsText.doortime);
			break;

		case "tlog":
			System.out.println(ComsText.tlog);
			break;

		case "datac":
			System.out.println(ComsText.datac);
			break;

		case "userc":
			System.out.println(ComsText.userc);
			break;

		/* Klassmetoder UserContainer */
		case "1a":
			System.out.println(ComsText.userc1a);
			String user = sc.nextLine();
			System.out.println("Ange pass...");
			String pass = sc.nextLine();
			if (!(user.isEmpty() && pass.isEmpty()) && !(user.equals("exit") || pass.equals("exit"))) {
				try {
					RootServer.getUserContainer().addToAcceptanceMap(user, pass);
					System.out.println("Method executed.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println(ComsText.frame);
			break;

		case "1b":
			System.out.println(ComsText.userc1b);
			break;

		case "1c":
			System.out.println(ComsText.userc1c);
			int limit = -1;
			try {
				limit = sc.nextInt();
			} catch (InputMismatchException ex) {
				System.out.println("Du måste mata in ett tal." + ComsText.frame);
				break;
			}

			if (limit >= 0) {
				RootServer.getUserContainer().setBackupLimit(limit);
				System.out.println("Method executed.");
			}
			System.out.println(ComsText.frame);
			break;

		case "1d":
			System.out.println(ComsText.userc1d);
			RootServer.getUserContainer().printContent();
			System.out.println("\nMethod executed." + ComsText.frame);
			break;

		case "1e":
			String res = "";
			System.out.println(ComsText.userc1e);

			if (RootServer.getUserContainer().getBackupsList() != null) {
				String list[] = RootServer.getUserContainer().getBackupsList();
				if (list.length > 0) {
					res = "";
					for (String s : list) {
						res += "\n" + s;
					}
				}
			}
			System.out.println(res + "\nMethod executed." + ComsText.frame);
			break;

		case "1f":
			System.out.println(ComsText.userc1f);
			input = sc.nextLine();
			if (!(input.equals("exit"))) {
				try {
					System.out.println(
							"Ange mappens namn där backupfilen befinner sig: userBackups eller oldUserBackups");
					String input2 = sc.nextLine();
					if (input2.equals("userBackups") || input2.equals("oldUserBackups")) {
						RootServer.getUserContainer().loadBackup(input, input2);
						System.out.println("Method executed.");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println(ComsText.frame);
			break;

		case "1g":
			System.out.println(ComsText.userc1g);
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
			System.out.println(ComsText.frame);
			break;

		case "1h":
			System.out.println(ComsText.userc1h);
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
			System.out.println(ComsText.frame);
			break;

		case "1i":
			System.out.println(ComsText.userc1i);
			String arr[] = RootServer.getUserContainer().getUserBackupsList();
			if (arr != null) {
				for (String elem : arr) {
					System.out.println(elem);
				}
			} else {
				System.out.println("Finns inga backup filer i mappen.");
			}
			System.out.println("\nMethod executed." + ComsText.frame);
			break;

		case "1j":
			System.out.println(ComsText.userc1j);
			String arr2[] = RootServer.getUserContainer().getOldUserBackupsList();
			if (arr2 != null) {
				for (String elem : arr2) {
					System.out.println(elem);
				}
			} else {
				System.out.println("Finns inga backup filer i mappen.");
			}

			System.out.println("\nMethod executed." + ComsText.frame);
			break;

		/* Klassmetoder TimestampLog */
		case "2a":
			System.out.println(ComsText.tlog2a);
			break;

		case "2b":
			System.out.println(ComsText.tlog2b);
			input = sc.nextLine();
			String[] log;
			if (!(input.equals("exit"))) {
				try{
				log = RootServer.getTimestampLog().getLog(input);
				}catch(NullPointerException ex){
					break;
				}
				String result = "";
				for (String s : log) {
					result += s;
				}
				System.out.println(result + "\nMethod executed.");
			}
			System.out.println(ComsText.frame);
			break;

		case "2c":
			System.out.println(ComsText.tlog2c);
			input = sc.nextLine();
			if (!(input.equals("exit"))) {
				System.out.println(RootServer.getTimestampLog().getLogSize(input) + "\nMethod executed.");
			}
			System.out.println(ComsText.frame);
			break;

		case "2d":
			System.out.println(ComsText.tlog2d);
			input = sc.nextLine();
			if (!(input.equals("exit"))) {
				System.out.println(RootServer.getTimestampLog().toString(input) + "\nMethod executed.");
			}
			System.out.println(ComsText.frame);
			break;

		case "2e":
			System.out.println(ComsText.tlog2e);
			input = sc.nextLine();
			if (!(input.equals("exit"))) {
				System.out.println(
						"Mata in \"true\" för inloggningen var ska loggas som lyckad. Annars, matta in vad som helst.");
				String input2 = sc.nextLine();
				if (input2.equals("true")) {
					RootServer.getTimestampLog().addTimestamp(input, true);
				} else {
					RootServer.getTimestampLog().addTimestamp(input, false);
				}
				System.out.println("\nMethod executed.");
			}
			System.out.println(ComsText.frame);
			break;

		/* Klassmetoder DataContainer */
		case "3a":
			System.out.println(ComsText.datac3a);
			break;

		case "3b":
			System.out.println(ComsText.datac3b);
			HashMap<String, Boolean> map = RootServer.getDataContainer().getAcceptanceMap();
			String res2 = "";
			Iterator<String> keyIter = map.keySet().iterator();
			while (keyIter.hasNext()) {
				String key = keyIter.next();
				res2 += "\n" + key + "  " + map.get(key).toString();
			}
			System.out.println(res2 + "\n\nMethod executed." + ComsText.frame);
			break;

		case "3c":
			System.out.println(ComsText.datac3c);
			input = sc.nextLine();
			if (!(input.equals("exit"))) {
				try {
					RootServer.getDataContainer().blip(input);
					System.out.println("\nMethod executed.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println(ComsText.frame);
			break;

		case "3d":
			System.out.println(ComsText.datac3d);
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
			System.out.println(ComsText.frame);
			break;

		case "3e":
			System.out.println(ComsText.datac3e);
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
					RootServer.getDataContainer().setAcceptanceMap(map2);
					System.out.println("\nMethod executed.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println(ComsText.frame);
			break;

		case "3f":
			System.out.println(ComsText.datac3f);
			HashMap<String, String> map2 = RootServer.getDataContainer().getIdNameMap();
			String res3 = "";
			for (Entry<String, String> entry : map2.entrySet()) {
				res3 += "kort-id: " + entry.getKey() + "  kortnamn: " + entry.getValue() + "\n";
			}
			System.out.println(res3 + "\nMethod executed." + ComsText.frame);
			break;

		case "3g":
			System.out.println(ComsText.datac3g);
			input = sc.nextLine();
			if (input.equals("y")) {
				HashMap<String, String> map3 = new HashMap<String, String>();
				map3.put("111", "Sebastian");
				map3.put("222", "Arvid");
				map3.put("333", "Mikael");
				map3.put("444", "Hadi");
				map3.put("555", "Benjamin");
				map3.put("666", "Viktor");
				RootServer.getDataContainer().setIdNameMap(map3);
				System.out.println("\nMethod executed.");
			}
			System.out.println(ComsText.frame);
			break;

		case "3h":
			System.out.println(ComsText.datac3h);
			break;

		case "3i":
			System.out.println(ComsText.datac3i);
			input = sc.nextLine().toLowerCase();
			if (!input.equals("exit")) {
				if (input.equals("open")) {
					RootServer.getDataContainer().setDoorState(DoorState.OPEN);
					System.out.println(
							"Door state: " + RootServer.getDataContainer().getDoorState() + "\nMethod executed.");
				} else if (input.equals("openalarm")) {
					RootServer.getDataContainer().setDoorState(DoorState.OPEN_ALARM);
					System.out.println(
							"Door state: " + RootServer.getDataContainer().getDoorState() + "\nMethod executed.");
				} else if (input.equals("closed")) {
					RootServer.getDataContainer().setDoorState(DoorState.CLOSED);
					System.out.println(
							"Door state: " + RootServer.getDataContainer().getDoorState() + "\nMethod executed.");
				}
			}
			System.out.println(ComsText.frame);
			break;

		case "3j":
			System.out.println(ComsText.datac3j);
			break;

		case "3k":
			System.out.println(ComsText.datac3k);
			input = sc.nextLine().toLowerCase();
			if (!input.equals("exit")) {
				if (input.equals("true")) {
					RootServer.getDataContainer().setShouldLockBeOpened(true);
					System.out.println("ShouldLockBeOpened value: "
							+ RootServer.getDataContainer().getShouldLockBeOpened() + "\nMethod executed.");
				}
			}
			System.out.println(ComsText.frame);
			break;

		case "3l":
			System.out.println(ComsText.datac3l);
			break;

		case "3m":
			System.out.println(ComsText.datac3m);
			int inp = 0;
			try {
				inp = sc.nextInt();
			} catch (InputMismatchException ex) {
				System.out.println("Du måste ange ett heltal.");
				System.out.println(ComsText.frame);
			}
			if (inp != 0) {
				RootServer.getDataContainer().getTimeoutClock().setTimeLimit(inp);
				System.out.println("Timeout value (time limit): " + RootServer.getDataContainer().getTimeout()
						+ "\nMethod executed.");
			}
			System.out.println(ComsText.frame);
			break;

		/* Klassmetoder TimeoutClock */
		case "4a":
			System.out.println(ComsText.doortime4a);
			break;

		case "4b":
			System.out.println(ComsText.doortime4b);
			break;

		case "4c":
			System.out.println(ComsText.doortime4c);
			input = sc.nextLine();
			if (!(input.equals("exit"))) {
				try {
					RootServer.getDataContainer().getTimeoutClock().setTimeLimit(Integer.parseInt(input));
				} catch (NumberFormatException ex) {
					System.out.println("Du måste mata in ett heltal.");
				}
				System.out.println("Timeout value: " + RootServer.getDataContainer().getTimeoutClock().getTimeLimit()
						+ "\nMethod executed.");
			}
			System.out.println(ComsText.frame);
			break;

		case "4d":
			System.out.println(ComsText.doortime4d);
			break;

		case "4e":
			System.out.println(ComsText.doortime4e);
			input = sc.nextLine();
			if (input.equals("y")) {
				RootServer.getDataContainer().getTimeoutClock().reset();
				System.out.println("\nMethod executed.");
			}
			System.out.println(ComsText.frame);
			break;

		// case "4f":
		// System.out.println(frame + "4f) (DataContainer)
		// getHasTimeoutPushBeenSent(void):boolean\n"
		// + RootServer.getDataContainer().getHasTimeoutPushBeenSent() +
		// "\nMethod executed." + frame);
		// break;

		// case "4g":
		// System.out.println(frame
		// + "4g) (DataContainer)
		// setHasTimeoutPushBeenSent(state:Boolean):void\ntype \"exit\" to exit
		// this method panel.\ntype \"true\" or \"false\".");
		// input = sc.nextLine();
		// if (!input.equals("exit")) {
		// if (input.equals("true") || input.equals("false")) {
		// RootServer.getDataContainer().setHasTimeoutPushBeenSent(Boolean.parseBoolean(input));
		// System.out.println("\nMethod executed.");
		// }
		// } else {
		// System.out.println("You need to type either \"true\" or \"false\" as
		// input.");
		// }
		// System.out.println(frame);
		// break;

		// case "4h":
		// System.out.println(frame
		// + "4h) (TimeoutClock & DataContainer) runtest\nPress any key to get
		// updated info, type \"exit\" to exit.");
		// RootServer.getDataContainer().getTimeoutClock().reset();
		// RootServer.getDataContainer().setHasTimeoutPushBeenSent(true);
		// input = sc.nextLine();
		// while (!input.equals("exit")) {
		// System.out.println("Time limit: " +
		// RootServer.getDataContainer().getTimeoutClock().getTimeLimit()
		// + "\nTime elapsed: " +
		// RootServer.getDataContainer().getTimeoutClock().timeElapsed()
		// + "\nTime up?: " +
		// RootServer.getDataContainer().getTimeoutClock().isTimeUp()
		// + "\ntype \"exit\" to exit.");
		// input = sc.nextLine();
		// }
		// System.out.println("\nMethod executed." + frame);
		// break;
		}

	}
}
