package com.example.serbisyofinder;

public class Messages  {
    public String scheduleID, providerID, clientID, message, senderID, senderName, sentTime, sentDate;

    public Messages() {

    }

    public Messages(String scheduleID,  String providerID, String clientID, String message, String senderID, String senderName, String sentDate, String sentTime) {
        this.scheduleID = scheduleID;
        this.providerID = providerID;
        this.clientID = clientID;
        this.message = message;
        this.senderID = senderID;
        this.senderName = senderName;
        this.sentTime = sentTime;
        this.sentDate = sentDate;
    }

    public String getScheduleID() { return  scheduleID; }

    public String getProviderID() { return  providerID; }

    public String getClientID() { return clientID;}

    public String getMessage() { return message;}

    public String getSenderID() { return senderID;}

    public String getSenderName() { return senderName;}

    public String getSentTime () { return sentTime; }

    public String getSentDate () { return sentDate; }
}
