package security;

import java.util.Collection;

import play.Logger;
import play.api.mvc.RequestHeader;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.Http.Session;
import play.mvc.Result;
import us.ny.state.cookie.CookieManager;
import us.ny.state.cookie.UserInfo;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import be.objectify.deadbolt.core.models.Subject;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUserIdentity;

public class MyDeadboltHandler extends AbstractDeadboltHandler {

	@Override
	public Promise<Result> beforeAuthCheck(final Http.Context context) {
		if (PlayAuthenticate.isLoggedIn(context.session())) {
			// user is logged in
			return F.Promise.pure(null);
		} else {
			// user is not logged in

			// call this if you want to redirect your visitor to the page that
			// was requested before sending him to the login page
			// if you don't call this, the user will get redirected to the page
			// defined by your resolver
			final String originalUrl = PlayAuthenticate
					.storeOriginalUrl(context);

			context.flash().put("error",
					"You need to log in first, to view '" + originalUrl + "'");
			return F.Promise.promise(new F.Function0<Result>() {
				@Override
				public Result apply() throws Throwable {
					return redirect(PlayAuthenticate.getResolver().login());
				}
			});
		}
	}

	@Override
	public DynamicResourceHandler getDynamicResourceHandler(Context context) {
		// TODO Auto-generated method stub
		return super.getDynamicResourceHandler(context);
	}

	/**
	 * Get current subject(OrgChartUser) of current session. 
	 * Since id is provided in cookies, we just get user information from it.
	 * @param context
	 */
	@Override
	public Promise<Subject> getSubject(Context context) {
		final AuthUserIdentity u = PlayAuthenticate.getUser(context);
		Logger.info("MyDeadboltHandler@getSubject:u.getId()="+u.getId());
		
		//Get current user here using provided cookie
		UserInfo userInfo = getUserInfo(context.session());
		// Caching might be a good idea here
		return F.Promise.pure((Subject)userInfo);
	}

	@Override
	public Promise<Result> onAuthFailure(Context context, String content) {
		// TODO Auto-generated method stub
		return super.onAuthFailure(context, content);
	}
	
	private UserInfo getUserInfo(Session session){
    	System.out.println("AuthUserFilter@getUserInfo session="+session);
        CookieManager cookieman = new CookieManager();
        cookieman.injectCookies(session);	

        UserInfo uInfo = new UserInfo();
        uInfo.inputCookies(cookieman);
        
        return uInfo;
	}

}
