package com.website.persistence;

import static com.website.persistence.EntityAttributes.EMAIL;

import javax.ejb.Stateless;
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
public class UserService {

	/** The entity manager binding the model to the persistence entity. */
	@PersistenceContext(unitName = "website")
	private EntityManager entityManager;

	/**
	 * Finds the user with the specified id.
	 *
	 * @param id the id
	 * @return the found user
	 */
	public User findUserById(final Long id) {
		return entityManager.find(User.class, id);
	}

	/**
	 * Finds the user with the specified email.
	 *
	 * @param email the email
	 * @return the found user
	 */
	public User findUserByEmail(final String email) {
		try {
			final String jpql = "SELECT user FROM User user WHERE user.email = :email";
			final TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
			query.setParameter(EMAIL.getName(), email);
			return query.getSingleResult();
		}
		catch (final NoResultException e) {
			return null;
		}
	}

	/**
	 * Persists the user.
	 *
	 * @param user the user to persist
	 */
	public void persistUser(final User user) {
		entityManager.persist(user);
	}
}
