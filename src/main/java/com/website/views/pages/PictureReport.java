package com.website.views.pages;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.website.models.entities.Author;
import com.website.models.entities.Picture;
import com.website.persistence.AuthorService;
import com.website.persistence.PictureService;
import com.website.persistence.VisitService;
import com.website.tools.error.HttpErrorHandler;
import com.website.tools.navigation.ContextManager;
import com.website.tools.navigation.SessionManager;
import com.website.views.WebPages;

/**
 * The picture report web page view.</br>
 * This {@link Named} class is bound with the same named xhtml file.
 * 
 * @author Jérémy Pansier
 */
@Named
@ViewScoped
public class PictureReport implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = 4582347711226573145L;

	/** The service managing the visit persistence. */
	@Inject
	private VisitService visitService;

	/** The service managing the picture persistence. */
	@Inject
	private PictureService pictureService;

	/** The service managing the author persistence. */
	@Inject
	private AuthorService authorService;

	/** The web page. */
	public static final WebPages WEB_PAGE = WebPages.PICTURE_REPORT;

	/** The id. */
	private Long id;

	/** The picture. */
	private transient Picture picture;

	/** The author. */
	private transient Author author;

	/** The session user name. */
	private String sessionUserName;

	/** The views count. */
	private Long viewsCount;

	/**
	 * Initializes the session user name and the author just after the construction.
	 */
	@PostConstruct
	public void init() {
		sessionUserName = SessionManager.getSessionUserNameOrRedirect();
		author = authorService.findAuthorByAuthorName(sessionUserName);
	}

	/**
	 * Sets the picture and the views count on the web page loading, depending on the HTTP request parameter:
	 */
	public void load() {
		try {
			if (!authorService.isPictureAuthor(id, sessionUserName)) {
				return;
			}
			final String websiteURL = ContextManager.getWebsiteUrl();

			picture = pictureService.findPictureByPictureId(id);

			final String url = websiteURL + "/" + "FilesServlet" + "/" + picture.getFilename();
			viewsCount = visitService.countVisitsByUrlGroupByUrl(url);
		}
		catch (final NumberFormatException numberFormatException) {
			HttpErrorHandler.print404(
					"The http parameter cannot be formatted to Integer. Method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " Class: " + this.getClass().getName(),
					numberFormatException);
			return;
		}
		catch (final IllegalStateException illegalStateException) {
			HttpErrorHandler.print500("Forward issue", illegalStateException);
			return;
		}
	}

	/**
	 * Gets the web page.
	 *
	 * @return the web page
	 */
	public WebPages getWebPage() {
		return WEB_PAGE;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * Gets the picture.
	 *
	 * @return the picture
	 */
	public Picture getPicture() {
		return picture;
	}

	/**
	 * Gets the author.
	 *
	 * @return the author
	 */
	public Author getAuthor() {
		return author;
	}

	/**
	 * Gets the views count.
	 *
	 * @return the views count
	 */
	public Long getViewsCount() {
		return viewsCount;
	}
}
