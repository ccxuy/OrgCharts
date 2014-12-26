package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import play.*;
import play.mvc.*;
import security.OrgChartRoleType;
import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return redirect("assets/index.html");
    }
    
    //TODO: disable this function in real application
    @Restrict(@Group(OrgChartRoleType.ADMIN))
    public static Result tmp() {
        return ok(views.html.tmp.render("hi"));
    }

}
