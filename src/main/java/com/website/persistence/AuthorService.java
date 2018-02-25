package com.website.persistence;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.website.models.entities.Author;
import com.website.tools.navigation.HttpErrorHandler;

/**
 * The service managing the author persistence.</br>
 *
 * @author Jérémy Pansier
 */
@Stateless
public class AuthorService
{
	/** The entity manager binding the model to the persistence entity. */
	@PersistenceContext(unitName = "website")
	private EntityManager entityManager;

	/** The service managing the event persistence. */
	@Inject
	EventService eventService;

	/** The event id persistence data category name. */
	private static final String EVENTID = PersistenceDataCategories.EVENTID.getName();

	/** The user name persistence data category name. */
	private static final String NAME = PersistenceDataCategories.NAME.getName();

	public void insertAuthor(final String name, final String password)
	{
		final Author a = new Author();
		a.setname(name);
		a.setPassword(password);
		entityManager.persist(a);
	}

	public Long selectAuthorIdByEventId(final Long eventId)
	{
		try
		{
			final String jpql = "SELECT e.author.id FROM Event e WHERE e.id = :eventId";
			final TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
			query.setParameter(EVENTID, eventId);
			return query.getSingleResult();
		}
		catch (final NoResultException e)
		{
			return 0L;
		}
	}

	public Author selectAuthorByAuthorName(final String authorName)
	{
		try
		{
			final String jpql = "SELECT a FROM Author a WHERE a.name = :name";
			final TypedQuery<Author> query = entityManager.createQuery(jpql, Author.class);
			query.setParameter(NAME, authorName);
			return query.getSingleResult();
		}
		catch (final NoResultException e)
		{
			return null;
		}
	}

	public String selectPasswordByAuthorName(final String authorName)
	{
		try
		{
			final String jpql = "SELECT a.password FROM Author a WHERE a.name = :name";
			final TypedQuery<String> query = entityManager.createQuery(jpql, String.class);
			query.setParameter(NAME, authorName);
			return query.getSingleResult();
		}
		catch (final NoResultException e)
		{
			return null;
		}
	}

	public boolean isAuthor(final String name)
	{
		try
		{
			final String jpql = "SELECT COUNT(a.name) FROM Author a WHERE a.name = :name";
			final TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
			query.setParameter(NAME, name);
			return query.getSingleResult() == 1L;
		}
		catch (final NoResultException e)
		{
			return false;
		}
	}

	public boolean isEventsAuthor(final Long eventId, final String name)
	{
		if (!eventService.countEventsById(eventId))
		{
			HttpErrorHandler.print404("There is no event for this id in the database");
			return false;
		}
		final Author author = selectAuthorByAuthorName(name);
		if (author.getId() != selectAuthorIdByEventId(eventId))
		{
			HttpErrorHandler.print401("The author cannot access to this event");
			return false;
		}
		return true;
	}

}
