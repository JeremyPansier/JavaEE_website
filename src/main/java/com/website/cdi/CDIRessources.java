package com.website.cdi;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.mail.Session;
import javax.sql.DataSource;

@ApplicationScoped
public class CDIRessources {
    @Produces
    @Eventsdb
    @Resource(lookup = "java:jboss/DataSources/eventsdb")
    private DataSource eventsdb;

    @Produces
    @Resource(name = "java:jboss/mail/Gmail")
    private Session session;

}
