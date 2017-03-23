package com.aiconoa.trainings.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="Id")
    int idAuthor;
    String username;
    String password;
    
    public Author() {
        super();
    }
    public Author(int idAuthor, String username, String password) {
        super();
        this.idAuthor = idAuthor;
        this.username = username;
        this.password = password;
    }
    
        public Author(int idAuthor) {
        this.idAuthor = idAuthor;
    }
        public int getIdAuthor() {
        return idAuthor;
    }
    public void setIdAuthor(int idAuthor) {
        this.idAuthor = idAuthor;
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
