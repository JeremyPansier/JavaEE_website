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
 * The event edition web page view.</br>
 * This {@link Named} class is bound with the same named xhtml file.
 * 
 * @author Jérémy Pansier
 */
@Named
@ViewScoped
public class EventEdition implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = -7682934518094297522L;

	/** The web page. */
	public static final WebPages WEB_PAGE = WebPages.EVENT_EDITION;

	/** The service managing the event persistence. */
	@Inject
	private EventService eventService;

	/** The service managing the author persistence. */
	@Inject
	private AuthorService authorService;

	/** The event id HTTP request parameter. */
	private Long id;

	/** The event. */
	private transient Event event;

	/** The author. */
	private transient Author author;

	/** The session user name. */
	private String sessionUserName;

	/**
	 * Initializes the session user name and the event just after the construction.
	 */
	@PostConstruct
	public void init() {
		sessionUserName = SessionManager.getSessionUserNameOrRedirect();
		author = authorService.findAuthorByAuthorName(sessionUserName);
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
	 * Gets the event id HTTP request parameter.
	 *
	 * @return the event id HTTP request parameter
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the event id HTTP request parameter.
	 *
	 * @param id the event id HTTP request parameter
	 */
	public void setId(final Long id) {
		this.id = id;
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
	 * Gets the event.
	 *
	 * @return the event
	 */
	public Event getEvent() {
		return event;
	}

	/**
	 * Sets the event on the web page loading, depending on the HTTP request parameter.</br>
	 */
	public void load() {
		try {
			if (id != null) {
				if (!eventService.isEventsAuthor(id, sessionUserName)) {
					return;
				}
				event = eventService.findEventByEventId(id);
			}
		}
		catch (final NullPointerException e) {
			Redirector.redirect(WebPages.EVENTS_LIST.createJsfUrl());
			return;
		}
	}

	/**
	 * Uploads the file corresponding to the specified file upload event.</br>
	 * Updates the event filename only if it is not null in such a way to keep the old file if the file upload fails.
	 *
	 * @param fileUploadEvent the file upload event
	 */
	public void upload(final FileUploadEvent fileUploadEvent) {
		final String uploadedFilename = Uploader.uploadFile(fileUploadEvent);
		if (null != uploadedFilename) {
			event.setFilename(uploadedFilename);
		}
	}

	/**
	 * Edits the event.
	 */
	public void editEvent() {
		if (!eventService.isEventsAuthor(id, sessionUserName)) {
			return;
		}
		eventService.updateEvent(event);
		Redirector.redirect(WebPages.EVENT_MANAGEMENT.createJsfUrl("eventId", id), false, "Évènement édité avec succès");
	}
}
