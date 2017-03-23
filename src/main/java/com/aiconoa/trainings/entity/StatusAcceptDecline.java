package com.aiconoa.trainings.entity;

public enum StatusAcceptDecline {
    ACCEPT(1, "Je viens"), DECLINE(2, "Je viens pas");

    private int Status;
    private String StatusInJSP;

    private StatusAcceptDecline(int status, String statusInJSP) {
        setStatus(status);
        setStatusInJSP(statusInJSP);
    }

    public String getStatusInJSP() {
        return StatusInJSP;
    }

    public void setStatusInJSP(String statusInJSP) {
        StatusInJSP = statusInJSP;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getStatus(String value) {
        return StatusAcceptDecline.valueOf(StatusInJSP).Status;
    }

}
