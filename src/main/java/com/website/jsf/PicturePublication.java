package com.website.jsf;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;

import com.website.entities.Author;
import com.website.entities.Picture;
import com.website.enumeration.Webpagename;
import com.website.services.EventService;
import com.website.services.PictureService;
import com.website.tools.EventServiceException;
import com.website.tools.HttpErrorHandler;
import com.website.tools.PathBuilder;
import com.website.tools.SessionChecker;
import com.website.tools.Uploader;

@Named
@ViewScoped
public class PicturePublication implements Serializable
{
	private static final long serialVersionUID = 8837046975013227692L;
	private Long id;
	private Author author;
	private Picture picture = new Picture();
	private String username;
	@Inject
	private EventService eventService;
	@Inject
	private PictureService pictureService;

	@PostConstruct
	public void init()
	{
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try
		{
			username = SessionChecker.getUsername(ec);
			author = eventService.selectAuthorByUsername(username);
		} catch (EventServiceException eventServiceException)
		{
			HttpErrorHandler.print500(ec, eventServiceException);
			return;
		}
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Author getAuthor()
	{
		return author;
	}

	public void setAuthor(Author author)
	{
		this.author = author;
	}

	public Picture getPicture()
	{
		return picture;
	}

	public void setPicture(Picture picture)
	{
		this.picture = picture;
	}

	public void upload(FileUploadEvent fue)
	{
		picture.setFilename(Uploader.uploadFile(fue));
	}

	public void addPicture() throws IOException
	{
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		if (picture.getFilename() == null)
		{
			flash.put("flash.message", "Vous devez choisir une image");
			ec.redirect(PathBuilder.getJsfPath(Webpagename.picturePublication.toString()));
		}
		try
		{
			id = pictureService.insertPicture(author.getId(), picture.getTitle(), picture.getDescription(), picture.getFilename());
		} catch (EventServiceException eventServiceException)
		{
			HttpErrorHandler.print500(ec, eventServiceException);
			return;
		}
		flash.put("flash.message", "Photo publiée avec succès");
		ec.redirect(PathBuilder.getJsfPath(Webpagename.pictureStat.toString(), "pictureId", id));
	}
}
