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
import security.OrgChartDeadboltHandler;
import security.OrgChartRoleType;
import security.OrgChartUser;
import views.html.*;

public class Application extends Controller {

    public static Result index() {
//        return redirect("assets/index.html");
//    	return ocsDashboard();
        return redirect("manage-dashboard/");
    }

    public static OrgChartUser getLoginUser(final Session session) {
        final OrgChartUser ocu = OrgChartDeadboltHandler.getOrgChartUserBySession(session());
        return ocu;
    }
    
    public static Result ocsDashboard() {
        int numCharts = 0;
        int numEmployees = 0;
        return ok(views.html.manageIndex.render());
    }
    
    public static Result ocsCharts() {
        return ok(views.html.manageChart.render());
    }
    
    public static Result ocsEmployees() {
        return ok(views.html.manageEmployee.render());
    }
    
    //TODO: disable this function in real application
    @Restrict({@Group(OrgChartRoleType.ADMIN),@Group(OrgChartRoleType.USER)})
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
        session().clear();
        session("fakelogin", "user");
        session("shortname", "yxx03");
        OrgChartAuthProvider.handleFakeLogin(ctx(), OrgChartRoleType.USER.toString());
        return ok(views.html.tmp.render("Application@loginAsUser"));
    }
    
    //TODO: disable this function in real application
    public static Result loginAsReadonly() {
        session().clear();
        session("fakelogin", "readonly");
        session("shortname", "jin");
        OrgChartAuthProvider.handleFakeLogin(ctx(), OrgChartRoleType.READONLY.toString());
        return ok(views.html.tmp.render("Application@loginAsReadonly"));
    }

}
