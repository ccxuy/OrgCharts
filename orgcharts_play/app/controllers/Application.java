package controllers;

import com.feth.play.module.pa.PlayAuthenticate;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import play.*;
import play.mvc.*;
import play.mvc.Http.Session;
import providers.NyAuthUser;
import providers.NyGovShortnameAuthProvider;
import providers.OrgChartAuthProvider;
import providers.NyGovUsernameAuthProvider;
import scala.reflect.internal.Trees.This;
import security.OrgChartRoleType;
import security.OrgChartUser;
import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return redirect("assets/index.html");
    }
    
    //TODO: disable this function in real application
    @Restrict(@Group(OrgChartRoleType.ADMIN))
    public static Result tmp() {
        System.out.println("Application@tmp");
        return ok(views.html.tmp.render("hi"));
    }
    
    //TODO: disable this function in real application
    public static Result loginAsAdmin() {
    	session().clear();
        session("fakelogin", "admin");
        session("shortname", "Tony");
//        return PlayAuthenticate.loginAndRedirect(ctx(),new NyAuthUser());
        OrgChartAuthProvider.handleFakeLogin(ctx(), OrgChartRoleType.ADMIN.toString());
        Logger.debug("Application@loginAsAdmin isLoggedIn="+PlayAuthenticate.isLoggedIn(session()));
        Logger.debug("Application@loginAsAdmin getUser="+PlayAuthenticate.getUser(session()));
//    	return ok(OrgChartAuthProvider.handleFakeLogin(ctx(), OrgChartRoleType.ADMIN.toString()));
//    	return ok(OrgChartAuthProvider.handleLogin(ctx()));
//    	return NyGovShortnameAuthProvider.handleLogin(ctx());
//    	return NyGovUsernameAuthProvider.handleFakeLogin( ctx(), "admin" );
//    	PlayAuthenticate.handleAuthentication(NyGovUsernameAuthProvider.PROVIDER_KEY, ctx(), arg2);
      return ok(views.html.tmp.render("Application@loginAsAdmin"));
    }
    
    //TODO: disable this function in real application
    public static Result loginAsUser() {
        return ok(views.html.tmp.render("Application@loginAsUser"));
    }
    
    //TODO: disable this function in real application
    public static Result loginAsReadonly() {
        return ok(views.html.tmp.render("Application@loginAsReadonly"));
    }

}
