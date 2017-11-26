package com.website.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

import com.website.enumeration.Webpagename;
import com.website.tools.PathBuilder;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String filename;
    
    @Transient
    private String eventLink;

    @ManyToOne
    @JoinColumn(name = "authorId")
    private Author author;

    public Event() {
        super();
    }

    public Event(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public Long getId() {
        return id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthorId(Long authorId) {
        this.author = new Author(authorId);
    }
    
    public String getEventLink() {
        return eventLink;
    }
    
    @PostLoad
    private void setEventLink() {
        this.eventLink = PathBuilder.getJsfPath(Webpagename.eventManagement.toString(), "eventId", this.id);
    }
}
