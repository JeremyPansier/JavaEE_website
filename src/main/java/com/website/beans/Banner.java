package com.website.beans;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Named;

@Named
@RequestScoped
public class Banner
{

	private String message;
	private String username;

	public String getMessage()
	{
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		message = (String) flash.get("flash.message");
		return message;
	}

	public String getUsername()
	{
		Map<String, Object> httpSession = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		username = (String) httpSession.get("username");
		return username;
	}
}
