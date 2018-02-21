package com.website.filters;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;

import com.website.persistence.VisitService;

@WebFilter("/*")
public class FlashScopeFilter implements Filter {

	/** The Logger. */
	private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();

	/** The flash session key. */
	private static final String FLASH_SESSION_KEY = "FLASH_SESSION_KEY";

	/** The service managing the visit persistence. */
	@Inject
	private VisitService visitService;

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {

		/* re-instantiate any flash scoped parameter from the users session and clear the session */
		if (request instanceof HttpServletRequest) {
			flashParamReinstantiation(request);
			final String ipAddress = request.getRemoteAddr();
			final String url = ((HttpServletRequest) request).getRequestURL().toString();
			try {
				visitService.insertVisit(url, ipAddress);
			}
			catch (final Exception e) {
				LOGGER.error("probleme de filtre", e);
				return;
			}
		}

		// process the chain
		chain.doFilter(request, response);

		/* store any flash scoped parameter in the user's session for the next request */
		if (request instanceof HttpServletRequest) {
			final HttpServletRequest httpRequest = (HttpServletRequest) request;
			final Map<String, Object> flashParams = new HashMap<>();
			flashParamStorage(request, httpRequest, flashParams);
			if (flashParams.size() > 0) {
				final HttpSession session = httpRequest.getSession(true);
				session.setAttribute(FLASH_SESSION_KEY, flashParams);
			}
		}
	}

	public void flashParamReinstantiation(final ServletRequest request) {
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final HttpSession session = httpRequest.getSession();
		if (session != null) {
			@SuppressWarnings("unchecked")
			final Map<String, Object> flashParams = (Map<String, Object>) session.getAttribute(FLASH_SESSION_KEY);
			if (flashParams != null) {
				for (final Map.Entry<String, Object> flashEntry : flashParams.entrySet()) {
					request.setAttribute(flashEntry.getKey(), flashEntry.getValue());
				}
				session.removeAttribute(FLASH_SESSION_KEY);
			}
		}
	}

	public void flashParamStorage(final ServletRequest request, final HttpServletRequest httpRequest, final Map<String, Object> flashParams) {
		final Enumeration<?> e = httpRequest.getAttributeNames();
		while (e.hasMoreElements()) {
			String paramName = (String) e.nextElement();
			if (paramName.startsWith("flash.")) {
				final Object value = request.getAttribute(paramName);
				paramName = paramName.substring(6);
				flashParams.put(paramName, value);
			}
		}
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		// Do nothing
	}

	@Override
	public void destroy() {
		// Do nothing
	}
}
