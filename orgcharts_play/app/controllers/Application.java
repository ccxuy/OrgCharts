package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.feth.play.module.pa.PlayAuthenticate;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import play.*;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Http.Session;
import providers.OrgChartAuthProvider;
import security.OrgChartDeadboltHandler;
import security.OrgChartRoleType;
import security.OrgChartUser;
import utilities.HibernateUtilities;

import java.util.HashMap;
import java.util.Map;

public class Application extends Controller {

    public static Result index() {
//        return redirect("assets/index.html");
//    	return ocsDashboard();
        return redirect("manage-dashboard/");
    }

    @SubjectPresent
    public static OrgChartUser getLoginUser(final Session session) {
        final OrgChartUser ocu = OrgChartDeadboltHandler.getOrgChartUserByContext(ctx());
        return ocu;
    }

    @SubjectPresent
    public static Result ocsDashboard() {
        Long numCharts = 0L;
        Long numEmployees = 0L;
        try {
            HibernateUtilities.getFactory();
            numCharts = HibernateUtilities.countCharBytName(null);
            numEmployees = HibernateUtilities.countEmployee();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok(views.html.manageIndex.render(numCharts.toString(), numEmployees.toString()));
    }

    @Restrict({@Group(OrgChartRoleType.ADMIN), @Group(OrgChartRoleType.USER), @Group(OrgChartRoleType.READONLY)})
    public static Result ocsCharts() {
        return ok(views.html.manageChart.render());
    }

    @Restrict({@Group(OrgChartRoleType.ADMIN), @Group(OrgChartRoleType.USER), @Group(OrgChartRoleType.READONLY)})
    public static Result ocsEmployees() {
        return ok(views.html.manageEmployee.render());
    }
    
    //TODO: disable this function in real application
    @Restrict({@Group(OrgChartRoleType.ADMIN)})
    public static Result tmp() {
        System.out.println("Application@tmp");
        return ok(views.html.tmp.render("hi"));
    }
    
    //TODO: disable this function in real application
    public static Result loginAsAdmin() {
    	session().clear();
        session("fakelogin", "admin");
        session("shortname", "tony");
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
    public static Result loginAsUser2() {
        session().clear();
        session("fakelogin", "user");
        session("shortname", "arul");
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


    @Restrict({@Group(OrgChartRoleType.ADMIN)})
    public static Result getCurrentLoginUser() {
        OrgChartUser ocu = OrgChartDeadboltHandler.getOrgChartUserByContext(ctx());
        return ok(Json.toJson(ocu));
    }


    @Restrict({@Group(OrgChartRoleType.ADMIN)})
    public static Result getCookie() {
//        Logger.info(""+request().cookies().get("PermCookie").value());
        Map<String, String> cookieMap = new HashMap<>();
        Http.Cookies cookies = request().cookies();
        cookies.forEach(c -> cookieMap.put(c.name(), c.value()));
        return ok(Json.toJson(cookieMap));
    }

    public static Result logout(){
        session().clear();
//        return com.feth.play.module.pa.controllers.Authenticate.logout();
        return PlayAuthenticate.logout(session());
    }

}
