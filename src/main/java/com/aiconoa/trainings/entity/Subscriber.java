package com.aiconoa.trainings.entity;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "subscribers")
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String hashCode;
    private int mailStatus;
    @Transient
    private String email;
    private int isPresent;
    @Transient
    private String isPresentString;
    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendingDate;
    @Transient
    private String sendingDateString;
    @Transient
    private String iconeLink;
    @Transient
    private static final int ACCEPT = 1;
    @Transient
    private static final int DECLINE = 2;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;

    @ManyToOne
    @JoinColumn(name = "idEvent")
    private Event event;

    public Subscriber() {
        super();
    }

    public Subscriber(int idEvent, int idUser) {
        super();
        this.event = new Event(idEvent);
        this.user = new User(idUser);
    }

    public Subscriber(int idEvent, int idUser, int isPresent) {
        this(idEvent, idUser);
        this.isPresent = isPresent;

        checkIsPresent();
    }

    public Subscriber(String email, int isPresent, Date sendingDate) {
        super();
        this.email = email;
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        this.sendingDateString = formater.format(sendingDate);
        this.isPresent = isPresent;

        checkIsPresent();
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getIconeLink() {
        return iconeLink;
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getSendingDateString() {
        return sendingDateString;
    }

    public int getIsPresent() {
        return isPresent;
    }

    public void setIsPresent(int isPresent) {
        this.isPresent = isPresent;
    }

    public String getIsPresentString() {
        return isPresentString;
    }

    public int getIdEvent() {
        return event.getIdEvent();
    }

    public void setIdEvent(int idEvent) {
        this.event = new Event(idEvent);
    }

    public void setIdUser(int idUser) {
        this.user = new User(idUser);
    }

    public int getIdUser() {
        return user.getIdUser();
    }

    public String getHashCode() {
        return hashCode;
    }

    public int getMailStatus() {
        return mailStatus;
    }

    public void setMailStatus(int mailStatus) {
        this.mailStatus = mailStatus;
    }

    public Date getSendingDate() {
        return sendingDate;
    }

    public int getId() {
        return id;
    }

    @PostLoad
    private void postLoad() {
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        sendingDateString = formater.format(sendingDate);
        checkIsPresent();
    }

    private void checkIsPresent() {
        switch (isPresent) {
        case ACCEPT:
            this.isPresentString = "Présent";
            this.iconeLink = "images/accept.png";
            break;
        case DECLINE:
            this.isPresentString = "Absent";
            this.iconeLink = "images/decline.png";
            break;
        default:
            this.isPresentString = "Sans réponse";
            this.iconeLink = "images/pending.png";
            return;
        }
    }
}
