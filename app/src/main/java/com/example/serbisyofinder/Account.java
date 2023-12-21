package com.example.serbisyofinder;

public class Account {
    public String accID, name, address, gender, phoneNum, email, password, role, occupation, mainService, earnings, starRating, countBookings;

    public Account() {

    }

    public Account(String accID, String name, String address, String gender, String phoneNum, String email, String password, String role, String occupation, String starRating, String  mainService, String earnings, String countBookings) {
        this.accID = accID;
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.email = email;
        this.password  = password;
        this.role = role;
        this.occupation = occupation;
        this.mainService = mainService;
        this.earnings = earnings;
        this.starRating = starRating;
        this.countBookings = countBookings;

    }

    public  String getAccID() { return  accID; }

    public String getName() { return name;}

    public String getRole() { return role;}

    public String getGender() { return gender;}

    public String getAddress() { return address;}

    public String getPhoneNum() { return phoneNum;}

    public String getOccupation() { return occupation;}

    public String getMainService() { return mainService;}

    public String getStarRating() { return starRating;}

    public String getEarnings() { return earnings;}

    public String getCountBookings() { return countBookings;}
}
