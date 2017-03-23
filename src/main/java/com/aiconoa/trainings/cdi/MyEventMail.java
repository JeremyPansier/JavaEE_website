package com.aiconoa.trainings.cdi;

public class MyEventMail {
    private String email;
    private String title;
    private String hash;

    public MyEventMail() {
        super();
    }

    public MyEventMail(String email, String title, String hash) {
        super();
        this.email = email;
        this.title = title;
        this.hash = hash;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getEmail() {
        return email;
    }

    public String getTitle() {
        return title;
    }

    public String getHash() {
        return hash;
    }
}
