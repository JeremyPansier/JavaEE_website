package com.website.jsf;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.website.entities.Author;
import com.website.entities.Profile;
import com.website.enumeration.Webpagename;
import com.website.services.EventService;
import com.website.tools.EventServiceException;
import com.website.tools.HttpErrorHandler;
import com.website.tools.PathBuilder;
import com.website.tools.SessionChecker;

@Named
@ViewScoped
public class ProfileEdition implements Serializable
{
	private static final long serialVersionUID = -1355154107935948964L;
	private Author author;
	private Profile profile;
	@Inject
	private EventService eventService;

	@PostConstruct
	public void init() throws IOException
	{
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try
		{
			String username = SessionChecker.getUsername(ec);
			author = eventService.selectAuthorByUsername(username);

// profile = eventService.selectProfileByIdProfile(author.getId());
		} catch (NullPointerException e)
		{
// ec.redirect(ec.getRequestContextPath() + "/faces/" + Webpagename.eventCreation.toString() + "." + Webpagename.xhtml.toString());
			return;
		} catch (EventServiceException eventServiceException)
		{
			HttpErrorHandler.print500(ec, eventServiceException);
			return;
		}
	}

	public Author getAuthor()
	{
		return author;
	}

	public void setAuthor(Author author)
	{
		this.author = author;
	}

	public Profile getProfile()
	{
		return profile;
	}

	public void setProfile(Profile profile)
	{
		this.profile = profile;
	}

// public void load() throws IOException {
// }

// public void upload(FileUploadEvent fue) {
// profile.setFilename(Tools.uploadMyFile(fue));
// }

	public void editProfile() throws IOException
	{
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
// if (!eventService.isEventsAuthor(id, sessionUsername, ec)) {
// return null;
// }
// eventService.updateEvent(id, event.getTitle(), event.getDescription(), event.getFilename());
// Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
// flash.put("flash.message", "Evènement édité avec succès");
		ec.redirect(PathBuilder.getJsfPath(Webpagename.profileEdition.toString()));
	}
}
