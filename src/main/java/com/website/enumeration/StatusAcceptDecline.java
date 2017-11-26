package com.website.enumeration;

public enum StatusAcceptDecline {
    ACCEPT(1), DECLINE(2);

    private int intStatus;

    private StatusAcceptDecline(int i) {
        setIntStatus(i);
    }

    public int getIntStatus() {
        return intStatus;
    }

    public void setIntStatus(int i) {
        intStatus = i;
    }
}
