package us.ny.state.cookie;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.Cookie;

import play.Logger;
import play.api.mvc.Cookies;
import play.api.mvc.RequestHeader;
import play.mvc.Http;
import play.mvc.Http.HeaderNames;
import play.mvc.Http.Session;


public class CookieManager {

	private Map cookieMaps = new TreeMap();

	/**
	 * inject our cookies
	 * we only process two cookies and their delimiters
	 */
	public void injectCookies(Cookie[] cookies){
		if (cookies != null) {
			for (int c=0;c<cookies.length;c++){
				Cookie cookie = cookies[c];
                //System.out.println("Cookie Name :"+cookie.getName());
				Logger.debug("Cookie Name :"+cookie.getName());
				if (cookie != null && cookie.getName().equalsIgnoreCase("PermCookie")){
					this.addCookieMap(cookie, "\\|", ":");
				} else if (cookie != null && cookie.getName().equalsIgnoreCase("WaarpCookie")){
					this.addCookieMap(cookie, "%7C", "%3A");
				}
			}
		}
	}


	/**
	 * inject our cookies
	 * we only process two cookies and their delimiters
	 */
	public void injectCookies(Http.Cookies cookies) {
		if (cookies != null) {
			for(Http.Cookie c : cookies){
				if(c.name().equalsIgnoreCase("PermCookie")){
					this.addCookieMap(c, "\\|", ":");
				}else if(c.name().equalsIgnoreCase("WaarpCookie")){
					this.addCookieMap(c, "%7C", "%3A");
				}
			}
		}
	}


	/**
	 * Yet another implementation for injecting cookies directly from a map!
	 * This function is designed for Play Framework lol!
	 */
//	public void injectCookies(Session session) {
////		if (cookie.get("PermCookie")!= null){
////			Map cookieMap = getCookieNameValueMap(cookie.get("PermCookie").get().value(), primary_del, second_del);
////			cookieMaps.put(cookieName, cookieMap);
////			this.addCookieMap(cookie, "\\|", ":");
////		} else if (cookie != null && cookie.getName().equalsIgnoreCase("WaarpCookie")){
////			this.addCookieMap(cookie, "%7C", "%3A");
////		}
//
////        Logger.debug("CookieManager@injectCookies session="+session);
//		if (session != null) {
//			for (String keyString : session.keySet()){
////                System.out.println("session.keySet().keyString="+keyString);
//				if (keyString.equalsIgnoreCase("PermCookie")){
//					this.addCookieMap(session, "\\|", ":");
//				} else if (keyString.equalsIgnoreCase("WaarpCookie")){
//					this.addCookieMap(session, "%7C", "%3A");
//				}
//			}
//		}
//	}


	/**
	 * TODO: Yet another implementation for injecting cookies directly from a map!
	 * This function is designed for Play Framework lol!
	 * 
	 */
//	public void injectCookies(Map cMap){
//		if (cMap != null) {
//			this.cookieMaps.putAll(cMap);
//		}
//	}


	private void addCookieMap(Session session, String primary_del,
			String second_del) {
		for(String key : session.keySet()){
			Map cookieMap = getCookieNameValueMap(session.get(key), primary_del, second_del);
			// add the CookieNameValueMap to the Map
			cookieMaps.put(key, cookieMap);
		}
	}


	/**
	 * parse cookies array and put them in the map
	 * @param cookies
	 */
	public void addCookieMap (Cookie[] cookies, String primary_del, String second_del){
		for (int c=0;c<cookies.length;c++){
			Cookie cookie = cookies[c];
			addCookieMap(cookie, primary_del, second_del);
		}
	}

	/**
	 * parse a cookie and put in the map after diseg it
	 * @param cookie
	 */
	public void addCookieMap(Cookie cookie, String primary_del, String second_del){

		String cookieName = cookie.getName();
		String cookieValue = cookie.getValue();
        //System.out.println(cookieName+" : "+cookieValue);
		// get CookieMap
		Map cookieMap = getCookieNameValueMap(cookieValue, primary_del, second_del);

		// add the CookieNameValueMap to the Map
		cookieMaps.put(cookieName, cookieMap);
	}

	public void addCookieMap(Http.Cookie cookie, String primary_del, String second_del){

		String cookieName = cookie.name();
		String cookieValue = cookie.value();
		//System.out.println(cookieName+" : "+cookieValue);
		// get CookieMap
		Map cookieMap = getCookieNameValueMap(cookieValue, primary_del, second_del);

		// add the CookieNameValueMap to the Map
		cookieMaps.put(cookieName, cookieMap);
	}


	/**
	 * parse Cookie such as
	 * PCVERS:2|STAGE:DEV|HINUSER:aft94272|HINAPPWD:aneasy1|HINAPDBID:none|HINAPLID:uphn|HINPROWS:0|
	 * into name-value map
	 * @param cookieValue
	 * @return
	 */
	private Map getCookieNameValueMap(String cookieValue, String primary_del, String second_del){

		Map map = new TreeMap();

		String[] cookie_splits = cookieValue.split(primary_del);

		for (int c=0;c<cookie_splits.length;c++){
			String cookie_split = cookie_splits[c];
			String[] n_v = cookie_split.split(second_del);
			if (n_v.length >0) {
				String n = n_v[0];
				String v = "";
				if (n != null && !n.equals("")) {
					if (n_v.length > 1){
						v = n_v[1];
						v = decodeCookieValue(v);
					}
					map.put(n, v);
				}

			}
		}

		return map;
	}

	/**
	 * get CookieMap based on cookieName
	 * @param cookieName
	 * @return
	 */
	public Map getCookieMap(String cookieName){
		return (Map) cookieMaps.get(cookieName) ;
	}

	private String decodeCookieValue(String input){
		if (input != null){
			input = input.replaceAll("%20", " ");
			input = input.replaceAll("%24", "$");
			input = input.replaceAll("%28", "(");
			input = input.replaceAll("%30", "0");
			input = input.replaceAll("%29", ")");
			input = input.replaceAll("%40", "@");
		}
		return input;
	}

	/**
	 *
	 * @param cookieName
	 * @param fieldName
	 * @return
	 */
	public String getParamFromCookie(String cookieName, String fieldName){

		String fieldValue = "";

		Map cookieMap  = getCookieMap(cookieName);
		if (cookieMap != null) {
			Object fieldValueObj = cookieMap.get(fieldName);
			if (fieldValueObj != null) fieldValue = (String) fieldValueObj;
		}

		return fieldValue;

	}

	public static void main(String[] args) {

		CookieManager cm = new CookieManager();
//		System.out.println(cm.getCookieNameValueMap("PCVERS:2|STAGE:DEV|HINUSER:aft94272|HINAPPWD:aneasy1|HINAPDBID:none|HINAPLID:uphn|HINPROWS:0|", "\\|", ":").toString());
//		System.out.println(cm.getCookieNameValueMap("TICKET%3A274507C50F1D19C9D4BFC4F1857B86AE9BDCE4FA55D54A5922F42CF040FD2B792F358A8C47A981E4%7CWCVERS%3A2%7CFULLNAME%3A%7CEMAIL%3A%7CORG%3AZ%20Test%20Hospital%7CPHONE%3A%7CPHONEEXT%3A%7CCOMMERCEID%3Aaft94272%7C","%7C", "%3A").toString());
//		Logger.debug(cm.getCookieNameValueMap("PCVERS:2|STAGE:DEV|HINUSER:aft94272|HINAPPWD:aneasy1|HINAPDBID:none|HINAPLID:uphn|HINPROWS:0|", "\\|", ":").toString());
//		Logger.debug(cm.getCookieNameValueMap("TICKET%3A274507C50F1D19C9D4BFC4F1857B86AE9BDCE4FA55D54A5922F42CF040FD2B792F358A8C47A981E4%7CWCVERS%3A2%7CFULLNAME%3A%7CEMAIL%3A%7CORG%3AZ%20Test%20Hospital%7CPHONE%3A%7CPHONEEXT%3A%7CCOMMERCEID%3Aaft94272%7C","%7C", "%3A").toString());
		System.out.println(cm.getCookieNameValueMap("PCVERS:2|STAGE:DEV|HINUSER:aft94272|HINAPPWD:aneasy1|HINAPDBID:none|HINAPLID:uphn|HINPROWS:0|", "\\|", ":"));
		System.out.println(cm.getCookieNameValueMap("TICKET%3A274507C50F1D19C9D4BFC4F1857B86AE9BDCE4FA55D54A5922F42CF040FD2B792F358A8C47A981E4%7CWCVERS%3A2%7CFULLNAME%3A%7CEMAIL%3A%7CORG%3AZ%20Test%20Hospital%7CPHONE%3A%7CPHONEEXT%3A%7CCOMMERCEID%3Aaft94272%7C","%7C", "%3A"));

		UserInfo uInfo = new UserInfo();
        uInfo.inputCookies(cm);
        System.out.println(uInfo);
	}

}

