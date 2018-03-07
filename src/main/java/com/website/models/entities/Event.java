package com.website.models.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

import com.website.views.WebPages;

/**
 * The event model.</br>
 * This class is an entity managed by JPA (Java Persistence API).</br>
 *
 * @author Jérémy Pansier
 */
@Entity
public class Event {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** The author of this event. */
	@ManyToOne
	@JoinColumn(name = "authorId")
	private final Author author;

	/** The title. */
	private String title;

	/** The description. */
	private String description;

	/** The filename. */
	private String filename;

	/**
	 * The link to open the event management web page.
	 * 
	 * @see #updateEventLink()
	 */
	@Transient
	private String eventLink;

	/**
	 * Default constructor needed by the Java Persistence API.
	 */
	@SuppressWarnings("unused")
	private Event() {
		this.author = null;
	}

	/**
	 * Instantiates a new event.
	 * 
	 * @param author the author
	 */
	public Event(final Author author) {
		this.author = author;
		this.filename = null;
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
	 * Gets the author of this event.
	 *
	 * @return the author of this event
	 */
	public Author getAuthor() {
		return author;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Gets the filename.
	 *
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Sets the filename.
	 *
	 * @param filename the new filename
	 */
	public void setFilename(final String filename) {
		this.filename = filename;
	}

	/**
	 * Gets the link to open the event management web page.
	 *
	 * @return the link to open the event web page
	 * @see #updateEventLink()
	 */
	public String getEventLink() {
		return eventLink;
	}

	/**
	 * Updates the {@link #eventLink}.</br>
	 * This method is called after the id has been generated during the persistence of the event.
	 */
	@PostLoad
	private void updateEventLink() {
		this.eventLink = WebPages.EVENT_MANAGEMENT.createJsfUrl("eventId", this.id);
	}
}
