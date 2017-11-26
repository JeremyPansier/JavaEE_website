package com.website.tools;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class Redirector implements Serializable
{

	private static final long serialVersionUID = 1148601551383990989L;

	public void redirect(String link) throws IOException
	{
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(link + ".xhtml");
	}

	public void redirectEvent(String link, int id) throws IOException
	{
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(link + ".xhtml?eventId=" + id);
	}

	public void redirectPicture(String link, int id) throws IOException
	{
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(link + ".xhtml?pictureId=" + id);
	}
}
