package com.github.joelbars.listener;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class DatabaseFactory implements ServletContextListener {

    private static EntityManagerFactory emf;

    @Override
    public void contextDestroyed(ServletContextEvent ev) {
        emf.close();
    }

    @Override
    public void contextInitialized(ServletContextEvent ev) {
        emf = Persistence.createEntityManagerFactory("app");
    }

    public static EntityManager createEntityManager() {
        if (emf == null) { throw new IllegalStateException("Context is not initialized yet."); }

        return emf.createEntityManager();
    }
}
