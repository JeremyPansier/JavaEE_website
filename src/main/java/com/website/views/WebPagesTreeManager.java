package com.website.views;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.website.views.pages.EventCreation;
import com.website.views.pages.EventEdition;
import com.website.views.pages.EventManagement;
import com.website.views.pages.EventReport;
import com.website.views.pages.EventsList;
import com.website.views.pages.PictureEdition;
import com.website.views.pages.PicturePublication;
import com.website.views.pages.PictureReport;
import com.website.views.pages.PicturesGallery;

/**
 * The web pages tree manager.</br>
 * Allows to know the position of a web page into the web pages tree.
 *
 * @author Jérémy Pansier
 */
@Named
@ViewScoped
public class WebPagesTreeManager implements Serializable
{
	/** The serialVersionUID. */
	private static final long serialVersionUID = -2091808123773827378L;

	/**
	 * Checks if the specified web page is the child of the other specified web page.
	 *
	 * @param childtWebPage the web page which should be the parent of the second one
	 * @param parentWebPage the web page which should be the parent of the first one
	 * @return true, if the first specified web page is the child of the second one.
	 */
	public boolean checkChild(final WebPages childtWebPage, final WebPages parentWebPage)
	{
		if (parentWebPage == EventsList.WEB_PAGE)
		{
			return childtWebPage.getFilename().equals(EventCreation.WEB_PAGE.getFilename())
					|| childtWebPage.getFilename().equals(EventEdition.WEB_PAGE.getFilename())
					|| childtWebPage.getFilename().equals(EventManagement.WEB_PAGE.getFilename())
					|| childtWebPage.getFilename().equals(EventReport.WEB_PAGE.getFilename());
		}
		else if (parentWebPage == PicturesGallery.WEB_PAGE)
		{
			return childtWebPage.getFilename().equals(PicturePublication.WEB_PAGE.getFilename())
					|| childtWebPage.getFilename().equals(PictureEdition.WEB_PAGE.getFilename())
					|| childtWebPage.getFilename().equals(PictureReport.WEB_PAGE.getFilename());
		}
		else
		{
			return false;
		}
	}
}
