package com.aiconoa.trainings.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int idEvent;
    private String title;
    private String description;
    private static final String LINKTOEVENTLINKSERVLET = "eventLink.xhtml?id=";

    @Transient
    private String linkToEventLinkServlet;


    private String fileName;

    @ManyToOne
    @JoinColumn(name = "idAuthor")
    private Author author;

    public Event() {
        super();
    }

    public Event(String title, String description, int idEvent) {
        super();
        this.title = title;
        this.description = description;
        this.linkToEventLinkServlet = LINKTOEVENTLINKSERVLET + idEvent;
        this.setIdEvent(idEvent);
    }

    public Event(String title, String description, int idEvent, String fileName) {
        super();
        this.title = title;
        this.description = description;
        this.idEvent = idEvent;
        this.fileName = fileName;
        this.linkToEventLinkServlet = LINKTOEVENTLINKSERVLET + idEvent;
    }

    public Event(int idEvent) {
        this.idEvent = idEvent;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLinkToEventLinkServlet() {
        return linkToEventLinkServlet;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public Author getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setIdAuthor(int idAuthor) {
        this.author = new Author(idAuthor);
    }

    @PostLoad
    private void setlinkToEventLinkServlet() {
        this.linkToEventLinkServlet = LINKTOEVENTLINKSERVLET + this.idEvent;
    }
}
