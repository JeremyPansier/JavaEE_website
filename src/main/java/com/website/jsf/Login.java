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
public class Login
{
	@Inject
	private EventService eventService;
	private String username;
	private String password;

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

	public void login() throws IOException
	{
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

		if (sessionMap.get("username") != username)
		{
			sessionMap.put("username", username);
		}

		if (!eventService.isAuthor(username))
		{
			flash.put("flash.message", "Utilisateur inconnu ou mot de passe incorrect");
			sessionMap.clear();
			ec.redirect(PathBuilder.getJsfPath(Webpagename.login.toString()));
			return;
		}

		String passwordDB = eventService.selectPasswordByUserName(username);

		if (!BCrypt.checkpw(password, passwordDB))
		{
			flash.put("flash.message", "Utilisateur inconnu ou mot de passe incorrect");
			sessionMap.clear();
			ec.redirect(PathBuilder.getJsfPath(Webpagename.login.toString()));
			return;
		}

		flash.put("flash.message", "Connection réussie");
		ec.redirect(PathBuilder.getJsfPath(Webpagename.eventsList.toString()));
	}
}
