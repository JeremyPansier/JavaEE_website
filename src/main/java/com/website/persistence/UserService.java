package com.website.persistence;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.website.models.entities.User;

/**
 * The service managing the user persistence.</br>
 *
 * @author Jérémy Pansier
 */
@Stateless
public class UserService
{
	/** The entity manager binding the model to the persistence entity. */
	@PersistenceContext(unitName = "website")
	private EntityManager entityManager;

	/** The service managing the event persistence. */
	@Inject
	EventService eventService;

	/** The email persistence data category name. */
	private static final String EMAIL = PersistenceDataCategories.EMAIL.getName();

	public User insertUser(final String email)
	{
		final User u = new User();
		u.setEmail(email);
		entityManager.persist(u);
		return u;
	}

	public User selectUserById(final Long id)
	{
		return entityManager.find(User.class, id);
	}

	public User selectUserByEmail(final String email)
	{
		try
		{
			final String jpql = "SELECT u FROM User u WHERE u.email = :email";
			final TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
			query.setParameter(EMAIL, email);
			return query.getSingleResult();
		}
		catch (final NoResultException e)
		{
			return null;
		}
	}
}
