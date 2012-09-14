import java.util.HashSet;
import java.util.List;


public class UserController {
	private final String logtag = getClass().getSimpleName();
	
	private HashSet<User> userList;
	private static UserController instance;
	
	
	public UserController(){
		userList = new HashSet<User>();
	}
	
	public static UserController getInstance(){
		if(instance == null){
			instance  = new UserController();
		}
		return instance;
	}
	
	public void addUser(String username){
		User newUser = new User(username);
		if(userList.contains(newUser)){
			log(logtag  + ": Username (" + username + ") already in use");
		}else{
			userList.add(newUser);
			log(logtag  + ": Username now in userlist");
		}
	}
	
	//TODO make a logmanager or some shit
	public void log(String logOutput){
		System.err.println(logOutput);
	}

	public Object[] getUsersAsObjectList() {
		return userList.toArray();
	}
}
