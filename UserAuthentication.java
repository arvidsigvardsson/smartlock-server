import java.util.HashMap;
import com.sun.net.httpserver.BasicAuthenticator;
	/**
	 * Klass som används för att indentifiera om en användare ska få vidare tillgång till funktioner
	 * lagrade på servern. Användarnamn och lösenord är vad som kontrolleras mot en UserContainer som instansieras
	 * i denna klass och innehåller en lista på alla godkända användarnamn och dess respektive lösenord.
	 * Timestamp funktionen aktiveras i denna klass vid varje check av användarnamn och lösen.
	 * @author Admin
	 *
	 */
public class UserAuthentication extends BasicAuthenticator{
		private UserContainer container;
		private TimestampLog log;

		/**
		 * Skapar en en ny UserAuthentication instans med angivet argument som namn på realm:et.
		 * @param message
		 */
		public UserAuthentication(String message) {
		super(message);
		this.log = RootServer.getTimestampLog();
		this.container = RootServer.getUserContainer();
	}

/**
 * Kontrollerar angivet användarnamn och lösenord mot listan. Om det angivna användarnamnet och lösenordet finns
 * med i listan returneras "true", annars "false". Detta booleanska värde ligger i grund till hur HttpContext objektet
 * som instansieras i ClientHttpServer klassen ska tillåta eller blockera klienten.
 * @param user användarnamn
 * @param pass lösenord
 * @return booelan Om användarnamnet och lösenordet finns med i listan
 */
		public boolean checkCredentials(String user, String pass) {
			if(user.isEmpty()){
				user = "*EMPTY*";
			}else if(!(UserContainer.characterCheck(user,false))){
				user = "*INVALID_INPUT*";
			}
			 // container.test(); /*Kommentera denna rad när UserContainer inte ska testas*/
			 // log.test(); /*Kommentera denna rad när TimestampLog inte ska testas*/
			 // RootServer.getDataContainer().test();/*Kommentera denna rad när DataContainer inte ska testas*/
			HashMap<String,String> list = container.getAcceptanceList();
			System.out.println("USER: "+user+" "+"PASS: "+pass+" END ");

			System.out.println("Empty? :" + list.isEmpty()+
			"\n"+"Size: "+list.size());
			System.out.println("list.containsKey(user) :"+list.containsKey(user));
			System.out.println("list.get(user) :"+list.get(user));

			if(list.containsKey(user.hashCode()+"") && list.get(user.hashCode()+"").equals(pass.hashCode()+"")){

				System.out.println("Inloggning lyckad: "+user);
//				log.addTimestamp(user,true); /*LOGGAR INLOGGNING*/
				return true;
			}else{
				System.out.println("Inloggning misslyckad: "+user);
//				log.addTimestamp(user,false); /*LOGGAR INLOGGNING*/
				return false;
			}
		}


}
