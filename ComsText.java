/**
 * 
 * @author Sebastian Sologuren
 *
 */
public class ComsText {

	public static final String frame = "\n******************************************************************************\n";
	public static final String info = frame
			+ "*********Mata in \"menu\" för att komma åt fler kommandon för metodanrop.*********" + frame;
	public static final String menu = frame
			+ "menu) Menu\nMata in \"userc\" för att komma åt metoder i UserContainer\nMata in \"tlog\" för att komma åt metoder i TimestampLog\nMata in \"datac\" för att komma åt metoder i DataContainer\nMata in \"doortime\" för att komma åt metoder i TimeoutCounter\nMata in \"testall\" för att stega igenom alla commands en efter en.\nMata in \"stop\" för att avsluta."
			+ frame;
	/*------------------------------------------ Klass DataContainer, TimestampLog, UserContainer, TimeoutClock ------------------------------------------*/
	public static final String datac = frame
			+ "datac) (DataContainer)\nMata in korresponderande nummer med bokstav för att aktivera en metod\n3a. getAcceptanceListArdu\n3b. getAcceptanceMap\n3c. blip\n3d. addToAcceptanceMap\n3e. setAcceptanceMap\n3f. getIdNameMap\n3g. setIdNameMap\n3h. getDoorState\n3i. setDoorState(doorState:DoorState)\n3j. getShouldLockBeOpened\n3k. setShouldLockBeOpened(state:boolean)\n3l. getTimeout\n3m. setTimeout(int)"
			+ frame;
	public static final String tlog = frame
			+ "tlog) (TimestampLog)\nMata in korresponderande nummer med bokstav för att aktivera en metod\n2a. getCreated\n2b. getLog(String)\n2c. getLogSize(String)\n2d. toString(String)\n2e. addTimestamp(String,boolean)"
			+ frame;
	public static final String userc = frame
			+ "userc) (UserContainer)\nMata in korresponderande nummer med bokstav för att aktivera en metod\n1a. addToAcceptanceMap\n1b. getBackupLimit\n1c. setBackupLimit\n1d. printContent\n1e. getBackupslist\n1f. loadBackup\n1g. updateAcceptanceMap\n1h. updateAcceptanceMapNonHashedData\n1i. getUserBackupsList(void):void\n1j. getOldUserBackupsList(void):void"
			+ frame;
	public static final String doortime = frame
			+ "doortime) (TimeoutClock & DataContainer)\nMata in korresponderande nummer med bokstav för att aktivera en metod\n4a. getTimeLimit\n4b. timeElapsed\n4c. setTimeLimit(int)\n4d. isTimeUp\n4e. reset"
			+ frame;
	/*------------------------------------------ Klassmetoder UserContainer ------------------------------------------*/
	public static final String userc1a = frame
			+ "1a) (UserContainer) addToAcceptanceMap(username:String,pass:String)\ntype \"exit\" to exit this method panel.\n\nAnge username...";
	public static final String userc1b = frame + "1b) (UserContainer) getBackupLimit(void):int\n"
			+ RootServer.getUserContainer().getBackupLimit() + "\nMethod executed." + frame;
	public static final String userc1c = frame
			+ "1c) (UserContainer) setBackupLimit(backupLimit:int)\ntype \"-1\" to exit this method panel.\n\nAnge backups limit...";
	public static final String userc1d = frame + "1d) (UserContainer) printContent(void):void\n";
	public static final String userc1e = frame + "1e) (UserContainer) getBackupsList(void):String[]\n";
	public static final String userc1f = frame
			+ "1f) (UserContainer) loadBackup(filename:String)\ntype \"exit\" to exit this method panel.\nAnge backupfilens namn och format...";
	public static final String userc1g = frame
			+ "1g) (UserContainer) updateAcceptanceMap(hashedMap:HashMap<String,String>, backup:Boolean):void\nWARNING! this will replace the current AcceptanceMap \nwith a new one containing the logins of the creators and their super secret passwords.\nPress \"y\" to continue or any other button to exit";
	public static final String userc1h = frame
			+ "1h) (UserContainer) updateAcceptanceMapNonHashedData(hashedMap:HashMap<String,String>, backup:Boolean):void\nWARNING! this will replace the current AcceptanceMap \nwith a new one containing the logins of the creators and their super secret passwords.\nPress \"y\" to continue or any other button to exit";
	public static final String userc1i = frame + "1i) (UserContainer) getUserBackupsList(void):void\n";
	public static final String userc1j = frame + "1j) (UserContainer) getOldUserBackupsList(void):void\n";
	/*------------------------------------------ Klassmetoder TimestampLog ------------------------------------------*/
	public static final String tlog2a = frame + "2a) (TimestampLog) getCreated(void):String\n"
			+ RootServer.getTimestampLog().getCreated() + "\nMethod executed." + frame;
	public static final String tlog2b = frame
			+ "2b) (TimestampLog) getLog(searchTerm:String):String[]\ntype \"exit\" to exit this method panel.\n(OPTIONELLT)Ange en sökterm...\n(Det går bra att använda \"&\" som logiskt \"och\" och \"£\" som logiskt \"eller\")";
	public static final String tlog2c = frame
			+ "2c) (TimestampLog) getLogSize(searchTerm:String):int\ntype \"exit\" to exit this method panel.\n(OPTIONELLT)Ange en sökterm...\nså visas antalet tidstämplar innehållandes söktermen.\n(Det går bra att använda \"&\" som logiskt \"och\" och \"£\" som logiskt \"eller\")";
	public static final String tlog2d = frame
			+ "2d) (TimestampLog) toString(searchTerm:String):String\ntype \"exit\" to exit this method panel.\n(OPTIONELLT)Ange en sökterm...\nså visas antalet tidstämplar innehållandes söktermen.\n(Det går bra att använda \"&\" som logiskt \"och\" och \"£\" som logiskt \"eller\")";
	public static final String tlog2e = frame
			+ "2e) (TimestampLog) addTimestamp(id:String,succeeded:boolean):void\ntype \"exit\" to exit this method panel.\nAnge ett id eller mata in \"exit\" för att avsluta....\n";

	/* \ngetTimeout\n3m. setTimeout(int)" */
	/*------------------------------------------ Klassmetoder DataContainer ------------------------------------------*/
	public static final String datac3a = frame + "3a) (DataContainer) 3a. getAcceptanceListArdu(void):String\n"
			+ RootServer.getDataContainer().getAcceptanceListArdu() + "\nMethod executed." + frame;
	public static final String datac3b = frame
			+ "3b) (DataContainer) 3b. getAcceptanceList(void):HashMap<String, Boolean>";
	public static final String datac3c = frame
			+ "3c) (DataContainer) blip(id:String):void\ntype \"exit\" to exit this method panel.\nAnge ett virtuellt ID som ska blippas...";
	public static final String datac3d = frame
			+ "3d) (DataContainer) addToAcceptanceMap(key:String, value:Boolean):void\ntype \"exit\" to exit this method panel.\nAnge ett kort ID som ska läggas till...";
	public static final String datac3e = frame
			+ "3e) (DataContainer) updateAcceptanceMap(map:HashMap<String,Boolean>):void\nWARNING! this will replace the current AcceptanceMap \nwith a new one containing the card IDs of the creators.\nPress \"y\" to continue or any other button to exit";
	public static final String datac3f = frame + "3f) (DataContainer) getIdNameMap(void):HashMap<String, String>";
	public static final String datac3g = frame
			+ "3g) (DataContainer) setIdNameMap():void\nWARNING! This will replace the current idNameList with \none containing the card IDs of the creators mapped to their names. \nPress \"y\" to continue or any other button to exit";
	public static final String datac3h = frame + "3h) (DataContainer) getDoorState(void):DoorState\n" + "Dörr status: "
			+ RootServer.getDataContainer().getDoorState() + "\nMethod executed." + frame;
	public static final String datac3i = frame
			+ "3i) (DataContainer) setDoorState(boolean):void\ntype \"exit\" to exit this method panel.\nAnge en dörrstatus: OPEN , CLOSED eller OPENALARM.";
	public static final String datac3j = frame + "3j) (DataContainer) getShouldLockBeOpened(void):boolean)\nShouldLockBeOpened value: "+RootServer.getDataContainer().getShouldLockBeOpened()+"\nMethod executed." + frame;
	public static final String datac3k = frame + "3k) (DataContainer) setShouldLockBeOpened(doorState:DoorState):void\ntype \"exit\" to exit this method panel.\nAnge en \"true\" eller \"false\"";
	public static final String datac3l = frame + "3l) (DataContainer) getTimeout(void):int\nTimeout value: "+RootServer.getDataContainer().getTimeout()+ "\nMethod executed." + frame;
	public static final String datac3m = frame + "3m) (DataContainer) setTimeout(seconds:int):void\ntype \"0\" to exit this method panel.\nAnge ett heltal (antal sekunder).";

	/*------------------------------------------ Klassmetoder TimeoutClock ------------------------------------------*/
	public static final String doortime4a = frame + "4a) (TimeoutClock) getTimeLimit(void):int\n"
			+ RootServer.getDataContainer().getTimeoutClock().getTimeLimit() + " sekunder.\nMethod executed." + frame;
	public static final String doortime4b = frame + "4b) (TimeoutClock) timeElapsed(void):int\n"
			+ RootServer.getDataContainer().getTimeoutClock().timeElapsed() + " sekunder.\nMethod executed." + frame;
	public static final String doortime4c = frame
			+ "4c) (TimeoutClock) setTimeLimit(limit:int):void\ntype \"exit\" to exit this method panel.\nAnge i sekunder hur lång tid dörren får vara öppen innan en push-notis skickas ut till app:en.";
	public static final String doortime4d = frame + "4d) (TimeoutClock) isTimeUp(void):boolean\nisTimeUp: "
			+ RootServer.getDataContainer().getTimeoutClock().isTimeUp() + "\nMethod executed." + frame;
	public static final String doortime4e = frame
			+ "4e) (TimeoutClock) reset(void):void\nDen här metoden kommer att nollställa räknaren. Tryck \"y\" för att fortsätta eller valfri annan knapp för att avbryta.";
}
