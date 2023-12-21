package com.example.serbisyofinder;

import java.io.Serializable;

public class ServiceProvider implements Serializable{

    String accID, name, role, occupation, mainService, starRating;

    public ServiceProvider(){
        //empty method is required
    }

    public ServiceProvider (String accID, String name, String role, String occupation, String mainService, String starRating) {
        this.accID = accID;
        this.name = name;
        this.role = role;
        this.occupation = occupation;
        this.mainService = mainService;
        this.starRating = starRating;
    }

    public  String getAccID() { return  accID; }

    public String getName() { return name;}

    public String getRole() { return role;}

    public String getOccupation() { return occupation;}

    public String getMainService() { return mainService;}

    public String getStarRating() { return starRating;}
}
