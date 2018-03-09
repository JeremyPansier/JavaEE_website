package com.website.persistence;

import static com.website.persistence.EntityAttributes.ID;
import static com.website.persistence.EntityAttributes.NAME;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.website.models.entities.Author;

/**
 * The service managing the author persistence.</br>
 *
 * @author Jérémy Pansier
 */
@Stateless
public class AuthorService {

	/** The entity manager binding the model to the persistence entity. */
	@PersistenceContext(unitName = "website")
	private EntityManager entityManager;

	/**
	 * Finds the author id for the specified event id.
	 *
	 * @param eventId the event id
	 * @return the found author id or null if there is no author for the specified event id
	 */
	public Long findAuthorIdByEventId(final Long eventId) {
		try {
			final String jpql = "SELECT event.author.id FROM Event event WHERE event.id = :id";
			final TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
			query.setParameter(ID.getName(), eventId);
			return query.getSingleResult();
		}
		catch (final NoResultException noSuchResultException) {
			return null;
		}
	}

	/**
	 * Finds the author for the specified author name.
	 *
	 * @param authorName the author name
	 * @return the found author or null if there is no author for the specified name
	 */
	public Author findAuthorByAuthorName(final String authorName) {
		try {
			final String jpql = "SELECT author FROM Author author WHERE author.name = :name";
			final TypedQuery<Author> query = entityManager.createQuery(jpql, Author.class);
			query.setParameter(NAME.getName(), authorName);
			return query.getSingleResult();
		}
		catch (final NoResultException noResultException) {
			return null;
		}
	}

	/**
	 * Finds the password for the specified author name.
	 *
	 * @param authorName the author name
	 * @return the found password or null if there is no password for the specified name
	 */
	public String findPasswordByAuthorName(final String authorName) {
		try {
			final String jpql = "SELECT author.password FROM Author author WHERE author.name = :name";
			final TypedQuery<String> query = entityManager.createQuery(jpql, String.class);
			query.setParameter(NAME.getName(), authorName);
			return query.getSingleResult();
		}
		catch (final NoResultException noResultException) {
			return null;
		}
	}

	/**
	 * Checks if the specified name is an author name.
	 *
	 * @param name the name
	 * @return true, if at least one author is bearing the specified name
	 */
	public boolean isAuthor(final String name) {
		try {
			final String jpql = "SELECT COUNT(author.name) FROM Author author WHERE author.name = :name";
			final TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
			query.setParameter(NAME.getName(), name);
			return 0L < query.getSingleResult();
		}
		catch (final NoResultException noResultException) {
			return false;
		}
	}

	/**
	 * Persists the author.
	 * 
	 * @param author the author to persist
	 */
	public void persistAuthor(final Author author) {
		entityManager.persist(author);
	}
}
