package com.website.views.pages;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.website.models.entities.Author;
import com.website.models.entities.Profile;
import com.website.persistence.AuthorService;
import com.website.tools.navigation.Redirector;
import com.website.tools.navigation.SessionManager;
import com.website.views.WebPages;

/**
 * The profile edition web page view.</br>
 * This {@link Named} class is bound with the same named xhtml file.
 * 
 * @author Jérémy Pansier
 */
@Named
@ViewScoped
public class ProfileEdition implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = -1355154107935948964L;

	/** The web page. */
	public static final WebPages WEB_PAGE = WebPages.PROFILE_EDITION;

	/** The service managing the author persistence. */
	@Inject
	private AuthorService authorService;

	/** The profile. */
	private transient Profile profile;

	/** The author. */
	private transient Author author;

	/**
	 * Initializes the author just after the construction.
	 */
	@PostConstruct
	public void init() {
		final String username = SessionManager.getSessionUserNameOrRedirect();
		author = authorService.findAuthorByAuthorName(username);

		// profile = eventService.selectProfileByIdProfile(author.getId());
	}

	/**
	 * Gets the web page.
	 *
	 * @return the web page
	 */
	public WebPages getWebPage() {
		return WEB_PAGE;
	}

	/**
	 * Gets the profile.
	 *
	 * @return the profile
	 */
	public Profile getProfile() {
		return profile;
	}

	/**
	 * Gets the author.
	 *
	 * @return the author
	 */
	public Author getAuthor() {
		return author;
	}

// public void load() throws IOException {
// }

// public void upload(FileUploadEvent fue) {
// profile.setFilename(Tools.uploadMyFile(fue));
// }

	/**
	 * Edits the profile.
	 */
	public void editProfile() {
// if (!eventService.isEventsAuthor(id, sessionUsername, ec)) {
// return null;
// }
// eventService.updateEvent(id, event.getTitle(), event.getDescription(), event.getFilename());
// Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
// flash.put("flash.message", "Evènement édité avec succès");
		Redirector.redirect(WebPages.PROFILE_EDITION.createJsfUrl());
	}
}
