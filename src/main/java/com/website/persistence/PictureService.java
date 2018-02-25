package com.website.persistence;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.website.models.entities.Author;
import com.website.models.entities.Picture;
import com.website.tools.navigation.HttpErrorHandler;

/**
 * The service managing the picture persistence.</br>
 *
 * @author Jérémy Pansier
 */
@Stateless
public class PictureService
{

	/** The entity manager binding the model to the persistence entity. */
	@PersistenceContext(unitName = "website")
	private EntityManager entityManager;

	/** The service managing the author persistence. */
	@Inject
	private AuthorService authorService;

	/** The user name persistence data category name. */
	private static final String NAME = PersistenceDataCategories.NAME.getName();

	/** The picture id persistence data category name. */
	private static final String PICTUREID = PersistenceDataCategories.PICTUREID.getName();

	/** The id persistence data category name. */
	private static final String ID = PersistenceDataCategories.ID.getName();

	public List<Picture> selectPicturesByAuthorName(final String authorName)
	{
		final String jpql = "SELECT p FROM Picture p JOIN p.author a WHERE a.name =:name";
		final TypedQuery<Picture> query = entityManager.createQuery(jpql, Picture.class);
		query.setParameter(NAME, authorName);
		return query.getResultList();
	}

	public void deletePicture(final Long id)
	{
		final Picture picture = entityManager.find(Picture.class, id);
		entityManager.remove(picture);
	}

	public Long insertPicture(final Long id, final String title, final String description, final String filename)
	{
		final Picture p = new Picture();
		p.setTitle(title);
		p.setDescription(description);
		if (filename != null)
		{
			p.setFilename(filename);
		}
		p.getAuthor().setId(id);
		entityManager.persist(p);
		return p.getId();
	}

	public boolean isPicturesAuthor(final Long id, final String authorName)
	{
		if (!countPicturesById(id))
		{
			HttpErrorHandler.print404("There is no event for this id in the database");
			return false;
		}
		final Author author = authorService.selectAuthorByAuthorName(authorName);
		if (author.getId() != selectAuthorIdByPictureId(id))
		{
			HttpErrorHandler.print401("The author cannot access to this event");
			return false;
		}
		return true;
	}

	private Long selectAuthorIdByPictureId(final Long pictureId)
	{
		final String jpql = "SELECT p.author.id FROM Picture p WHERE p.id = :pictureId";
		final TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
		query.setParameter(PICTUREID, pictureId);
		return query.getSingleResult();
	}

	private boolean countPicturesById(final Long id)
	{
		final String jpql = "SELECT COUNT(p) FROM Picture p WHERE p.id=:id";
		final TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
		query.setParameter(ID, id);
		return query.getSingleResult() == 1L;
	}

	public Picture selectPictureByPictureId(final Long id)
	{
		return entityManager.find(Picture.class, id);
	}

	public void updatePicture(final Long id, final String title, final String description, final String filename)
	{
		// TODO fill this method
	}
}
