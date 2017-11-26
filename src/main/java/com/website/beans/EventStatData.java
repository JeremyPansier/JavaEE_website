package com.website.beans;

public class EventStatData {
	private String title;
	private Long nbIsPresent;
	private Long nbIsNotPresent;
	private Long nbNotConfirmed;
	private Long nbIsRead;
	private Long nbIsNotRead;
	private Long nbIsPresentAfterReading;
	private Long nbIsAbsentAfterReading;
	private Long nbIsNotConfirmedAfterReading;
	private Long idEvent;
	private int nbTotalInvitation;

	public EventStatData() {
	    super();
	}
	
	public void setNbIsNotPresent(Long nbIsNotPresent) {
		this.nbIsNotPresent = nbIsNotPresent;
		nbNotConfirmed = nbTotalInvitation - nbIsNotPresent - nbIsPresent;
	}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getNbIsPresent() {
        return nbIsPresent;
    }

    public void setNbIsPresent(Long nbIsPresent) {
        this.nbIsPresent = nbIsPresent;
    }

    public Long getNbNotConfirmed() {
        return nbNotConfirmed;
    }

    public void setNbNotConfirmed(Long nbNotConfirmed) {
        this.nbNotConfirmed = nbNotConfirmed;
    }

    public Long getNbIsRead() {
        return nbIsRead;
    }

    public void setNbIsRead(Long nbIsRead) {
        this.nbIsRead = nbIsRead;
        nbIsNotRead = nbTotalInvitation - nbIsRead;
    }

    public Long getNbIsNotRead() {
        return nbIsNotRead;
    }

    public void setNbIsNotRead(Long nbIsNotRead) {
        this.nbIsNotRead = nbIsNotRead;
    }

    public Long getNbIsPresentAfterReading() {
        return nbIsPresentAfterReading;
    }

    public void setNbIsPresentAfterReading(Long nbIsPresentAfterReading) {
        this.nbIsPresentAfterReading = nbIsPresentAfterReading;
    }

    public Long getNbIsAbsentAfterReading() {
        return nbIsAbsentAfterReading;
    }

    public void setNbIsAbsentAfterReading(Long nbIsAbsentAfterReading) {
        this.nbIsAbsentAfterReading = nbIsAbsentAfterReading;
        nbIsNotConfirmedAfterReading = nbIsRead - nbIsPresentAfterReading - nbIsAbsentAfterReading;
    }
    
    public Long getNbIsNotConfirmedAfterReading() {
        return nbIsNotConfirmedAfterReading;
    }

    public void setNbIsNotConfirmedAfterReading(Long nbIsNotConfirmedAfterReading) {
        this.nbIsNotConfirmedAfterReading = nbIsNotConfirmedAfterReading;
    }

    public Long getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(Long idEvent) {
        this.idEvent = idEvent;
    }

    public int getNbTotalInvitation() {
        return nbTotalInvitation;
    }

    public void setNbTotalInvitation(int i) {
        this.nbTotalInvitation = i;
    }

    public Long getNbIsNotPresent() {
        return nbIsNotPresent;
    }

}
