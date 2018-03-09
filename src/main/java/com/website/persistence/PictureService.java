package com.website.persistence;

import static com.website.persistence.EntityAttributes.ID;
import static com.website.persistence.EntityAttributes.NAME;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.website.models.entities.Author;
import com.website.models.entities.Picture;
import com.website.tools.error.HttpErrorHandler;

/**
 * The service managing the picture persistence.</br>
 *
 * @author Jérémy Pansier
 */
@Stateless
public class PictureService {

	/** The entity manager binding the model to the persistence entity. */
	@PersistenceContext(unitName = "website")
	private EntityManager entityManager;

	/** The service managing the author persistence. */
	@Inject
	private AuthorService authorService;

	/**
	 * Counts the pictures with the specified id.
	 *
	 * @param pictureId the picture id
	 * @return the pictures count
	 */
	public Long countPicturesById(final Long pictureId) {
		final String jpql = "SELECT COUNT(picture) FROM Picture picture WHERE picture.id=:id";
		final TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
		query.setParameter(ID.getName(), pictureId);
		return query.getSingleResult();
	}

	/**
	 * Finds the author id for the specified picture id.
	 *
	 * @param pictureId the picture id
	 * @return the found author id
	 */
	public Long findAuthorIdByPictureId(final Long pictureId) {
		final String jpql = "SELECT picture.author.id FROM Picture picture WHERE picture.id = :id";
		final TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
		query.setParameter(ID.getName(), pictureId);
		return query.getSingleResult();
	}

	/**
	 * Finds the pictures for specified author name.
	 *
	 * @param authorName the author name
	 * @return the found pictures or an empty list if there is no picture for the specified author name
	 */
	public List<Picture> findPicturesByAuthorName(final String authorName) {
		final String jpql = "SELECT picture FROM Picture picture JOIN picture.author author WHERE author.name =:name";
		final TypedQuery<Picture> query = entityManager.createQuery(jpql, Picture.class);
		query.setParameter(NAME.getName(), authorName);
		return query.getResultList();
	}

	/**
	 * Finds the picture with the specified picture id.
	 *
	 * @param pictureId the picture id
	 * @return the found picture
	 */
	public Picture findPictureByPictureId(final Long pictureId) {
		return entityManager.find(Picture.class, pictureId);
	}

	/**
	 * Checks if the specified name is name of the author of the picture with the specified id.
	 *
	 * @param pictureId the picture id
	 * @param authorName the author name
	 * @return
	 *         <ul>
	 *         <li>true, if the specified name is name of the author of the picture with the specified id</li>
	 *         <li>false, otherwise or if there is no event for the specified id in the database</li>
	 *         </ul>
	 */
	public boolean isPictureAuthor(final Long pictureId, final String authorName) {
		if (0 == countPicturesById(pictureId)) {
			HttpErrorHandler.print404("There is no event for this id in the database");
			return false;
		}
		final Author author = authorService.findAuthorByAuthorName(authorName);
		if (author.getId() != findAuthorIdByPictureId(pictureId)) {
			HttpErrorHandler.print401("The author cannot access to this event");
			return false;
		}
		return true;
	}

	/**
	 * Persists the specified picture.
	 * 
	 * @param picture the picture to persist
	 */
	public void persistPicture(final Picture picture) {
		entityManager.persist(picture);
	}

	/**
	 * Removes the specified picture.
	 * </p>
	 * <b>Note:</b> </br>
	 * This method uses the {@link EntityManager}.</br>
	 * But here's the thing: {@link EntityManager#remove} works only on entities which are managed in the current transaction/context. To remove entities which are not managed, the only way is to make
	 * it managed by merging the specified entity with the managed entity that have the same id.</br>
	 * For this purpose, this method first checks if the entity is managed (with {@link EntityManager#contains}) and if not, then make it managed (with {@link EntityManager#merge}).</br>
	 * Only then will the entity be removed (with {@link EntityManager#remove}).
	 * 
	 * @param picture the picture to remove
	 */
	public void removePicture(final Picture picture) {
		Picture managedPicture = picture;
		/* Check if the picture is managed */
		if (!entityManager.contains(picture)) {
			/* If not, then make it managed */
			managedPicture = entityManager.merge(picture);
		}
		/* Remove the picture */
		entityManager.remove(managedPicture);
	}

	/**
	 * Updates the specified picture.
	 *
	 * @param picture the picture to update
	 */
	public void updatePicture(final Picture picture) {
		entityManager.merge(picture);
	}

}
