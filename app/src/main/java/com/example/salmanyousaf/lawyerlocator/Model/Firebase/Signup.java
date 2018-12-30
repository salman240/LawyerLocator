package com.example.salmanyousaf.lawyerlocator.Model.Firebase;

import java.io.Serializable;

public class Signup implements Serializable {
    //private global fields/variables
    private String email;
    private String password;
    private String name;
    private String caseType;
    private String fee;
    private String description;
    private String gender;
    private String status;
    private String accountType;
    private String profileImage;
    private String location;
    private String phone;
    private String dateTime;
    private String experience;

    public Signup()
    {

    }

    public Signup(String password, String name, String phone, String caseType, String fee, String description, String experience,
                  String location, String gender, String profileImage, String status, String accountType, String dateTime)
    {
        this.password = password;
        this.name = name;
        this.caseType = caseType;
        this.fee = fee;
        this.description = description;
        this.gender = gender;
        this.status = status;
        this.accountType = accountType;
        this.profileImage = profileImage;
        this.location = location;
        this.phone = phone;
        this.dateTime = dateTime;
        this.experience = experience;
    }


    //Getters
    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getCaseType() {
        return caseType;
    }

    public String getFee() {
        return fee;
    }

    public String getDescription() {
        return description;
    }

    public String getGender() {
        return gender;
    }

    public String getStatus() {
        return status;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getLocation() {
        return location;
    }

    public String getPhone() {
        return phone;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExperience() {
        return experience;
    }

}//class
