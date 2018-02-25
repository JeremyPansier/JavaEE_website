package com.website.filters;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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

import com.website.persistence.VisitService;

@WebFilter("/*")
public class FlashScopeFilter implements Filter {

	/** The first part of the flash parameter name that which will be caught by the filter. */
	public static final String FLASH_PARAM_NAME_START = "flash.";

	/** The flash session scope. */
	private static final String FLASH_SESSION_SOPE = "FLASH_SESSION_SCOPE";

	/** The service managing the visit persistence. */
	@Inject
	private VisitService visitService;

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {

		/* Re-instantiate any flash scoped parameter from the user session and clear the session */
		if (request instanceof HttpServletRequest) {
			flashParamReinstantiation(request);
			final String ipAddress = request.getRemoteAddr();
			final String url = ((HttpServletRequest) request).getRequestURL().toString();
			visitService.insertVisit(url, ipAddress);
		}

		/* Process the chain */
		chain.doFilter(request, response);

		/* Store any flash scoped parameter in the user session for the next request */
		if (request instanceof HttpServletRequest) {
			final HttpServletRequest httpRequest = (HttpServletRequest) request;
			final Map<String, Object> flashParams = new HashMap<>();
			flashParamStorage(request, httpRequest, flashParams);
			if (flashParams.size() > 0) {
				final HttpSession session = httpRequest.getSession(false);
				session.setAttribute(FLASH_SESSION_SOPE, flashParams);
			}
		}
	}

	public void flashParamReinstantiation(final ServletRequest request) {
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final HttpSession session = httpRequest.getSession();
		if (session != null) {
			@SuppressWarnings("unchecked")
			final Map<String, Object> flashParams = (Map<String, Object>) session.getAttribute(FLASH_SESSION_SOPE);
			if (flashParams != null) {
				for (final Entry<String, Object> flashEntry : flashParams.entrySet()) {
					request.setAttribute(flashEntry.getKey(), flashEntry.getValue());
				}
				session.removeAttribute(FLASH_SESSION_SOPE);
			}
		}
	}

	public void flashParamStorage(final ServletRequest request, final HttpServletRequest httpRequest, final Map<String, Object> flashParams) {
		final Enumeration<String> attributeNames = httpRequest.getAttributeNames();
		while (attributeNames.hasMoreElements()) {
			String paramName = attributeNames.nextElement();
			if (paramName.startsWith(FLASH_PARAM_NAME_START)) {
				final Object value = request.getAttribute(paramName);
				paramName = paramName.substring(FLASH_PARAM_NAME_START.length());
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
