package com.website.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.website.entities.Visit;

public class TableManager {
    public static final String WEBPAGES = "webpages";
    private static final String XHTML = ".xhtml";

    /**
     * 
     * @param visits. if resourceName is null, the visit will not be counted
     * @param listName
     * @return visits grouped by count and by listName
     */
    public static List<Visit> groupByName(List<Visit> visits, String listName) {
        List<Visit> visitGroups = new ArrayList<>();
        List<String> visitNames = new ArrayList<>();
        for (Visit visit : visits) {
            String resourceName = visit.getResourceName();
            if (resourceName != null) {
                String visitName = resourceName;
                visit.setCount(1);
                Visit visitGroup = new Visit(null, visit.getCount());
                if (listName.compareTo(WEBPAGES) == 0 && isXhtml(resourceName)) {
                    visitName = resourceName.substring(0, resourceName.indexOf(XHTML));
                }
                visitGroup.setResourceName(visitName);
                if ((listName.compareTo(WEBPAGES) == 0 && isXhtml(resourceName))
                        || (listName.compareTo(WEBPAGES) != 0 && !isXhtml(resourceName))) {
                    if (visitNames.contains(visitName)) {
                        int index = visitNames.indexOf(visitName);
                        visitGroup = visitGroups.get(index);
                        visitGroup.setCount(visitGroup.getCount() + 1);
                        visitGroups.remove(index);
                        visitNames.remove(index);
                    }
                    visitGroups.add(visitGroup);
                    visitNames.add(visitName);
                }
            }
        }
        return visitGroups;
    }

    private static boolean isXhtml(String resourceName) {
        String extension = null;
        // compilation de la regex
        Pattern p = Pattern.compile("\\.([/a-zA-Z_0-9.-])+");
        // création d'un moteur de recherche
        Matcher m = p.matcher(resourceName);
        // lancement de la recherche de toutes les occurrences successives
        while (m.find()) {
            // sous-chaine capturee
            extension = m.group();
            return extension.compareTo(XHTML) == 0;
        }
        return false;
    }

}
