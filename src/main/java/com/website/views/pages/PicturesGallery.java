package com.website.views.pages;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.website.models.entities.Author;
import com.website.models.entities.Picture;
import com.website.persistence.AuthorService;
import com.website.persistence.PictureService;
import com.website.tools.EventServiceException;
import com.website.tools.navigation.HttpErrorHandler;
import com.website.tools.navigation.Redirector;
import com.website.tools.navigation.SessionManager;
import com.website.views.WebPages;

/**
 * The picture gallery web page view.</br>
 * This {@link Named} class is bound with the same named xhtml file.
 * 
 * @author Jérémy Pansier
 */
@Named
@RequestScoped
public class PicturesGallery
{
	/** The service managing the picture persistence. */
	@Inject
	private PictureService pictureService;

	/** The service managing the author persistence. */
	@Inject
	private AuthorService authorService;

	/** The web page. */
	public static final WebPages WEB_PAGE = WebPages.PICTURES_GALLERY;

	/** The pictures. */
	private List<Picture> pictures;

	/** The author. */
	private Author author;

	/**
	 * Initializes the picture and the author just after the construction.
	 */
	@PostConstruct
	public void init()
	{
		final String username = SessionManager.checkSessionUserName();

		try
		{
			pictures = pictureService.selectPicturesByAuthorName(username);
			author = authorService.selectAuthorByAuthorName(username);
		}
		catch (final EventServiceException e)
		{
			HttpErrorHandler.print500(e);
			return;
		}
	}

	/**
	 * Gets the web page.
	 *
	 * @return the web page
	 */
	public WebPages getWebPage()
	{
		return WEB_PAGE;
	}

	/**
	 * Gets the pictures.
	 *
	 * @return the pictures
	 */
	public List<Picture> getPictures()
	{
		return pictures;
	}

	/**
	 * Gets the author.
	 *
	 * @return the author
	 */
	public Author getAuthor()
	{
		return author;
	}

	/**
	 * Redirects to the picture publication web page.
	 */
	public void addNewPicture()
	{
		Redirector.redirect(WebPages.PICTURE_PUBLICATION.createJsfUrl());
	}

	/**
	 * Removes the specified picture.
	 *
	 * @param picture the picture to remove
	 */
	public void removePicture(final Picture picture)
	{
		try
		{
			pictureService.deletePicture(picture.getId());
		}
		catch (final EventServiceException eventServiceException)
		{
			HttpErrorHandler.print500(eventServiceException);
			return;
		}
		Redirector.redirect(WebPages.PICTURES_GALLERY.createJsfUrl());
	}
}
