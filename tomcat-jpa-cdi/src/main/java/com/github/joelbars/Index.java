package com.github.joelbars;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * Backbean class for the index page to test CDI injection.
 * 
 * @author joel
 */
@Named
@RequestScoped
public class Index {

    public String getHello() {
        // Database db = new Database(true);
        // db.query("select o from User o").multi(User.class);
        return "hello there!";
    }
}
