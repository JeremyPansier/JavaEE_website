package com.aiconoa.trainings.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="Id")
	private int idUser;
	private String email;
	
	public User() {
        super();
    }

    public User(int idUser, String email) {
		super();
		this.idUser = idUser;
		this.email = email;
	}
	
	public User(int idUser) {
        this.idUser = idUser;
    }

    public int getIdUser() {
		return idUser;
	}
	
	public String getEmail() {
		return email;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
}
