package com.website.jsf;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.website.entities.Author;
import com.website.entities.Picture;
import com.website.enumeration.Webpagename;
import com.website.services.EventService;
import com.website.services.PictureService;
import com.website.tools.EventServiceException;
import com.website.tools.HttpErrorHandler;
import com.website.tools.PathBuilder;
import com.website.tools.SessionChecker;

@Named
@RequestScoped
public class PicturesGallery {
    @Inject
    private PictureService pictureService;
    @Inject
    private EventService eventService;

    private String username;
    private List<Picture> pictures;
    private Author author;

    @PostConstruct
    public void init() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        username = SessionChecker.getUsername(ec);
        
        try {
            pictures = pictureService.selectPicturesByAuthorUsername(username);
            author = eventService.selectAuthorByUsername(username);
        } catch (EventServiceException e) {
            HttpErrorHandler.print500(ec, e);
            return;
        }
    }
    
    public List<Picture> getPictures() {
        return pictures;
    }

    public Author getAuthor() {
        return author;
    }

    public int getSize() {
        return pictures.size();
    }

    public void addNewPicture() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(PathBuilder.getJsfPath(Webpagename.picturePublication.toString()));
    }

    public void removePicture(Picture picture) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            pictureService.deletePicture(picture.getId());
        } catch (EventServiceException eventServiceException) {
            HttpErrorHandler.print500(ec, eventServiceException);
            return;
        }
        ec.redirect(PathBuilder.getJsfPath(Webpagename.picturesGallery.toString()));
    }
}
