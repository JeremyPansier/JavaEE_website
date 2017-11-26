package com.website.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.website.entities.Author;
import com.website.entities.Picture;
import com.website.enumeration.Constant;
import com.website.tools.HttpErrorHandler;

@Stateless
public class PictureService
{
	@PersistenceContext(unitName = "event")
	private EntityManager em;
	@Inject
	private EventService eventService;
	private static final String USERNAME = Constant.username.toString();
	private static final String PICTUREID = Constant.pictureId.toString();
	private static final String ID = Constant.id.toString();

	public List<Picture> selectPicturesByAuthorUsername(String username)
	{
		String jpql = "SELECT p FROM Picture p JOIN p.author a WHERE a.username =:username";
		TypedQuery<Picture> query = em.createQuery(jpql, Picture.class);
		query.setParameter(USERNAME, username);
		return query.getResultList();
	}

	public void deletePicture(Long id)
	{
		Picture picture = em.find(Picture.class, id);
		em.remove(picture);
	}

	public Long insertPicture(Long id, String title, String description, String filename)
	{
		Picture p = new Picture();
		p.setTitle(title);
		p.setDescription(description);
		if (filename != null)
		{
			p.setFilename(filename);
		}
		p.setAuthorId(id);
		em.persist(p);
		return p.getId();
	}

	public boolean isPicturesAuthor(Long id, String username, ExternalContext ec)
	{
		if (!countPicturesById(id))
		{
			HttpErrorHandler.print404(ec, "There is no event for this id in the database");
			return false;
		}
		Author author = eventService.selectAuthorByUsername(username);
		if (author.getId() != selectAuthorIdByPictureId(id))
		{
			HttpErrorHandler.print401(ec, "The author cannot access to this event");
			return false;
		}
		return true;
	}

	private Long selectAuthorIdByPictureId(Long pictureId)
	{
		String jpql = "SELECT p.author.id FROM Picture p WHERE p.id = :pictureId";
		TypedQuery<Long> query = em.createQuery(jpql, Long.class);
		query.setParameter(PICTUREID, pictureId);
		return query.getSingleResult();
	}

	private boolean countPicturesById(Long id)
	{
		String jpql = "SELECT COUNT(p) FROM Picture p WHERE p.id=:id";
		TypedQuery<Long> query = em.createQuery(jpql, Long.class);
		query.setParameter(ID, id);
		return query.getSingleResult() == 1L;
	}

	public Picture selectPictureByPictureId(Long id)
	{
		return em.find(Picture.class, id);
	}

	public void updatePicture(Long id, String title, String description, String filename)
	{
		// TODO Auto-generated method stub

	}
}
