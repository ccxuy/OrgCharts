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
public abstract class NyGovUsernameAuthProvider
		extends AuthProvider {
//	public static final String PROVIDER_KEY = "gov.ny.orgchart.user.id";
	public static final String PROVIDER_KEY = "basic";

	protected enum LoginResult {
		USER_UNVERIFIED, USER_LOGGED_IN, NOT_FOUND
	}
	
	protected enum AuthInstruction{
		LOGIN, LOGOUT, FAKE_LOGIN_AS_ADMIN, FAKE_LOGIN_AS_USER, FAKE_LOGIN_AS_READONLY
	}

	public NyGovUsernameAuthProvider(final Application app) {
		super(app);
		Registry.register(getKey(), this);
		Logger.debug(String.valueOf(Registry.hasProvider(this.PROVIDER_KEY)));
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
		cookieman.injectCookies(context.request().cookies());
		UserInfo uInfo = new UserInfo();
		uInfo.inputCookies(cookieman);
		// TODO: combine UserInfo type with OrgChartUser
		OrgChartUser rcu = new OrgChartUser(uInfo);

		if(null == payload){
			throw new AuthException(
					"NyGovUsernameAuthProvider@authenticate: Null payload.");
		} else if(payload instanceof AuthInstruction){
			switch ((AuthInstruction)payload) {
			case LOGIN:
				break;
			case FAKE_LOGIN_AS_ADMIN:
				rcu.addRole(OrgChartRoleType.ADMIN);
			case FAKE_LOGIN_AS_USER:
				rcu.addRole(OrgChartRoleType.USER);
			case FAKE_LOGIN_AS_READONLY:
				rcu.addRole(OrgChartRoleType.READONLY);
			default:
				throw new AuthException(
						"NyGovUsernameAuthProvider@authenticate: Not supported payload");
			}

			final LoginResult r = loginUser(rcu);
			switch (r) {
			case USER_UNVERIFIED:
				// Should not happen, user access this app is verified, route to login page
				break;
			case USER_LOGGED_IN:
				return rcu;
			case NOT_FOUND:
				// Should not happen, user access this app is verified, route to login page
				break;
			default:
				throw new AuthException(
						"NyGovUsernameAuthProvider@authenticate: Something in login went wrong");
			}
		}
		//TODO: add go to login url here.
		throw new AuthException(
				"NyGovUsernameAuthProvider@authenticate: Unexpected payload.");
		// Http.Context.current.set(context);
		// return null;
	}

	@Override
	public String getKey() {
		return PROVIDER_KEY;
	}

	@Override
	public boolean isExternal() {
		return false;
	}

	protected LoginResult loginUser(final OrgChartUser authUser) {
		return LoginResult.USER_LOGGED_IN;
	}

	public static Result handleLogin(final Context ctx) {
		return PlayAuthenticate.handleAuthentication(PROVIDER_KEY, ctx,
				AuthInstruction.LOGIN);
	}
	
	/**
	 * This is only for develop purpose. Disable on production.
	 * @param ctx
	 * @return
	 * @deprecated
	 */
	@Deprecated
	public static Result handleFakeLogin(final Context ctx, String role){
		switch (role) {
		case "admin":
			return PlayAuthenticate.handleAuthentication(PROVIDER_KEY, ctx,
					AuthInstruction.FAKE_LOGIN_AS_ADMIN);
		case "user":
			return PlayAuthenticate.handleAuthentication(PROVIDER_KEY, ctx,
					AuthInstruction.FAKE_LOGIN_AS_USER);
		case "readonly":
			return PlayAuthenticate.handleAuthentication(PROVIDER_KEY, ctx,
					AuthInstruction.FAKE_LOGIN_AS_READONLY);
		default:
			return PlayAuthenticate.handleAuthentication(PROVIDER_KEY, ctx,
					AuthInstruction.FAKE_LOGIN_AS_ADMIN);
		}
	}
	

	/** The 401 page to return to the browser if authentication failed.
	 *
	 * This could for example be a login form that submits to another
	 * authentication method.
	 *
	 * @param context The current request context
	 * @return The formatted unauthorized page
	 */
	protected Content unauthorized(Context context) {
		return new Content() {

			@Override
			public String body() {
				return "Go away, you don't exit.";
			}

			@Override
			public String contentType() {
				return "text/plain";
			}};
	}

}
