import java.io.Serializable;
import java.util.LinkedList;

import org.vertx.java.core.json.JsonArray;


public class User implements Comparable<User>,Serializable{
	private static final long serialVersionUID = -2654414564884238786L;
	
	private String name;
	private String cookie;
	private LinkedList<Message> messageQueue;
	

	public User(String username){
		name = username;
		cookie = generateNewCookie();
		messageQueue = new LinkedList<Message>();
	}
	
	@Override
	public int compareTo(User otherUser) {
		if(otherUser == null || otherUser.name == null) return 1;
		if(name == null) return -1;
		
		return name.compareTo(name);
	}
	
	public String getName(){
		return name;
	}
	
	public String getCookie(){
		return cookie;
	}
	
	public void bufferChatMessage(User user, String chatString){
		messageQueue.addLast(new Message(user, chatString));
	}
		
	public JsonArray popJSONMessageBuffer(){
		Message message;
		JsonArray messageArray = new JsonArray();
		
		while(!messageQueue.isEmpty()){
			messageArray.addObject(messageQueue.pop().toJSONObject());
		}
		
		return messageArray;
	}

	public static String generateNewCookie() {
		return Math.random()+ "";
	}

	
}
