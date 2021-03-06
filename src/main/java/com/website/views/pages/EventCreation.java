package com.website.views.pages;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;

import com.website.managers.file.Uploader;
import com.website.models.entities.Author;
import com.website.models.entities.Event;
import com.website.persistence.AuthorService;
import com.website.persistence.EventService;
import com.website.tools.navigation.Redirector;
import com.website.tools.navigation.SessionManager;
import com.website.views.WebPages;

/**
 * The event creation web page view.</br>
 * This {@link Named} class is bound with the same named xhtml file.
 * 
 * @author Jérémy Pansier
 */
@Named
@ViewScoped
public class EventCreation implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = 6144143620477608461L;

	/** The web page. */
	public static final WebPages WEB_PAGE = WebPages.EVENT_CREATION;

	/** The service managing the event persistence. */
	@Inject
	private EventService eventService;

	/** The service managing the author persistence. */
	@Inject
	private AuthorService authorService;

	/** The event. */
	private transient Event event;

	/** The author. */
	private transient Author author;

	/**
	 * Initializes the event and the author just after the construction.
	 */
	@PostConstruct
	public void init() {
		final String sessionUserName = SessionManager.getSessionUserNameOrRedirect();
		author = authorService.findAuthorByAuthorName(sessionUserName);
		event = new Event(author);
	}

	/**
	 * @return the web page
	 */
	public WebPages getWebPage() {
		return WEB_PAGE;
	}

	/**
	 * Gets the event.
	 *
	 * @return the event
	 */
	public Event getEvent() {
		return event;
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
		final String filename = Uploader.uploadFile(fileUploadEvent);
		event.setFilename(filename);
	}

	/**
	 * Creates the event.
	 */
	public void createEvent() {
		String message = "avec succès";
		if (null == event.getFilename()) {
			message = "sans image";
		}
		eventService.persistEvent(event);
		Redirector.redirect(WebPages.EVENT_MANAGEMENT.createJsfUrl("eventId", event.getId()), false, "Évènement créé " + message);
	}
}
