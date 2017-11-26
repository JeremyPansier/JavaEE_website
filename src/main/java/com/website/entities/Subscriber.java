package com.website.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String hash;
    private int status;
    private int emailstatus;
    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date emaildate;
    
    @Transient
    private String email;
    @Transient
    private String isPresentString;
    @Transient
    private String sendingDateString;
    @Transient
    private String iconPath;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "eventId")
    private Event event;

    public Subscriber() {
        super();
    }

    public Subscriber(Long idEvent, Long idUser) {
        super();
        this.event = new Event(idEvent);
        this.user = new User(idUser);
    }

    public Subscriber(Long idEvent, Long idUser, int status) {
        this(idEvent, idUser);
        this.status = status;

        checkIsPresent();
    }

    public Subscriber(String email, int status, Date emaildate) {
        super();
        this.email = email;
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        this.sendingDateString = formater.format(emaildate);
        this.status = status;

        checkIsPresent();
    }

    public String getHash() {
        return hash;
    }
    
    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getIconPath() {
        return iconPath;
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getSendingDateString() {
        return sendingDateString;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIsPresentString() {
        return isPresentString;
    }

    public Long getIdEvent() {
        return event.getId();
    }

    public void setEventId(Long idEvent) {
        this.event = new Event(idEvent);
    }

    public void setUserId(Long idUser) {
        this.user = new User(idUser);
    }

    public Long getIdUser() {
        return user.getId();
    }

    public int getEmailstatus() {
        return emailstatus;
    }

    public void setEmailstatus(int mailstatus) {
        this.emailstatus = mailstatus;
    }

    public Date getEmaildate() {
        return emaildate;
    }

    public Long getId() {
        return id;
    }

    @PostLoad
    private void postLoad() {
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        sendingDateString = formater.format(emaildate);
        checkIsPresent();
    }

    private void checkIsPresent() {
        switch (status) {
        case 1: // ACCEPT
            this.isPresentString = "Présent";
            this.iconPath = "images/accept.png";
            break;
        case 2: // DECLINE
            this.isPresentString = "Absent";
            this.iconPath = "images/decline.png";
            break;
        default:
            this.isPresentString = "Sans réponse";
            this.iconPath = "images/pending.png";
            return;
        }
    }
}
