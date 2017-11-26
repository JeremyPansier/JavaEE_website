package com.website.jsf;

import java.io.IOException;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;

import org.mindrot.jbcrypt.BCrypt;

import com.website.enumeration.Webpagename;
import com.website.services.EventService;
import com.website.tools.PathBuilder;

@Named
@RequestScoped
public class Register
{
	@Inject
	private EventService eventService;
	private String username;
	private String password;
	private String confirmationPassword;

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getConfirmationPassword()
	{
		return confirmationPassword;
	}

	public void setConfirmationPassword(String confirmationPassword)
	{
		this.confirmationPassword = confirmationPassword;
	}

	public void register() throws IOException
	{
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

		if (sessionMap.get("username") != username)
		{
			sessionMap.put("username", username);
		}

		if (eventService.isAuthor(username))
		{
			flash.put("flash.message", "le pseudonyme \"" + username + "\" est déjà pris");
			sessionMap.clear();
			ec.redirect(PathBuilder.getJsfPath(Webpagename.register.toString()));
			return;
		}

		if ("".compareTo(username) == 0)
		{
			flash.put("flash.message", "Le nom d'utilisateur n'est pas valide");
			sessionMap.clear();
			ec.redirect(PathBuilder.getJsfPath(Webpagename.register.toString()));
			return;
		}

		if (password.compareTo(confirmationPassword) != 0)
		{
			flash.put("flash.message", "Les mots de passes ne sont pas identiques");
			sessionMap.clear();
			ec.redirect(PathBuilder.getJsfPath(Webpagename.register.toString()));
			return;
		}

		String passwordDB = BCrypt.hashpw(password, BCrypt.gensalt(12));
		eventService.insertAuthor(username, passwordDB);
		flash.put("flash.message", "Inscription réussie");
		ec.redirect(PathBuilder.getJsfPath(Webpagename.eventCreation.toString()));
	}
}
