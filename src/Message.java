import org.vertx.java.core.json.JsonObject;

public class Message {
	public static enum MessageType{
		CHAT_TEXT
	};
	
	private MessageType messageType;
	private String messageData;
	private User fromUser;
	
	public Message(User user, String chatString){
		fromUser = user;
		messageData = chatString;
		messageType = MessageType.CHAT_TEXT;
	}
	
	public JsonObject toJSONObject(){
		JsonObject jsonObject = new JsonObject();
		jsonObject.putString("username", fromUser.getName());
		jsonObject.putString("messageData",messageData);
		return jsonObject;
	}
	
}
