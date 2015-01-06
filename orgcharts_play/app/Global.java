import java.lang.reflect.Method;
import java.util.Arrays;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.PlayAuthenticate.Resolver;
import com.feth.play.module.pa.exceptions.AccessDeniedException;
import com.feth.play.module.pa.exceptions.AuthException;

import controllers.routes;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.mvc.Action;
import play.mvc.Call;
import play.mvc.Http.Request;
import play.api.mvc.EssentialFilter;
import security.AuthUserFilter;

public class Global extends GlobalSettings {
    @Override
    public <T extends EssentialFilter> Class<T>[] filters() {
        return new Class[]{AuthUserFilter.class};
    }

	@Override
	public Action onRequest(Request request, Method actionMethod) {
		// TODO Check user role in EssentialFilter or here?
		return super.onRequest(request, actionMethod);
	}

	@Override
	public void onStart(Application app) {
		// TODO get things ready... load role?
		Logger.info("Global@onStart: Application has started");
		PlayAuthenticate.setResolver(new Resolver(){

			@Override
			public Call afterAuth() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Call afterLogout() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Call askLink() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Call askMerge() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Call auth(final String provider) {
				return com.feth.play.module.pa.controllers.routes.Authenticate
						.authenticate(provider);
			}

			@Override
			public Call login() {
				// TODO Auto-generated method stub
				return null;
			}
			
		});
		
		super.onStart(app);
	}
	
}