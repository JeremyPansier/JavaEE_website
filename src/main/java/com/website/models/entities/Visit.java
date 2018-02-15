package com.website.models.entities;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * The visit model.</br>
 * This class is an entity managed by JPA (Java Persistence API).</br>
 *
 * @author Jérémy Pansier
 */
@Entity
public class Visit implements Comparable<Visit>
{
	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** The URL. */
	private String url;

	/** The IP. */
	private String ip;

	/** The date. */
	@Column(insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	/** The count. */
	@Transient
	private int count;

	/** The resource name. */
	@Transient
	private String resourceName;

	/**
	 * Gets the URL.
	 *
	 * @return the URL
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * Sets the URL.
	 *
	 * @param url the new URL
	 */
	public void setUrl(final String url)
	{
		this.url = url;
	}

	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public int getCount()
	{
		return count;
	}

	/**
	 * Sets the count.
	 *
	 * @param count the new count
	 */
	public void setCount(final int count)
	{
		this.count = count;
	}

	/**
	 * Gets the IP.
	 *
	 * @return the IP
	 */
	public String getIp()
	{
		return ip;
	}

	/**
	 * Sets the IP.
	 *
	 * @param ip the new IP
	 */
	public void setIp(final String ip)
	{
		this.ip = ip;
	}

	/**
	 * Gets the resource name.
	 *
	 * @return the resource name
	 */
	public String getResourceName()
	{
		return resourceName;
	}

	/**
	 * Sets the resource name.
	 *
	 * @param resourceName the new resource name
	 */
	public void setResourceName(final String resourceName)
	{
		this.resourceName = resourceName;
	}

	/**
	 * Format resource name.
	 */
	@PostLoad
	private void formatResourceName()
	{
		// compilation de la regex
		final Pattern p = Pattern.compile("faces/([/a-zA-Z_0-9.-])+");
		// création d'un moteur de recherche
		final Matcher m = p.matcher(url);
		// lancement de la recherche de toutes les occurrences successives
		while (m.find())
		{
			// sous-chaine capturee
			this.resourceName = m.group().substring(6);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object) */
	@Override
	public int compareTo(final Visit stats) // on redéfinit compareTo(Object)
	{
		final Visit s = stats;
		int b;
		if (count == s.count)
		{
			return resourceName.compareTo(s.resourceName);
		}
		if (count >= s.count)
		{
			b = -1;
		}
		else
		{
			b = 1;
		}
		return b;
	}

}
