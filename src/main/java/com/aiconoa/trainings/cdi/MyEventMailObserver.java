package com.aiconoa.trainings.cdi;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.aiconoa.trainings.services.EventServiceException;

import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

@ApplicationScoped
public class MyEventMailObserver {
    private static final Logger LOGGER = Logger.getLogger(MyEventMailObserver.class.getName());
    @Resource(lookup="java:jboss/mail/Gmail")
    private Session session;

    public void listenToMyEvent(@Observes MyEventMail myEventMail) {
        LOGGER.info(String.format("received event %s", myEventMail));

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("websitejeremy@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(myEventMail.getEmail()));
            message.setSubject("Invitation");

            // Freemarker configuration object
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);

            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest origRequest = (HttpServletRequest)ec.getRequest();
            
            String URL= origRequest.getRequestURL().toString();
            String originWebSite=URL.substring(0, URL.lastIndexOf('/'));
            
            String imageLink = "<img src=\"" + originWebSite + "/MailReadServlet?msg_id="
                    + myEventMail.getHash()
                    + "\"\" alt=\"\" width=\"\"0\"\" height=\"\"0\"\"style=\"\"width: 0px; height: 0px; border:0px;\"\" />";
            String link = originWebSite + "/eventConfirm.xhtml?token=" + myEventMail.getHash();
            
            
            //String fullPath = myEventMail.getRequest().getServletContext().getRealPath("/WEB-INF");
            
            //InputStream inputStream = ec.getResourceAsStream("/WEB-INF");
            
          
            // URL url=MyEventMail.class.getResource("/");//.getPath();
            
            WebappTemplateLoader templateLoader = new WebappTemplateLoader((ServletContext) ec.getContext(), "WEB-INF/templates");

            templateLoader.setURLConnectionUsesCaches(false);
            templateLoader.setAttemptFileAccess(false);
            cfg.setTemplateLoader(templateLoader);
             
           // cfg.setDirectoryForTemplateLoading(new File(fullPath));
            cfg.setDefaultEncoding("UTF-8");

            // Build the data-model
            Map<String, Object> root = new HashMap<>();

            // Put datas into the root
            root.put("link", link);
            root.put("imageLink", imageLink);
            root.put("title", myEventMail.getTitle());

            Template temp = cfg.getTemplate("emailTemplate.ftl");
            Writer sw = new StringWriter();
            temp.process(root, sw);
            message.setContent(sw.toString(), "text/html; charset=utf-8");
            Transport.send(message);

        } catch (Exception e) {
            LOGGER.info("Issue with Freemarker template location within the class SendMailBySite");
            throw new EventServiceException("Error - Mail not send", e);
        }
    }
}