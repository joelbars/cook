package com.github.joelbars.service;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.joelbars.listener.DatabaseFactory;
import com.github.joelbars.model.Domain;

/**
 * Class for manage database transactions.
 * 
 * @author joel
 */
public class Database {

    private static volatile boolean initialized = false;
    private static Boolean lock = new Boolean(true);

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private EntityManager outer;

    /**
     * Creates a new instance with transaction support.
     */
    public Database() {
        this(false);
    }

    /**
     * Creates a new instance and if @param readonly is specified no JPA transaction is started, you can still persist
     * stuff, but the entities won't become managed.
     */
    public Database(boolean readOnly) {
        initialize();
        openEm(readOnly);
    }

    public void openEm(boolean readOnly) {
        if (outer != null) { return; }

        outer = DatabaseFactory.createEntityManager();

        if (readOnly == false) {
            outer.getTransaction().begin();
        }
    }

    /**
     * Get the outer transaction; an active transaction must already exist for this to succeed.
     */
    public EntityManager getActiveEm() {
        if (outer == null) { throw new IllegalStateException("No transaction was active!"); }

        return outer;
    }

    /**
     * Close the entity manager, properly committing or rolling back a transaction if one is still active.
     */
    public void closeEm() {
        if (outer == null) { return; }

        try {
            if (outer.getTransaction().isActive()) {

                if (outer.getTransaction().getRollbackOnly()) {
                    outer.getTransaction().rollback();
                } else {
                    outer.getTransaction().commit();
                }
            }
        } finally {
            outer.close();
            outer = null;
        }
    }

    /**
     * Mark the transaction as rollback only, if there is an active transaction to begin with.
     */
    public void markRollback() {

        if (outer != null) {
            outer.getTransaction().setRollbackOnly();
        }
    }

    public boolean isRollbackOnly() {
        return outer != null && outer.getTransaction().getRollbackOnly();
    }

    // thread safe way to initialize the entity manager factory.
    private void initialize() {

        if (initialized) { return; }

        synchronized (lock) {

            if (initialized) { return; }

            initialized = true;
            //
            // try {
            // emf = Persistence.createEntityManagerFactory("sisgecont");
            // } catch (Throwable t) {
            // logger.error("Failed to setup persistence unit!", t);
            // return;
            // }
        }
    }

    public Q query(String q) {
        if (outer == null) { throw new IllegalStateException("Creating a query when there is no active transaction!"); }

        return new Q(outer.createQuery(q));
    }

    public <T extends Domain> T save(T obj) {

        if (outer == null) { throw new IllegalStateException("Creating a query when there is no active transaction!"); }

        if (obj.getId() > 0L) {
            return outer.merge(obj);
        } else {
            outer.persist(obj);
            return obj;
        }
    }

    public <T> T find(Class<T> clz, Long id) {
        if (outer == null) { throw new IllegalStateException("No transaction was active!"); }
        return outer.find(clz, id);
    }

    @SuppressWarnings("unchecked")
    public <T extends Domain> T refresh(T obj) {
        if (outer == null) { throw new IllegalStateException("No transaction was active!"); }
        return (T) outer.find(obj.getClass(), obj.getId());
    }
}