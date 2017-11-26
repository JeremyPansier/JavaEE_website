package com.website.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String email;
	
	public User() {
        super();
    }

    public User(Long id, String email) {
		super();
		this.id = id;
		this.email = email;
	}
	
	public User(Long id) {
        this.id = id;
    }

    public Long getId() {
		return id;
	}
	
    public void setId(Long id) {
        this.id = id;
    }
    
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
