package com.website.views.pages;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.mindrot.jbcrypt.BCrypt;

import com.website.persistence.AuthorService;
import com.website.tools.navigation.Redirector;
import com.website.tools.navigation.SessionManager;
import com.website.views.WebPages;

/**
 * The connection web page view.</br>
 * This {@link Named} class is bound with the same named xhtml file.
 * 
 * @author Jérémy Pansier
 */
@Named
@RequestScoped
public class Connection {

	/** The service managing the author persistence. */
	@Inject
	private AuthorService authorService;

	/** The web page. */
	public static final WebPages WEB_PAGE = WebPages.CONNECTION;

	/** The author name used to log in. */
	private String authorName;

	/** The password. */
	private String password;

	/**
	 * @return the web page
	 */
	public WebPages getWebPage() {
		return WEB_PAGE;
	}

	/**
	 * Gets the author name used to log in.
	 *
	 * @return the author name used to log in
	 */
	public String getAuthorName() {
		return authorName;
	}

	/**
	 * Sets the author name used to log in.
	 *
	 * @param authorName the new author name used to log in
	 */
	public void setAuthorName(final String authorName) {
		this.authorName = authorName;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * Logs the website user in.
	 */
	public void login() {
		SessionManager.trackUser(authorName);

		if (!authorService.isAuthor(authorName)) {
			Redirector.redirect(WEB_PAGE.createJsfUrl(), true, "Utilisateur inconnu ou mot de passe incorrect");
			return;
		}

		final String storedPassword = authorService.findPasswordByAuthorName(authorName);

		if (!BCrypt.checkpw(password, storedPassword)) {
			Redirector.redirect(WEB_PAGE.createJsfUrl(), true, "Utilisateur inconnu ou mot de passe incorrect");
			return;
		}

		Redirector.redirect(WebPages.EVENTS_LIST.createJsfUrl(), false, "Connection réussie");
	}
}
