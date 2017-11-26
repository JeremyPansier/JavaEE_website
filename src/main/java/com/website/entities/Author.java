package com.website.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id;
    String username;
    String password;
    
    public Author() {
        super();
    }
    public Author(Long id, String username, String password) {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
    }
    
    public Author(Long id) {
        this.id = id;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
