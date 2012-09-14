import java.util.ArrayList;
import java.util.List;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Verticle;

public class Server extends Verticle {

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
				UserController.getInstance().addUser(req.params().get("nick"));
				sendRedirect(req, "/chat");
			}

		});

		routeMatcher.get("/chat", new Handler<HttpServerRequest>() {

			@Override
			public void handle(HttpServerRequest req) {
				req.response.sendFile("chat.html");
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

	private void sendRedirect(HttpServerRequest req, String url) {
		req.response.putHeader("Location", url);
		req.response.statusCode = 302;
		req.response.end();
	}
}
