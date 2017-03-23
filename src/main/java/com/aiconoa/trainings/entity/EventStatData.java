package com.aiconoa.trainings.entity;

public class EventStatData {
	private String title;
	private int nbIsPresent;
	private int nbIsNotPresent;
	private int nbNotConfirmed;
	private int nbIsRead;
	private int nbIsNotRead;
	private int nbIsPresentAfterReading;
	private int nbIsAbsentAfterReading;
	private int nbIsNotConfirmedAfterReading;
	private int idEvent;
	private int nbTotalInvitation;

	public EventStatData() {
	    super();
	}
	
	public int getNbTotalInvitation() {
		return nbTotalInvitation;
	}
	
	public void setNbTotalInvitation(int nbTotalInvitation) {
		this.nbTotalInvitation = nbTotalInvitation;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getNbIsPresent() {
		return nbIsPresent;
	}

	public void setNbIsPresent(int nbIsPresent) {
		this.nbIsPresent = nbIsPresent;
	}

	public int getNbIsNotPresent() {
		return nbIsNotPresent;
	}

	public void setNbIsNotPresent(int nbIsNotPresent) {
		this.nbIsNotPresent = nbIsNotPresent;
		nbNotConfirmed = nbTotalInvitation - nbIsNotPresent - nbIsPresent;
	}

	public int getNbNotConfirmed() {
		return nbNotConfirmed;
	}

	public int getNbIsRead() {
		return nbIsRead;
	}

	public void setNbIsRead(int nbIsRead) {
		this.nbIsRead = nbIsRead;
		nbIsNotRead = nbTotalInvitation - nbIsRead;
	}

	public int getNbIsNotRead() {
		return nbIsNotRead;
	}

	public int getNbIsPresentAfterReading() {
		return nbIsPresentAfterReading;
	}

	public void setNbIsPresentAfterReading(int nbIsPresentAfterReading) {
		this.nbIsPresentAfterReading = nbIsPresentAfterReading;
	}

	public int getNbIsAbsentAfterReading() {
		return nbIsAbsentAfterReading;
	}

	public void setNbIsAbsentAfterReading(int nbIsAbsentAfterReading) {
		this.nbIsAbsentAfterReading = nbIsAbsentAfterReading;
		nbIsNotConfirmedAfterReading = nbIsRead - nbIsPresentAfterReading - nbIsAbsentAfterReading;
	}

	public int getNbIsNotConfirmedAfterReading() {
		return nbIsNotConfirmedAfterReading;
	}

	public void setNbIsNotConfirmedAfterReading(int nbIsNotConfirmedAfterReading) {
		this.nbIsNotConfirmedAfterReading = nbIsNotConfirmedAfterReading;
	}

	public int getIdEvent() {
		return idEvent;
	}

	public void setIdEvent(int idEvent) {
		this.idEvent = idEvent;
	}

}
