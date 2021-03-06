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
 * The picture publication web page view.</br>
 * This {@link Named} class is bound with the same named xhtml file.
 * 
 * @author Jérémy Pansier
 */
@Named
@ViewScoped
public class PicturePublication implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = 8837046975013227692L;

	/** The web page. */
	public static final WebPages WEB_PAGE = WebPages.PICTURE_PUBLICATION;

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

	/**
	 * Initializes the the author just after the construction.
	 */
	@PostConstruct
	public void init() {
		author = authorService.findAuthorByAuthorName(SessionManager.getSessionUserNameOrRedirect());
		picture = new Picture(author);
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
	 * Adds the picture.
	 */
	public void addPicture() {
		if (picture.getFilename() == null) {
			Redirector.redirect(WebPages.PICTURE_PUBLICATION.createJsfUrl(), false, "Vous devez choisir une image");
		}
		pictureService.persistPicture(picture);
		Redirector.redirect(WebPages.PICTURE_REPORT.createJsfUrl("pictureId", picture.getId()), false, "Photo publiée avec succès");
	}
}
