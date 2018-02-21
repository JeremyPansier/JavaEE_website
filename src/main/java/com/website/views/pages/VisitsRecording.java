package com.website.views.pages;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.website.models.entities.Visit;
import com.website.persistence.VisitService;
import com.website.tools.EventServiceException;
import com.website.tools.context.HttpErrorHandler;
import com.website.tools.context.Redirector;
import com.website.tools.context.SessionManager;
import com.website.tools.data.TableManager;
import com.website.views.WebPages;

/**
 * The visits recording web page view.</br>
 * This {@link Named} class is bound with the same named xhtml file.
 * 
 * @author Jérémy Pansier
 */
@Named
@RequestScoped
public class VisitsRecording {

	/** The web page. */
	public static final WebPages WEB_PAGE = WebPages.VISITS_RECORDING;

	/** The service managing the visit persistence. */
	@Inject
	private VisitService visitService;

	/** The visited web pages. */
	private List<Visit> webpages;

	/** The down-loaded files. */
	private List<Visit> files;

	/** The chart row. */
	private String chartRow;

	/**
	 * Initializes the web pages and the files just after the construction.
	 */
	@PostConstruct
	public void init() {
		try {
			final String username = SessionManager.checkSessionUserName();
			if ("admin".compareTo(username) != 0) {
				HttpErrorHandler.print401("L'autheur n'a pas accès a cette page");
				return;
			}
			final List<Visit> visits = visitService.selectVisits();
			webpages = TableManager.groupByName(visits, TableManager.WEBPAGES);
			files = TableManager.groupByName(visits, "files");
			Collections.sort(webpages);
			Collections.sort(files);

			if (!webpages.isEmpty()) {
				final StringBuilder chartRowBuilder = new StringBuilder("['" + webpages.get(0).getResourceName() + "', " + webpages.get(0).getCount() + "]");
				for (int i = 1; i < webpages.size(); i++) {
					chartRowBuilder.append(", ['" + webpages.get(i).getResourceName() + "', " + webpages.get(i).getCount() + "]");
				}
				chartRow = chartRowBuilder.toString();
			}

		}
		catch (final EventServiceException e) {
			HttpErrorHandler.print500(e);
			return;
		}
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
	 * Gets the visited web pages.
	 *
	 * @return the visited web pages
	 */
	public List<Visit> getWebpages() {
		return webpages;
	}

	/**
	 * Gets the down-loaded files.
	 *
	 * @return the down-loaded files
	 */
	public List<Visit> getFiles() {
		return files;
	}

	/**
	 * Gets the chart row.
	 *
	 * @return the chart row
	 */
	public String getChartRow() {
		return chartRow;
	}

	/**
	 * Delete all the recorded visits.
	 */
	public void deleteVisits() {
		try {
			visitService.deleteVisits();
		}
		catch (final EventServiceException eventServiceException) {
			HttpErrorHandler.print500(eventServiceException);
			return;
		}
		Redirector.redirect(WebPages.VISITS_RECORDING.createJsfUrl());
	}
}
