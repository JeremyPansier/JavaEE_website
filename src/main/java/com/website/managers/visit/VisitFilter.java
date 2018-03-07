package com.website.managers.visit;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.website.persistence.VisitService;
import com.website.tools.navigation.ContextManager;

/**
 * The phase-listener for recording website visits (<i>cf.</i> faces-config.xml).
 *
 * @author Jérémy Pansier
 */
public class VisitFilter implements PhaseListener {

	/** The serial version UID. */
	private static final long serialVersionUID = 9171765153715621420L;

	/** The service managing the visit persistence. */
	@Inject
	private VisitService visitService;

	@Override
	public void afterPhase(final PhaseEvent event) {

		final HttpServletRequest request = ContextManager.getRequest();
		final String ipAddress = request.getRemoteAddr();
		final String url = request.getRequestURL().toString();
		visitService.persistVisit(url, ipAddress);
	}

	@Override
	public void beforePhase(final PhaseEvent event) {
		// DO NOTHING
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}
}
