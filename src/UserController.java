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
	
	public User addUser(String username){
		User newUser = new User(username);
		if(userList.contains(newUser)){
			log(logtag  + ": Username (" + username + ") already in use");
		}else{
			userList.add(newUser);
			log(logtag  + ": Username now in userlist");
		}
		return newUser;
	}
	
	//TODO make a logmanager or some shit
	public void log(String logOutput){
		System.err.println(logOutput);
	}

	public Object[] getUsersAsObjectList() {
		String[] userNames = new String[userList.size()];
		int i = 0 ;
		for(User user : userList){
			userNames[i++] = user.getName();
		}
		return userNames;
	}

	public void broadcast(User fromUser, String message) {
		for(User user : userList){
			user.bufferChatMessage(fromUser, message);
		}
	}
	
	public static User lookupUserByCookie(String cookieLookupValue) {
		for(User user : getInstance().userList){
			
			if(user.getCookie().compareTo(cookieLookupValue) == 0){
				return user;
			}
		}
		return null;
	}
}
