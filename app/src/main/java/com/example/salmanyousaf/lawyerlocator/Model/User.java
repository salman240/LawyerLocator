package com.example.salmanyousaf.lawyerlocator.Model;


import java.io.Serializable;

/**
 * Created by SalmanGeForce on 3/7/2018.
 */

public class User implements Serializable
{
    //private global fields/variables

    private String email;
    private String name;
    private String location;
    private String contactNo;
    private int cost;
    private String dateTime;
    private String description;
    private double experience;
    private String caseType;
    private double rating;
    private String profileImage;

    //4 Constructors

    public User(String email, String name, String location, String contact, int cost, String dateTime, String description, double experience, double rating, String profileImage )
    {
        this.email = email;
        this.name = name;
        this.location = location;
        this.contactNo = contact;
        this.cost = cost;
        this.dateTime = dateTime;
        this.description = description;
        this.experience = experience;
        this.rating = rating;
        this.profileImage = profileImage;
    }   //lawyer


    //______________________________________________________________________________________________

    public User(String email, String name, String location, String contact, int cost, String dateTime, String description, String caseType, String profileImage )
    {
        this.email = email;
        this.name = name;
        this.location = location;
        contactNo = contact;
        this.cost = cost;
        this.dateTime = dateTime;
        this.description = description;
        this.caseType = caseType;
        this.profileImage = profileImage;
    }   //client


    //For ReserveAsyncTask
    public User(String email, String name, String location, String contact, int cost, String dateTime, String description, String caseType, double experience, double rating, String profileImage )
    {
        this.email = email;
        this.name = name;
        this.location = location;
        contactNo = contact;
        this.cost = cost;
        this.dateTime = dateTime;
        this.description = description;
        this.caseType = caseType;
        this.experience = experience;
        this.rating = rating;
        this.profileImage = profileImage;
    }



    //for search filter(Lawyer)
    public User(String name, String location, String caseType, String profileImage)
    {
        this.name = name;
        this.location = location;
        this.caseType = caseType;
        this.profileImage = profileImage;
    }

    //for search filter(Client)
    public User(String name, String location, double rating, String profileImage)
    {
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.profileImage = profileImage;
    }

    //for search filter(both)
    public User(String name, String location, String caseType, double rating, String profileImage)
    {
        this.name = name;
        this.location = location;
        this.caseType = caseType;
        this.rating = rating;
        this.profileImage = profileImage;
    }


    //Getters
    public String getEmail()
    {
        return email;
    }

    public String getName()
    {
        return name;
    }

    public String getLocation()
    {
        return location;
    }

    public String getContact()
    {
        return contactNo;
    }

    public int getCost()
    {
        return cost;
    }

    public String getDateTime()
    {
        return dateTime;
    }

    public String getDescription()
    {
        return description;
    }

    public double getExperience()
    {
        return experience;
    }

    public String getCaseType()
    {
        return caseType;
    }

    public double getRatingStars()
    {
        return rating;
    }

    public String getProfileImage()
    {
        return profileImage;
    }





}//class ends.
