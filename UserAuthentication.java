import java.util.HashMap;

import com.sun.net.httpserver.BasicAuthenticator;

public class UserAuthentication extends BasicAuthenticator{
	    private UserContainer container = new UserContainer("filer/userList.txt");
	
		public UserAuthentication(String message) {
		super(message);
	}

		public boolean checkCredentials(String user, String pass) {

			HashMap<String,String> list = container.getAcceptanceList();
			System.out.println("USER: "+user+" "+"PASS: "+pass+" END ");
			
			System.out.println("Empty? :" + list.isEmpty()+
			"\n"+"Size: "+list.size());
			System.out.println("list.containsKey(user) :"+list.containsKey(user));
			System.out.println("list.get(user) :"+list.get(user));
			
			if(list.containsKey(user) && list.get(user).equals(pass)){
				return true;
			}else{
				return false;
			}
		}


}
