package com.example.serbisyofinder;

import java.io.Serializable;

//These java class fetch data from database
public class ServiceCategory implements Serializable{

    String serviceID, serviceName, serviceImg, subService1, subService2, subService3, subService4, subService5;

    public ServiceCategory(){
        //empty method is required
    }

    public ServiceCategory (String serviceID, String serviceName, String serviceImg, String subService1, String subService2, String subService3, String subService4, String subService5) {
        this.serviceID = serviceID;
        this.serviceImg = serviceImg;
        this.serviceName = serviceName;
        this.subService1 = subService1;
        this.subService2 = subService1;
        this.subService3 = subService3;
        this.subService4 = subService4;
        this.subService5 = subService5;
    }

    public  String getServiceID() { return  serviceID; }

    public String getServiceImg() { return serviceImg;}

    public String getServiceName() { return serviceName;}

    public String getSubService1() { return subService1;}

    public String getSubService2() { return subService2;}

    public String getSubService3() { return subService3;}

    public String getSubService4() { return subService4;}

    public String getSubService5() { return subService5;}



}
