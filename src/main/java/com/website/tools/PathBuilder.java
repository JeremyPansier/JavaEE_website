package com.website.tools;

import com.website.enumeration.Constant;

public class PathBuilder {

    /**
     * 
     * @param link name of the webpage to reach
     * @return the path of the webpage to reach with "xhtml" extension
     */
    public static String getJsfPath(String link) {
        return "/" + Constant.faces + "/" + link + "." + Constant.xhtml;
    }

    /**
     * 
     * @param name name of the webpage to reach
     * @param parametername identifier of the http request parameter (i.e.: "id")
     * @param parameter http request parameter
     * @return the path of the webpage to reach with "xhtml" extension followed by the parmater
     */
    public static String getJsfPath(String name, String parametername, Long parameter) {
        return "/" + Constant.faces + "/" + name + "." + Constant.xhtml + "?" + parametername + "=" + parameter;
    }
    
    /**
     * 
     * @param name name of the webpage to reach
     * @param parametername identifier of the http request parameter (i.e.: "id")
     * @param parameter http request parameter
     * @return the path of the webpage to reach with "xhtml" extension followed by the parmater
     */
    public static String getJsfPath(String name, String parametername, String parameter) {
        return "/" + Constant.faces + "/" + name + "." + Constant.xhtml + "?" + parametername + "=" + parameter;
    }
}
