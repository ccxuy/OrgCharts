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
		super.onStart(app);
	}
	
}