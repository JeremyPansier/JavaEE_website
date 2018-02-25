package com.website.views.pages;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;

import com.website.models.entities.Author;
import com.website.models.entities.Picture;
import com.website.persistence.AuthorService;
import com.website.persistence.PictureService;
import com.website.tools.EventServiceException;
import com.website.tools.file.Uploader;
import com.website.tools.navigation.HttpErrorHandler;
import com.website.tools.navigation.Redirector;
import com.website.tools.navigation.SessionManager;
import com.website.views.WebPages;

/**
 * The picture edition web page view.</br>
 * This {@link Named} class is bound with the same named xhtml file.
 * 
 * @author Jérémy Pansier
 */
@Named
@ViewScoped
public class PictureEdition implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = -9021006042600302770L;

	/** The web page. */
	public static final WebPages WEB_PAGE = WebPages.PICTURE_EDITION;

	/** The service managing the picture persistence. */
	@Inject
	private PictureService pictureService;

	/** The service managing the author persistence. */
	@Inject
	private AuthorService authorService;

	/** The id. */
	private Long id;

	/** The picture. */
	private transient Picture picture;

	/** The author. */
	private transient Author author;

	/** The session user name. */
	private String sessionUserName;

	/**
	 * Initializes the session user name and the author just after the construction.
	 */
	@PostConstruct
	public void init() {
		try {
			sessionUserName = SessionManager.checkSessionUserName();
			author = authorService.selectAuthorByAuthorName(sessionUserName);
		}
		catch (final EventServiceException eventServiceException) {
			HttpErrorHandler.print500(eventServiceException);
			return;
		}
	}

	/**
	 * Sets the picture on the web page loading, depending on the HTTP request parameter:
	 */
	public void load() {
		try {
			if (id != null) {
				if (!pictureService.isPicturesAuthor(id, sessionUserName)) {
					return;
				}
				picture = pictureService.selectPictureByPictureId(id);
			}
		}
		catch (final NullPointerException e) {
			Redirector.redirect(WebPages.PICTURES_GALLERY.createJsfUrl());
			return;
		}
		catch (final EventServiceException eventServiceException) {
			HttpErrorHandler.print500(eventServiceException);
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
	 * Uploads the file corresponding to the specified file upload event.
	 *
	 * @param fileUploadEvent the file upload event
	 */
	public void upload(final FileUploadEvent fileUploadEvent) {
		picture.setFilename(Uploader.uploadFile(fileUploadEvent));
	}

	/**
	 * Edits the picture.
	 */
	public void editPicture() {
		if (!pictureService.isPicturesAuthor(id, sessionUserName)) {
			return;
		}
		pictureService.updatePicture(id, picture.getTitle(), picture.getDescription(), picture.getFilename());
		Redirector.redirect(WebPages.PICTURE_REPORT.createJsfUrl("pictureId", id), false, "Évènement édité avec succès");
	}
}
