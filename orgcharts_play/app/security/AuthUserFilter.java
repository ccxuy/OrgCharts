package security;

import java.util.List;

import play.Logger;
import play.mvc.Http.HeaderNames;
import play.api.libs.iteratee.Iteratee;
import play.api.mvc.EssentialAction;
import play.api.mvc.EssentialFilter;
import play.api.mvc.RequestHeader;
import play.api.mvc.Result;
import play.api.mvc.Session;
import play.libs.F.Promise;
import play.mvc.SimpleResult;
import play.mvc.Http.Context;
import scala.Function1;
import scala.Option;
import scala.runtime.AbstractFunction1;
import us.ny.state.cookie.CookieManager;
import us.ny.state.cookie.HinPerm;
import us.ny.state.cookie.UserInfo;

public class AuthUserFilter implements EssentialFilter {

	@Override
	public EssentialAction apply(final EssentialAction next) {
		return new AuthAction() {

			@Override
			public EssentialAction apply() {
				return next.apply();
			}

			@Override
			public Iteratee<byte[], Result> apply(RequestHeader request) {
				//TODO: Handle by official cookie manager
//				request.cookies()
//				Session session = (Session) Session.decodeFromCookie(request
//						.headers().get(HeaderNames.COOKIE));
//                Session session = request.session();
//				Logger.info("AuthUserFilter@apply<<AuthAction@apply session.data="+session.data().toString());
				// Try to print details of cookies
//				System.out.println("AuthUserFilter:cookies().mkString()="+request.cookies().mkString());
//				System.out.println("request.getQueryString(\"role\")="+request.getQueryString("role"));
				
//				UserInfo userInfo = getUserInfoBySession(request);
//				if(null!=userInfo){
//					HinPerm[] hPerm = userInfo.getHinperms();
//					for(HinPerm hp : hPerm){
//						System.out.print(hp.getRole());
//					}
//				}
				//Next: build your own user object...
				return next.apply(request);
			}
		};
	}

	public abstract class AuthAction extends
			AbstractFunction1<RequestHeader, Iteratee<byte[], Result>>
			implements EssentialAction {
	}
	
//    private UserInfo getUserInfoBySession(RequestHeader req){
//    	System.out.println("AuthUserFilter@getUserInfoBySession "+req.headers().get("Cookie"));
//        CookieManager cookieman = new CookieManager();
//        cookieman.injectCookies(req);	
//
//        UserInfo uInfo = new UserInfo();
//        uInfo.inputCookies(cookieman);
//        
//        return uInfo;
//	}

}
