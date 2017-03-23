package com.aiconoa.trainings.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class HashCodeChecker {
   
    @PersistenceContext(unitName = "event")
    private EntityManager em;

    public HashCodeChecker() {
        super();
    }
    
    public boolean checkHashCode(String hashCode) {
        if (hashCode == "") {
            return false;
        }
        String jpql = "SELECT COUNT(s) FROM Subscriber s WHERE s.hashCode=:hashCode";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter("hashCode", hashCode);
        if (query.getSingleResult() != 1L) {
            return false;
        }
        return true;
    }
}