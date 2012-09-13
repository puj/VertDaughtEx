import java.util.HashSet;
import java.util.Set;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.deploy.Verticle;


public class Server extends Verticle {

	private static Set<String> urls;
	
	static {
		urls = new HashSet<>();
		urls.add("/");
	}

	public void start() {
		vertx.createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
				public void handle(HttpServerRequest req) {
					String page = UrlHandler.resolve(req.path);
					System.out.println("the path=\"" + req.path + "\"");
					req.response.sendFile(page);
				}
			}).listen(8008);
	}
}

