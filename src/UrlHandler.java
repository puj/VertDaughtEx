import java.io.File;



public class UrlHandler {

	public static String resolve(String url) {
		if(url == null){
			System.out.println("FUCK");
			throw new IllegalArgumentException();
		}

		switch(url){
		case "":
			return "index.html";
		case "/":
			return "index.html";
		case "/favicon.ico":
			return "";
		case "/chat":
			return "chat.html";
		default:
			url = url.substring(1);
			boolean authenticatedAsIs = doAuthenticationHere(url);
			if(authenticatedAsIs){
				return url;
			}else{
				return "404.html";
			}
		}
	}

	private static boolean doAuthenticationHere(String url) {
		
		if((new File(url)).exists()){
			System.err.println(url + " exists");
			return true;
		}
		System.err.println(url + " does not exist");
		return false;
	}
	
	
}