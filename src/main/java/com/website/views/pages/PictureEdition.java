package com.website.views.pages;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;

import com.website.managers.file.Uploader;
import com.website.models.entities.Author;
import com.website.models.entities.Picture;
import com.website.persistence.AuthorService;
import com.website.persistence.PictureService;
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
		sessionUserName = SessionManager.getSessionUserNameOrRedirect();
		author = authorService.findAuthorByAuthorName(sessionUserName);
	}

	/**
	 * Sets the picture on the web page loading, depending on the HTTP request parameter:
	 */
	public void load() {
		try {
			if (id != null) {
				if (!authorService.isPictureAuthor(id, sessionUserName)) {
					return;
				}
				picture = pictureService.findPictureByPictureId(id);
			}
		}
		catch (final NullPointerException nullPointerException) {
			Redirector.redirect(WebPages.PICTURES_GALLERY.createJsfUrl());
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
	 * Uploads the file corresponding to the specified file upload event.</br>
	 * Updates the picture filename only if it is not null in such a way to keep the old file if the file upload fails.
	 *
	 * @param fileUploadEvent the file upload event
	 */
	public void upload(final FileUploadEvent fileUploadEvent) {
		final String uploadedFilename = Uploader.uploadFile(fileUploadEvent);
		if (null != uploadedFilename) {
			picture.setFilename(uploadedFilename);
		}
	}

	/**
	 * Edits the picture.
	 */
	public void editPicture() {
		if (!authorService.isPictureAuthor(id, sessionUserName)) {
			return;
		}
		pictureService.updatePicture(picture);
		Redirector.redirect(WebPages.PICTURE_REPORT.createJsfUrl("pictureId", id), false, "Évènement édité avec succès");
	}
}
