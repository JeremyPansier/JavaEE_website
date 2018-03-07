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
 * The picture model.</br>
 * This class is an entity managed by JPA (Java Persistence API).</br>
 *
 * @author Jérémy Pansier
 */
@Entity
public class Picture {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** The author of this picture. */
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
	 * The link to open the picture edition web page.
	 * 
	 * @see #updatePictureLink()
	 */
	@Transient
	private String pictureLink;

	/**
	 * Default constructor needed by the Java Persistence API.
	 */
	@SuppressWarnings("unused")
	private Picture() {
		this.author = null;
	}

	/**
	 * Instantiates a new picture.
	 * 
	 * @param author the author
	 */
	public Picture(final Author author) {
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
	 * Gets the author of this picture.
	 *
	 * @return the author of this picture
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
	 * Gets the link to open the picture edition web page.
	 *
	 * @return the picture link
	 * @see #updatePictureLink()
	 */
	public String getPictureLink() {
		return pictureLink;
	}

	/**
	 * Updates the {@link #pictureLink}.</br>
	 * This method is called after the id has been generated during the persistence of the event.
	 */
	@PostLoad
	private void updatePictureLink() {
		this.pictureLink = WebPages.PICTURE_EDITION.createJsfUrl("pictureId", this.id);
	}
}
