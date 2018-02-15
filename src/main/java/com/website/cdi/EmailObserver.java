package com.website.cdi;

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

import com.website.models.beans.Email;
import com.website.tools.EventServiceException;
import com.website.views.WebPages;

import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

@ApplicationScoped
public class EmailObserver
{
	private static final Logger LOGGER = Logger.getLogger(EmailObserver.class.getName());
	@Resource(lookup = "java:jboss/mail/Gmail")
	private Session session;

	public void listenToMyEvent(@Observes final Email email)
	{
		try
		{
			final Message message = new MimeMessage(session);
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getAddress()));
			message.setSubject("Invitation");

			// Freemarker configuration object
			final Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);

			final ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			final HttpServletRequest origRequest = (HttpServletRequest) ec.getRequest();

			final String url = origRequest.getRequestURL().toString();
			final String originWebSite = url.substring(0, url.lastIndexOf("/faces/"));

			final String imageLink = "<img src=\"" + originWebSite + "/ReadEmailTackerServlet?msg_id="
					+ email.getHash()
					+ "\"\" alt=\"\" width=\"\"0\"\" height=\"\"0\"\"style=\"\"width: 0px; height: 0px; border:0px;\"\" />";
			final String link = originWebSite + WebPages.EVENT_SUBSCRIPTION.createJsfUrl("token", email.getHash());

			final WebappTemplateLoader templateLoader = new WebappTemplateLoader((ServletContext) ec.getContext(), "WEB-INF/templates");

			templateLoader.setURLConnectionUsesCaches(false);
			templateLoader.setAttemptFileAccess(false);
			cfg.setTemplateLoader(templateLoader);

			cfg.setDefaultEncoding("UTF-8");

			// Build the data-model
			final Map<String, Object> root = new HashMap<>();

			// Put data into the root
			root.put("link", link);
			root.put("imageLink", imageLink);
			root.put("title", email.getTitle());

			final Template temp = cfg.getTemplate("emailTemplate.ftl");
			final Writer sw = new StringWriter();
			temp.process(root, sw);
			message.setContent(sw.toString(), "text/html; charset=utf-8");
			Transport.send(message);

		}
		catch (final Exception e)
		{
			LOGGER.info("Issue with Freemarker template location within the class " + this.getClass().toString());
			throw new EventServiceException("Error - Email not send", e);
		}
	}
}