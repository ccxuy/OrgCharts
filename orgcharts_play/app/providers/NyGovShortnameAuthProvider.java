package providers;

import play.Application;
import play.twirl.api.Content;
import play.Logger;
import play.api.mvc.Session;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.Result;
import security.OrgChartRoleType;
import security.OrgChartUser;
import us.ny.state.cookie.CookieManager;
import us.ny.state.cookie.UserInfo;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.exceptions.AuthException;
import com.feth.play.module.pa.providers.AuthProvider;

/**
 * This provider is not use any more, use OrgChartAuthProvider instead.
 * @author yxx03
 * @deprecated
 */
@Deprecated
public abstract class NyGovShortnameAuthProvider
		extends AuthProvider {
//	public static final String PROVIDER_KEY = "gov.ny.orgchart.user.id";
	public static final String PROVIDER_KEY = "basic";

	public NyGovShortnameAuthProvider(final Application app) {
		super(app);
	}

	/**
	 * 
	 * @param context
	 * @param payload
	 *            Some arbitrary payload that shall get passed into the
	 *            authentication process
	 *            String: Some authentication instruction.
	 * @return either an AuthUser object or a String (URL)
	 * @throws AuthException
	 */
	@Override
	public Object authenticate(Context context, Object payload)
			throws AuthException {
		CookieManager cookieman = new CookieManager();
		cookieman.injectCookies(context.session());
		UserInfo uInfo = new UserInfo();
		uInfo.inputCookies(cookieman);
		// TODO: combine UserInfo type with OrgChartUser
//		OrgChartUser rcu = new OrgChartUser();
		
		return new NyAuthUser();
	}

	@Override
	public String getKey() {
		return PROVIDER_KEY;
	}

	@Override
	public boolean isExternal() {
		return false;
	}

	public static Result handleLogin(final Context ctx) {
		return PlayAuthenticate.handleAuthentication(PROVIDER_KEY, ctx,
				"");
	}

}
