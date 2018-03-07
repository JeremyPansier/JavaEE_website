package com.website.views.pages;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.mindrot.jbcrypt.BCrypt;

import com.website.models.entities.Author;
import com.website.persistence.AuthorService;
import com.website.tools.navigation.Redirector;
import com.website.tools.navigation.SessionManager;
import com.website.views.WebPages;

/**
 * The registration web page view.</br>
 * This {@link Named} class is bound with the same named xhtml file.
 * 
 * @author Jérémy Pansier
 */
@Named
@RequestScoped
public class Registration {

	/** The service managing the author persistence. */
	@Inject
	private AuthorService authorService;

	/** The web page. */
	public static final WebPages WEB_PAGE = WebPages.REGISTRATION;

	/** The author name used to register. */
	private String authorName;

	/** The password. */
	private String password;

	/** The confirmation password. */
	private String confirmationPassword;

	/**
	 * @return the web page
	 */
	public WebPages getWebPage() {
		return WEB_PAGE;
	}

	/**
	 * Gets the author name used to register.
	 *
	 * @return the author name used to register
	 */
	public String getAuthorName() {
		return authorName;
	}

	/**
	 * Sets the author name used to register.
	 *
	 * @param authorName the new author name used to register
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
	 * Gets the confirmation password.
	 *
	 * @return the confirmation password
	 */
	public String getConfirmationPassword() {
		return confirmationPassword;
	}

	/**
	 * Sets the confirmation password.
	 *
	 * @param confirmationPassword the new confirmation password
	 */
	public void setConfirmationPassword(final String confirmationPassword) {
		this.confirmationPassword = confirmationPassword;
	}

	/**
	 * Registers the new author with the name and password he specified.
	 */
	public void signin() {
		SessionManager.trackUser(authorName);

		if (0 != password.compareTo(confirmationPassword)) {
			Redirector.redirect(WEB_PAGE.createJsfUrl(), true, "Les mots de passes ne sont pas identiques");
			return;
		}

		if (0 == "".compareTo(authorName) || authorService.isAuthor(authorName)) {
			Redirector.redirect(WEB_PAGE.createJsfUrl(), true, "Ce nom d'utilisateur n'est pas valide");
			return;
		}

		final String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
		final Author author = new Author();
		author.setname(authorName);
		author.setPassword(hashedPassword);
		authorService.persistAuthor(author);
		Redirector.redirect(WebPages.PROFILE_EDITION.createJsfUrl(), false, "Inscription réussie");
	}
}
