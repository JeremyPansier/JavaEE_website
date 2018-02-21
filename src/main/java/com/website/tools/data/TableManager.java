package com.website.tools.data;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.website.models.entities.Visit;

public class TableManager
{
	public static final String WEBPAGES = "webpages";
	private static final String XHTML = ".xhtml";

	/**
	 * 
	 * @param visits. if resourceName is null, the visit will not be counted
	 * @param listName
	 * @return visits grouped by count and by listName
	 */
	public static List<Visit> groupByName(final List<Visit> visits, final String listName)
	{
		final List<Visit> visitGroups = new ArrayList<>();
		final List<String> visitNames = new ArrayList<>();
		for (final Visit visit : visits)
		{
			final String resourceName = visit.getResourceName();
			if (resourceName != null)
			{
				String visitName = resourceName;
				visit.setCount(1);
				Visit visitGroup = new Visit();
				visitGroup.setCount(visit.getCount());
				final boolean isXhtml = isXhtml(resourceName);
				if (listName.compareTo(WEBPAGES) == 0 && isXhtml)
				{
					visitName = resourceName.substring(0, resourceName.indexOf(XHTML));
				}
				visitGroup.setResourceName(visitName);
				if ((listName.compareTo(WEBPAGES) == 0 && isXhtml)
						|| (listName.compareTo(WEBPAGES) != 0 && !isXhtml))
				{
					if (visitNames.contains(visitName))
					{
						final int index = visitNames.indexOf(visitName);
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

	private static boolean isXhtml(final String resourceName)
	{
		// Regular expression compilation
		final Pattern p = Pattern.compile("\\.([/a-zA-Z_0-9.-])+");
		// Search engine instantiation
		final Matcher m = p.matcher(resourceName);
		// Find the first pattern occurrence
		if (m.find())
		{
			// Compare the captured string to the wanted one
			final String extension = m.group();
			return extension.compareTo(XHTML) == 0;
		}
		return false;
	}

}
