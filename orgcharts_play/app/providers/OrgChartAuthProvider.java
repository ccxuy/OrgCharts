/*
 * Copyright © 2014 Florian Hars, nMIT Solutions GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package providers;

import java.io.UnsupportedEncodingException;

import play.Application;
import play.twirl.api.Content;
import play.mvc.Result;
import play.mvc.Http.Context;
import providers.NyGovUsernameAuthProvider.AuthInstruction;
import providers.NyGovUsernameAuthProvider.LoginResult;
import scala.reflect.internal.Trees.This;
import security.OrgChartRoleType;
import security.OrgChartUser;
import us.ny.state.cookie.CookieManager;
import us.ny.state.cookie.UserInfo;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.exceptions.AuthException;
import com.feth.play.module.pa.providers.AuthProvider;
import com.feth.play.module.pa.providers.wwwauth.WWWAuthenticateProvider;
import com.feth.play.module.pa.providers.wwwauth.basic.BasicAuthProvider;
import com.feth.play.module.pa.user.AuthUser;
import com.ning.http.util.Base64;

/** A really simple basic auth provider that accepts one hard coded user */
public class OrgChartAuthProvider extends AuthProvider {
	public static final String PROVIDER_KEY = "basic";
	OrgChartUser orgchartUser;

	public OrgChartAuthProvider(Application app) {
		super(app);
	}

	@Override
	public String getKey() {
		return PROVIDER_KEY;
	}

	protected LoginResult loginUser(final OrgChartUser authUser) {
		return LoginResult.USER_LOGGED_IN;
	}

	public static Content handleLogin(final Context ctx) {
		System.out.println("ctx = "+ctx);
		Result result = PlayAuthenticate.handleAuthentication(PROVIDER_KEY, ctx,
				AuthInstruction.LOGIN);
		System.out.println(result);
		return views.html.tmp.render("Application@loginAsUser");
	}

	/**
	 * This is only for develop purpose. Disable on production.
	 * 
	 * @param ctx
	 * @return
	 * @deprecated
	 */
	@Deprecated
	public static Result handleFakeLogin(final Context ctx, String role) {
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

	@Override
	public Object authenticate(Context context, Object payload)
			throws AuthException {
//        CookieManager cookieman = new CookieManager();
//        cookieman.injectCookies(context.session());
//        UserInfo uInfo = new UserInfo();
//        uInfo.inputCookies(cookieman);
//        // TODO: combine UserInfo type with OrgChartUser
//        OrgChartUser rcu = new OrgChartUser(uInfo);
//        if(null == payload){
//            throw new AuthException(
//                    "NyGovUsernameAuthProvider@authenticate: Null payload.");
//        } else if(payload instanceof AuthInstruction) {
//            switch ((AuthInstruction) payload) {
//                case LOGIN:
//                    break;
//                case FAKE_LOGIN_AS_ADMIN:
//                    if(context.session().containsKey("shortname")){
//                        rcu.setId(context.session().get("shortname"));
//                    }
//                    rcu.addRole(OrgChartRoleType.ADMIN);
//                case FAKE_LOGIN_AS_USER:
//                    rcu.addRole(OrgChartRoleType.USER);
//                case FAKE_LOGIN_AS_READONLY:
//                    rcu.addRole(OrgChartRoleType.READONLY);
//                default:
//                    throw new AuthException(
//                            "NyGovUsernameAuthProvider@authenticate: Not supported payload");
//            }
//        }
        // All user arrived here are authorized user so we skip verification since we don't have user data.
//		return rcu;
        NyAuthUser au = new NyAuthUser();
        if(context.session().containsKey("shortname")){
            au.setShortname(context.session().get("shortname"));
        }
        return au;
	}

	@Override
	public boolean isExternal() {
		return false;
	}
}
