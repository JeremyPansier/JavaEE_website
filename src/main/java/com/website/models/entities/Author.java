package com.website.models.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * The author model.</br>
 * This class is an entity managed by JPA (Java Persistence API).</br>
 *
 * @author Jérémy Pansier
 */
@Entity
public class Author
{
	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	/** The name. */
	String name;

	/** The password. */
	String password;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(final Long id)
	{
		this.id = id;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getname()
	{
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setname(final String name)
	{
		this.name = name;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(final String password)
	{
		this.password = password;
	}
}
