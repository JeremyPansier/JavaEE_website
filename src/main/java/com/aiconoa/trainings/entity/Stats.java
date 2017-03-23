package com.aiconoa.trainings.entity;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "visites")
public class Stats implements Comparable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String url;
    @Transient
    private int count;
    @Transient
    private String webpageName;
    private String ipAddress;
    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date visitDate;
    @Transient
    private static final String ORIGIN = "event/";

    public Stats(String url, int count) {
        super();
        this.url = url;
        this.count = count;
    }

    public Stats() {
        super();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getWebpageName() {
        return webpageName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAdress) {
        this.ipAddress = ipAdress;
    }

    @PostLoad
    private void setWebpageName() {
        // compilation de la regex
        Pattern p = Pattern.compile("event([/a-zA-Z])+");
        // création d'un moteur de recherche
        Matcher m = p.matcher(url);
        // lancement de la recherche de toutes les occurrences successives
        while (m.find()) {
            // sous-chaine capturee
            this.webpageName = m.group();
        }
    }

    public int compareTo(Object o) // on redéfinit compareTo(Object)
    {
      Stats s=(Stats)o;
      int b;
      if(count == s.count) {    
        return webpageName.compareTo(s.webpageName);
      }
      if(count >= s.count) {    
          b = -1;
      } else {
          b= 1;
      }
      return b;
    }
}
