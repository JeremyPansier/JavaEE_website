package com.website.models.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The profile model.</br>
 * This class is an entity managed by JPA (Java Persistence API).</br>
 *
 * @author Jérémy Pansier
 */
@Entity
public class Profile
{
	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/** The title. */
	private String title;

	/** The description. */
	private String description;

	/** The filename. */
	private String filename;

	/** The author. */
	@ManyToOne
	@JoinColumn(name = "idAuthor")
	private Author author;

	/**
	 * This default constructor is needed by the Java Persistence API.
	 */
	@SuppressWarnings("unused")
	private Profile()
	{
	}

	/**
	 * Instantiates a new profile.</br>
	 *
	 * @param title the title
	 * @param description the description
	 * @param id the id
	 */
	public Profile(final String title, final String description, final int id)
	{
		this.title = title;
		this.description = description;
		this.setId(id);
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(final int id)
	{
		this.id = id;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(final String title)
	{
		this.title = title;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(final String description)
	{
		this.description = description;
	}

	/**
	 * Gets the filename.
	 *
	 * @return the filename
	 */
	public String getFilename()
	{
		return filename;
	}

	/**
	 * Sets the filename.
	 *
	 * @param filename the new filename
	 */
	public void setFilename(final String filename)
	{
		this.filename = filename;
	}
}
