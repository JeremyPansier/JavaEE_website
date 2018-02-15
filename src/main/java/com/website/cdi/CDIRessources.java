package com.website.cdi;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.mail.Session;
import javax.sql.DataSource;

@ApplicationScoped
public class CDIRessources
{
	@Produces
	@Website
	@Resource(lookup = "java:jboss/DataSources/website")
	private DataSource website;

	@Produces
	@Resource(name = "java:jboss/mail/gmail")
	private Session session;
}
