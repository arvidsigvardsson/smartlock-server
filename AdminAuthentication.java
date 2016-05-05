import com.sun.net.httpserver.BasicAuthenticator;

public class AdminAuthentication extends BasicAuthenticator{
	
		
		
		public AdminAuthentication(String message) {
		super(message);
	}

		public boolean checkCredentials(String user, String pass) {
			System.out.println("USER: "+user+" "+"PASS: "+pass+" END ");
			if(user.equals("admin") && pass.equals("bir")){
				return true;
			}else{
				return false;
			}
		}

}
