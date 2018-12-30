package com.example.salmanyousaf.lawyerlocator.Model;

import com.example.salmanyousaf.lawyerlocator.R;

import java.io.Serializable;

/**
 * Created by SalmanGeForce on 3/7/2018.
 */

public class UserData implements Serializable
{
    //Data Model for User.

    //private global fields/variables
    private String mEmail;
    private String mName;
    private String mLocation;
    private String mContactNo;
    private int mCost;
    private String mDateTime;
    private String mDescription;
    private double mExperience;
    private String mCaseType;
    private double mRating;
    private String mProfileImage;

    //4 Constructors

    public UserData(String email, String name, String location, String contact, int cost, String dateTime, String description, double experience, double rating, String profileImage )
    {
        mEmail = email;
        mName = name;
        mLocation = location;
        mContactNo = contact;
        mCost = cost;
        mDateTime = dateTime;
        mDescription = description;
        mExperience = experience;
        mRating = rating;
        mProfileImage = profileImage;
    }   //lawyer


    //______________________________________________________________________________________________

    public UserData(String email, String name, String location, String contact, int cost, String dateTime, String description, String caseType, String profileImage )
    {
        mEmail = email;
        mName = name;
        mLocation = location;
        mContactNo = contact;
        mCost = cost;
        mDateTime = dateTime;
        mDescription = description;
        mCaseType = caseType;
        mProfileImage = profileImage;
    }   //client


    //For ReserveAsyncTask
    public UserData(String email, String name, String location, String contact, int cost, String dateTime, String description, String caseType, double experience, double rating, String profileImage )
    {
        mEmail = email;
        mName = name;
        mLocation = location;
        mContactNo = contact;
        mCost = cost;
        mDateTime = dateTime;
        mDescription = description;
        mCaseType = caseType;
        mExperience = experience;
        mRating = rating;
        mProfileImage = profileImage;
    }



    //for search filter(Lawyer)
    public UserData(String name, String location, String caseType, String profileImage)
    {
        mName = name;
        mLocation = location;
        mCaseType = caseType;
        mProfileImage = profileImage;
    }

    //for search filter(Client)
    public UserData(String name, String location, double rating, String profileImage)
    {
        mName = name;
        mLocation = location;
        mRating = rating;
        mProfileImage = profileImage;
    }

    //for search filter(both)
    public UserData(String name, String location, String caseType, double rating, String profileImage)
    {
        mName = name;
        mLocation = location;
        mCaseType = caseType;
        mRating = rating;
        mProfileImage = profileImage;
    }


    //Getters
    public String getEmail()
    {
        return mEmail;
    }

    public String getName()
    {
        return mName;
    }

    public String getLocation()
    {
        return mLocation;
    }

    public String getContact()
    {
        return mContactNo;
    }

    public int getCost()
    {
        return mCost;
    }

    public String getDateTime()
    {
        return mDateTime;
    }

    public String getDescription()
    {
        return mDescription;
    }

    public double getExperience()
    {
        return mExperience;
    }

    public String getCaseType()
    {
        return mCaseType;
    }

    public double getRatingStars()
    {
        return mRating;
    }

    public String getProfileImage()
    {
        return mProfileImage;
    }





}//class ends.
