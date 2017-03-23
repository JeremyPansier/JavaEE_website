package com.aiconoa.trainings.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.aiconoa.trainings.entity.Author;

@Stateless
public class LoginService {

    @PersistenceContext(unitName = "event")
    private EntityManager em;
    private static final String USERNAME = "username";

    public LoginService() {
        super();
    }

    public boolean checkIfAuthorExist(String username) {
        String jpql = "SELECT a.username FROM Author a WHERE a.username=:username";
        TypedQuery<String> query = em.createQuery(jpql, String.class);
        query.setParameter(USERNAME, username);
        return query.getSingleResult().compareTo(username) == 0;
    }

    public Author selectIdAuthorByUsername(String username) {
        String jpql = "SELECT a FROM Author a WHERE a.username = :username";
        TypedQuery<Author> query = em.createQuery(jpql, Author.class);
        query.setParameter(USERNAME, username);
        return query.getSingleResult();
     }

    public void insertAuthor(String username, String password) {
        Author a = new Author();
        a.setUsername(username);
        a.setPassword(password);
        em.persist(a);
    }

    public String getPasswordByUserName(String username) {
        String jpql = "SELECT a.password FROM Author a WHERE a.username = :username";
        TypedQuery<String> query = em.createQuery(jpql, String.class);
        query.setParameter(USERNAME, username);
        return query.getSingleResult();
    }

}
