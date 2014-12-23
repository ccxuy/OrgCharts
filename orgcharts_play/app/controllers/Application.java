package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return redirect("assets/index.html");
    }
    
    //TODO: disable this function in real application
    public static Result tmp() {
        return ok(views.html.tmp.render("hi"));
    }

}
