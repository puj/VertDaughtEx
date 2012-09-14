import java.util.Set;

import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieDecoder;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Verticle;

public class Server extends Verticle {

	private String AUTH_COOKIE_NAME = "VERTX_COOKIE";
	
	public void start() {
		HttpServer server = vertx.createHttpServer();
		RouteMatcher routeMatcher = new RouteMatcher();
		setRouts(routeMatcher);
		server.requestHandler(routeMatcher).listen(8008);
	}

	private void setRouts(RouteMatcher routeMatcher) {
		routeMatcher.get("/", new Handler<HttpServerRequest>() {

			@Override
			public void handle(HttpServerRequest req) {
				req.response.sendFile("index.html");
			}
		});

		routeMatcher.get("/enter", new Handler<HttpServerRequest>() {
			
			@Override
			public void handle(HttpServerRequest req) {
				User user = UserController.getInstance().addUser(req.params().get("nick"));
				req.response.headers().put("Set-Cookie",AUTH_COOKIE_NAME + "="  + user.getCookie());
				sendRedirect(req, "/chat");
			}

		});

		routeMatcher.get("/chat", new Handler<HttpServerRequest>() {

			@Override
			public void handle(HttpServerRequest req) {
				req.response.sendFile("chat.html");
			}

		});
		
		routeMatcher.get("/updates", new Handler<HttpServerRequest>() {
			@Override
			public void handle(HttpServerRequest req) {
				final User fromUser = getUserFromRequest(req);
				if(fromUser == null){
					// This user is no longer known to us and we should either reauthenticate
					//  or just quit..
					sendUnauthorized(req);
					return;
				}
				
				JsonArray messageArray = fromUser.popJSONMessageBuffer();
				JsonObject responseObject = new JsonObject();
				responseObject.putArray("messages", messageArray);
				req.response.end(responseObject.encode());
			}

		});
		
		routeMatcher.post("/message", new Handler<HttpServerRequest>() {

			@Override
			public void handle(HttpServerRequest req) {
				System.err.println("Trying to handle /message...");
				
				final User fromUser = getUserFromRequest(req);
				if(fromUser == null){
					// This user is no longer known to us and we should either reauthenticate
					//  or just quit..
					sendUnauthorized(req);
					return;
				}
				
				// Handle the message and broadcast it to all users
				req.bodyHandler(new Handler<Buffer>() {
					@Override
					public void handle(Buffer buff) {
						String message = getMessageFromBuffer(buff);
						UserController.getInstance().broadcast(fromUser, message);
					}
				});
				req.response.end();
			}

		});

		routeMatcher.get("/users", new Handler<HttpServerRequest>() {
			
			@Override
			public void handle(HttpServerRequest req) {
				JsonObject object = new JsonObject();
				JsonArray json = new JsonArray(UserController.getInstance().getUsersAsObjectList());
				object.putArray("users", json);
				req.response.headers().put("Content-Type","application/json");
				req.response.end(object.encode());

			}

		});

		routeMatcher.get("/js/:filename", new Handler<HttpServerRequest>() {

			@Override
			public void handle(HttpServerRequest req) {
				req.response.sendFile("js/" + req.params().get("filename"));
			}

		});

		routeMatcher.get("/css/:filename", new Handler<HttpServerRequest>() {

			@Override
			public void handle(HttpServerRequest req) {
				req.response.sendFile("css/" + req.params().get("filename"));
			}

		});
	}
	
	private String getMessageFromBuffer(Buffer buffer){
		return buffer.toString();
	}

	private void sendRedirect(HttpServerRequest req, String url) {
		req.response.putHeader("Location", url);
		req.response.statusCode = 302;
		req.response.end();
	}
	
	private void sendUnauthorized(HttpServerRequest req){
		req.response.statusCode = 401;
		req.response.end();
	}
	
	
	private User getUserFromRequest(HttpServerRequest req){
		String cookieLookupValue = null;
		String value = req.headers().get("Cookie");
		Set<Cookie> cookies = new CookieDecoder().decode(value);
		for (final Cookie cookie : cookies) {
			if (AUTH_COOKIE_NAME.equals(cookie.getName())) {
				cookieLookupValue = cookie.getValue();
			}
		}
		// Invalid cookie, no session
		if(cookieLookupValue == null){
			return null;
		}
		
		final User fromUser = UserController.lookupUserByCookie(cookieLookupValue);
		if(fromUser == null){
			return null;
		}
		
		return fromUser;
	}
}
