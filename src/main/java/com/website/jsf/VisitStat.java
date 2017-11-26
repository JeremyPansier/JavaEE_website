package com.website.jsf;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.website.entities.Visit;
import com.website.enumeration.Constant;
import com.website.enumeration.Webpagename;
import com.website.services.EventService;
import com.website.tools.EventServiceException;
import com.website.tools.HttpErrorHandler;
import com.website.tools.PathBuilder;
import com.website.tools.SessionChecker;
import com.website.tools.TableManager;

@Named
@RequestScoped
public class VisitStat {
   @Inject
    private EventService eventService;

    private List<Visit> webpages;
    private List<Visit> files;
    private String chartRow;

    @PostConstruct
    public void init() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            String username = SessionChecker.getUsername(ec);
            if (username.compareTo(Constant.admin.toString()) != 0) {
                HttpErrorHandler.print401(ec, "L'autheur n'a pas accès a cette page");
                return;
            }
            List<Visit> visits = eventService.selectVisits();
            webpages = TableManager.groupByName(visits, TableManager.WEBPAGES);
            files = TableManager.groupByName(visits, "files");
            Collections.sort(webpages);
            Collections.sort(files);
            
            if (webpages.size() > 0) {
                chartRow = "['" + webpages.get(0).getResourceName() + "', " + webpages.get(0).getCount() + "]";
                for (int i = 1; i < webpages.size(); i++) {
                    chartRow = chartRow + ", ['" + webpages.get(i).getResourceName() + "', " + webpages.get(i).getCount() + "]";
                }
            }

        } catch (EventServiceException e) {
            HttpErrorHandler.print500(ec, e);
            return;
        }
    }
    
    public List<Visit> getWebpages() {
        return webpages;
    }

    public List<Visit> getFiles() {
        return files;
    }

    public String getChartRow() {
        return chartRow;
    }

    public int getSize() {
        files.size();
        return files.size();
    }

    public void deleteVisits() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            eventService.deleteVisits();
        } catch (EventServiceException eventServiceException) {
            HttpErrorHandler.print500(ec, eventServiceException);
            return;
        }
        ec.redirect(PathBuilder.getJsfPath(Webpagename.visitStat.toString()));
    }
}
