package com.aiconoa.trainings.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aiconoa.trainings.entity.Stats;
import com.aiconoa.trainings.services.EventService;
import com.aiconoa.trainings.services.EventServiceException;
import com.aiconoa.trainings.services.HttpErrorHandler;

@WebServlet("/VisitStatServlet")
public class VisitStatServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger();
    @Inject
    private EventService eventService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        StringBuilder chartRowBuilder = new StringBuilder();
        HttpSession httpSession = request.getSession();
        if (httpSession.getAttribute("username") == null) {
            try {
                response.sendRedirect("/event/faces/login.xhtml");
            } catch (IOException | IllegalStateException e) {
                HttpErrorHandler.print500(response, e, "Probleme redirection");
                return;
            }
        }
        String sessionUsername = httpSession.getAttribute("username").toString();
        if (sessionUsername.compareTo("admin") != 0) {
            HttpErrorHandler.print401(response, "L'autheur n'a pas accès a cette page");
            return;
        }
        try {
            List<Stats> statsList = eventService.nbrVisitByURL();
            List<Stats> statsGroupedList = new ArrayList<>();
            List<String> statsNamesList = new ArrayList<>();
            List<Stats> statsSortedList = new ArrayList<>();
            for (Stats stats : statsList) {
                Stats statsGrouped = new Stats();
                if (statsNamesList.contains(stats.getWebpageName())){
                    int indexOfWebpageName = statsNamesList.indexOf(stats.getWebpageName());
                    statsGrouped = statsGroupedList.get(indexOfWebpageName);
                    statsGrouped.setCount(stats.getCount()+statsGrouped.getCount());
                    statsGroupedList.remove(indexOfWebpageName);
                    statsNamesList.remove(indexOfWebpageName);
                } else {
                    statsGrouped = stats;
                }
                statsGroupedList.add(statsGrouped);
                statsNamesList.add(statsGrouped.getWebpageName());
            }
            statsSortedList = statsGroupedList;
            Collections.sort(statsSortedList);
            for (Iterator iter = statsSortedList.iterator(); iter.hasNext();) {
                Stats stats   = (Stats) iter.next();
                System.out.println("name = " + stats.getWebpageName() + " count = " + stats.getCount());
            } 

            for (Stats stats : statsSortedList) {
                chartRowBuilder.append(", ['" + stats.getWebpageName() + "', " + stats.getCount() + "]");
            }
            String chartRow = "['', 0]" + chartRowBuilder.toString();
            request.setAttribute("statsList", statsSortedList);
            request.setAttribute("chartRow", chartRow);
            request.getRequestDispatcher("/WEB-INF/visitStat.jsp")
                   .forward(request, response);

        } catch (EventServiceException e) {
            HttpErrorHandler.print500(response, e);
            return;
        } catch (ServletException | IOException | IllegalStateException e) {
            HttpErrorHandler.print500(response, e, "forward impossible");
            return;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            eventService.deleteAllVisits();
            response.sendRedirect("VisitStatServlet");
        } catch (Exception e) {
            LOGGER.info("probleme avec la redirection dans le doPost de VisitStatServlet", e);
        }
    }
}
