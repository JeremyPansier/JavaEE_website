package com.aiconoa.trainings.services;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Stateless
public class SendMailBySite {
    private static final Logger LOGGER = LogManager.getLogger();
    private Session session;

    public SendMailBySite() {
        super();
    }

    @Inject
    public SendMailBySite(Session session) {
        this.session = session;
    }

    public void sendMail(String sendToUser, String title, String hashcodeString) {

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("pascaljeremym2i@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sendToUser));
            message.setSubject("Invitation");

            // Freemarker configuration object
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);

            String imageLink = "<img src=\"http://localhost:8080/http-jsp-servlet/MailReadServlet?msg_id="
                    + hashcodeString
                    + "\"\" alt=\"\" width=\"\"0\"\" height=\"\"0\"\"style=\"\"width: 0px; height: 0px; border:0px;\"\" />";
            String link = "http://localhost:8080/http-jsp-servlet/EventConfirmServlet?token=" + hashcodeString;
            String fullPath = this.getClass().getClassLoader().getResource("/WEB-INF").getPath();
            cfg.setDirectoryForTemplateLoading(new File(fullPath));
            cfg.setDefaultEncoding("UTF-8");

            // Build the data-model
            Map<String, Object> root = new HashMap<>();

            // Put datas into the root
            root.put("link", link);
            root.put("imageLink", imageLink);
            root.put("title", title);

            Template temp = cfg.getTemplate("emailTemplate.ftl");
            Writer sw = new StringWriter();
            temp.process(root, sw);
            message.setContent(sw.toString(), "text/html; charset=utf-8");
            Transport.send(message);

        } catch (Exception e) {
            LOGGER.info("Issue with Freemarker template location within the class SendMailBySite", e);
            throw new EventServiceException("Error - Mail not send", e);
        }
    }
}