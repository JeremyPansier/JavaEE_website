package com.website.jsf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import com.website.entities.Author;
import com.website.entities.Picture;
import com.website.services.EventService;
import com.website.services.PictureService;
import com.website.tools.EventServiceException;
import com.website.tools.HttpErrorHandler;
import com.website.tools.SessionChecker;

@Named
@ViewScoped
public class PictureStat implements Serializable
{
	private static final long serialVersionUID = 4582347711226573145L;
	@Inject
	private PictureService pictureService;
	@Inject
	private EventService eventService;

	private String username;
	private Author author;
	private Long id;
	private Picture picture;
	private Long viewCount;

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
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

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getViewCount()
	{
		return viewCount;
	}

	public void setViewCount(Long views)
	{
		this.viewCount = views;
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

	public void load()
	{
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try
		{
			if (!pictureService.isPicturesAuthor(id, username, ec))
			{
				return;
			}
			HttpServletRequest origRequest = (HttpServletRequest) ec.getRequest();
			String originURL = origRequest.getRequestURL().toString();
			String websiteURL = originURL.substring(0, originURL.lastIndexOf('/'));

			picture = pictureService.selectPictureByPictureId(id);

			String url = websiteURL + "/" + "FilesServlet" + "/" + picture.getFilename();
			picture.getFilename();
			viewCount = eventService.countVisitsByUrlGroupByUrl(url);

		} catch (EventServiceException e)
		{
			HttpErrorHandler.print500(ec, e);
			return;
		} catch (NumberFormatException e)
		{
			HttpErrorHandler.print404(ec, e, "The http parameter cannot be formatted to Integer. Method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " Class: " + this.getClass().getName());
			return;
		} catch (IllegalStateException exception)
		{
			HttpErrorHandler.print500(ec, exception, "Forward issue");
			return;
		}
	}
}
