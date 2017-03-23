package com.aiconoa.trainings.filters;

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

import com.aiconoa.trainings.services.EventService;


@WebFilter("/*")
public class FlashScopeFilter implements Filter {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();
    @Inject
    private EventService eventService;

    private static final String FLASH_SESSION_KEY = "FLASH_SESSION_KEY";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        // reinstate any flash scoped params from the users session
        // and clear the session
        if (request instanceof HttpServletRequest) {
            flashParamReinstanciation(request);
            String ipAddress = request.getRemoteAddr();
            String url = ((HttpServletRequest) request).getRequestURL().toString();
            try {
                eventService.insertVisit(url, ipAddress);
            } catch (Exception e) {
                LOGGER.error("probleme de filtre", e);
                return;
            }
        }

        // process the chain
        chain.doFilter(request, response);

        // store any flash scoped params in the user's session for the
        // next request
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            Map<String, Object> flashParams = new HashMap<>();
            flashParamStorage(request, httpRequest, flashParams);
            if (flashParams.size() > 0) {
                HttpSession session = httpRequest.getSession(false);
                session.setAttribute(FLASH_SESSION_KEY, flashParams);
            }
        }
    }

    public void flashParamReinstanciation(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        if (session != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> flashParams = (Map<String, Object>) session.getAttribute(FLASH_SESSION_KEY);
            if (flashParams != null) {
                for (Map.Entry<String, Object> flashEntry : flashParams.entrySet()) {
                    request.setAttribute(flashEntry.getKey(), flashEntry.getValue());
                }
                session.removeAttribute(FLASH_SESSION_KEY);
            }
        }
    }

    public void flashParamStorage(ServletRequest request, HttpServletRequest httpRequest, Map<String, Object> flashParams) {
        Enumeration<?> e = httpRequest.getAttributeNames();
        while (e.hasMoreElements()) {
            String paramName = (String) e.nextElement();
            if (paramName.startsWith("flash.")) {
                Object value = request.getAttribute(paramName);
                paramName = paramName.substring(6, paramName.length());
                flashParams.put(paramName, value);
            }
        }        
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //Do nothing
    }

    @Override
    public void destroy() {
        //Do nothing
    }
}