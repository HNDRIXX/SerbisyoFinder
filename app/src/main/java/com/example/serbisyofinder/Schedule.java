package com.example.serbisyofinder;

public class Schedule {
    public String scheduleID, accID, clientID, mainService, clientPhoneNum, providerPhoneNum, subService, clientName, providerName, clientLatitude, clientLongitude, price, status, rating, review, providerSchedule, providerMultiSchedule, clientAddress, flag;

    public Schedule () {

    }

    public Schedule (String scheduleID, String accID, String clientID, String mainService, String clientPhoneNum, String providerPhoneNum, String subService, String clientLatitude, String clientLongitude,String clientName, String rating, String review, String providerName, String price, String status, String providerSchedule, String providerMultiSchedule, String clientAddress, String flag) {
        this.scheduleID = scheduleID;
        this.accID = accID;
        this.clientID = clientID;
        this.mainService = mainService;
        this.subService = subService;
        this.clientName = clientName;
        this.providerName = providerName;
        this.clientLatitude = clientLatitude;
        this.clientLongitude = clientLongitude;
        this.clientPhoneNum = clientPhoneNum;
        this.providerPhoneNum = providerPhoneNum;
        this.price = price;
        this.rating = rating;
        this.review = review;
        this.status = status;
        this.providerSchedule = providerSchedule;
        this.providerMultiSchedule = providerMultiSchedule;
        this.clientAddress = clientAddress;
        this.flag = flag;
    }

    public String getScheduleID() { return  scheduleID; }

    public String getAccID() { return  accID; }

    public String getClientID() { return clientID; }

    public String getMainService() { return mainService; }

    public String getSubService() { return subService; }

    public String getClientName() { return clientName; }

    public String getProviderPhoneNum() { return providerPhoneNum; }

    public String getClientPhoneNum() { return clientPhoneNum; }

    public String getProviderName() { return providerName; }

    public String getClientLatitude() { return clientLatitude; }

    public String getClientLongitude() { return clientLongitude; }

    public String getPrice() { return price; }

    public String getRating() { return rating; }

    public String getReview() { return review; }

    public String getStatus() { return status; }

    public String getProviderSchedule() { return providerSchedule;}

    public String getProviderMultiSchedule() { return providerMultiSchedule;}

    public String getClientAddress() { return clientAddress; }

    public String getFlag() { return flag; }
}
