package com.website.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

import com.website.enumeration.Webpagename;
import com.website.tools.PathBuilder;

@Entity
public class Picture
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String description;
	private String filename;

	@Transient
	private String pictureLink;

	@ManyToOne
	@JoinColumn(name = "authorId")
	private Author author;

	public Picture()
	{
		super();
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public Long getId()
	{
		return id;
	}

	public Author getAuthor()
	{
		return author;
	}

	public void setAuthorId(Long authorId)
	{
		this.author = new Author(authorId);
	}

	public String getPictureLink()
	{
		return pictureLink;
	}

	@PostLoad
	private void setPictureLink()
	{
		this.pictureLink = PathBuilder.getJsfPath(Webpagename.pictureEdition.toString(), "pictureId", this.id);
	}
}
