package com.website.persistence;

import static com.website.persistence.EntityAttributes.URL;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.website.models.entities.Visit;

/**
 * The service managing the visit persistence.</br>
 *
 * @author Jérémy Pansier
 */
@Stateless
public class VisitService {

	/** The entity manager binding the model to the persistence entity. */
	@PersistenceContext(unitName = "website")
	private EntityManager entityManager;

	/**
	 * Counts the visits for the specified URL grouped by URL.
	 *
	 * @param url the visited URL
	 * @return the visits count
	 */
	public Long countVisitsByUrlGroupByUrl(final String url) {
		try {
			final String jpql = "SELECT COUNT(visit) FROM Visit visit WHERE visit.url = :url GROUP BY visit.url";
			final TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
			query.setParameter(URL.getName(), url);
			return query.getSingleResult();
		}
		catch (final NoResultException e) {
			return 0L;
		}
	}

	/**
	 * Finds the visits.
	 *
	 * @return the found visits
	 */
	public List<Visit> findVisits() {
		final String jpql = "SELECT visit FROM Visit visit";
		final TypedQuery<Visit> query = entityManager.createQuery(jpql, Visit.class);
		return query.getResultList();
	}

	/**
	 * Persists a new visit with the specified URL and IP address.
	 *
	 * @param url the visited URL
	 * @param ip the visitor IP address
	 */
	public void persistVisit(final String url, final String ip) {
		final Visit visit = new Visit();
		visit.setUrl(url);
		visit.setIp(ip);
		entityManager.persist(visit);
	}

	/**
	 * Removes all the visits.
	 */
	public void removeVisits() {
		final List<Visit> visits = findVisits();
		for (final Visit visit : visits) {
			entityManager.remove(visit);
		}
	}
}
