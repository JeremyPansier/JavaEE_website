package com.website.entities;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class Visit implements Comparable<Visit> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
	private String ip;
	@Column(insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@Transient
	private int count;
	@Transient
	private String resourceName;

	public Visit() {
		super();
	}

	public Visit(String url, int count) {
		super();
		this.url = url;
		this.count = count;
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	@PostLoad
	private void formatResourceName() {
		// compilation de la regex
		Pattern p = Pattern.compile("faces/([/a-zA-Z_0-9.-])+");
		// création d'un moteur de recherche
		Matcher m = p.matcher(url);
		// lancement de la recherche de toutes les occurrences successives
		while (m.find()) {
			// sous-chaine capturee
			this.resourceName = m.group().substring(6);
		}
	}

	@Override
	public int compareTo(Visit stats) // on redéfinit compareTo(Object)
	{
		Visit s = stats;
		int b;
		if (count == s.count) {
			return resourceName.compareTo(s.resourceName);
		}
		if (count >= s.count) {
			b = -1;
		} else {
			b = 1;
		}
		return b;
	}

}
