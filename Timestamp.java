
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 
 * Skapar tidstämplar som lagrar information om vem som försökt logga in, när
 * denne försökt logga in, samt om inloggningen lyckades. Timestamp objekt
 * lagras i en TimestampLog.
 * 
 * @author Admin
 *
 */
public class Timestamp {
	private SimpleDateFormat sdf;
	private String stamp;
	private String loginStatus;

	/**
	 * Konstruktor som tar emot användare samt status om huruvida inloggningen
	 * lyckades.
	 * 
	 * @param user
	 *            Användarens namn.
	 * @param success
	 *            true on inloggningen lyckades, false om inte.
	 */
	public Timestamp(String user, boolean success) {
		this.sdf = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Sweden/Stockholm"));
		if (success) {
			loginStatus = "Succeeded";
		} else {
			loginStatus = "Failed";
		}
		String dateTime[] = sdf.format(new Date()).split(" ");
		this.stamp = user + "," + dateTime[0] + "," + dateTime[1] +","+loginStatus;
	}

	public Timestamp(String[] loadedStamp){
		String res =loadedStamp[0]+","+loadedStamp[1]+","+loadedStamp[2]+","+loadedStamp[3];
		this.stamp = res;
	}
	/**
	 * Returnerar Timestamp objektets information i en sträng.
	 * 
	 * @return Tidstämpel i sträng format
	 */
	public String getTimestamp() {
		return this.stamp;
	}

	/**
	 * Testmetod för att säkerhetställa att klassen fungerar.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Timestamp s = new Timestamp("seb", true);
		System.out.println(s.getTimestamp());
	}

}
