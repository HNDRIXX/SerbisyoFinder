package com.example.serbisyofinder;

public class Messages  {
    public String scheduleID, providerID, clientID, message, senderName;

    public Messages() {

    }

    public Messages(String scheduleID,  String providerID, String clientID, String message, String senderName) {
        this.scheduleID = scheduleID;
        this.providerID = providerID;
        this.clientID = clientID;
        this.message = message;
        this.senderName = senderName;
    }

    public String getScheduleID() { return  scheduleID; }

    public String getProviderID() { return  providerID; }

    public String getClientID() { return clientID;}

    public String getMessage() { return message;}

    public String getSenderName() { return senderName;}
}
