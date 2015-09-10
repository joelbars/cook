package com.github.joelbars.service;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

public class Q {

    private Query q;

    public Q(Query q) {
        this.q = q;
    }

    public Q par(String pname, Object v) {
        q.setParameter(pname, v);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T single(Class<T> clazz) {
        try {
            return (T) q.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> multi(Class<T> clazz) {
        return (List<T>) q.getResultList();
    }
}
