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
public class PictureEdition implements Serializable
{
	private static final long serialVersionUID = -9021006042600302770L;
	private Long id;
	private Author author;
	private Picture picture;
	private String username;
	@Inject
	private EventService eventService;
	@Inject
	private PictureService pictureService;

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

	public void load() throws IOException
	{
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try
		{
			if (id != null)
			{
				if (!pictureService.isPicturesAuthor(id, username, ec))
				{
					return;
				}
				picture = pictureService.selectPictureByPictureId(id);
			}
		} catch (NullPointerException e)
		{
			ec.redirect(PathBuilder.getJsfPath(Webpagename.picturesGallery.toString()));
			return;
		} catch (EventServiceException eventServiceException)
		{
			HttpErrorHandler.print500(ec, eventServiceException);
			return;
		}
	}

	public void upload(FileUploadEvent fue)
	{
		picture.setFilename(Uploader.uploadFile(fue));
	}

	public void editPicture() throws IOException
	{
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		if (!pictureService.isPicturesAuthor(id, username, ec))
		{
			return;
		}
		pictureService.updatePicture(id, picture.getTitle(), picture.getDescription(), picture.getFilename());
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		flash.put("flash.message", "Évènement édité avec succès");
		ec.redirect(PathBuilder.getJsfPath(Webpagename.pictureStat.toString(), "pictureId", id));
	}
}
