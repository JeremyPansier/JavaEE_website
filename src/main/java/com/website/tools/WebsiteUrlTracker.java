package com.website.tools;

import javax.servlet.http.HttpServletRequest;

public class WebsiteUrlTracker {
    
    public static String getOriginWebSite() {
            HttpServletRequest origRequest = (HttpServletRequest) ServletContextTracker.getServletContext();
            String url = origRequest.getRequestURL()
                                    .toString();
            return url.substring(0, url.lastIndexOf('/'));
    }
}