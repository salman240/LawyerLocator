package com.example.salmanyousaf.lawyerlocator.Model;


public class Login {
    //private global fields/variables
    private String email;
    private String password;
    private String name;
    private boolean status;
    private String accountType;
    private String profileImage;
    private String location;
    private String phone;


    public Login(String email, String password, String name, Boolean status, String accountType, String profileImage, String location, String phone)
    {
        this.email = email;
        this.password = password;
        this.name = name;
        this.status = status;
        this.accountType = accountType;
        this.profileImage = profileImage;
        this.location = location;
        this.phone = phone;
    }



    //Getters
    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }

    public String getName()
    {
        return name;
    }

    public Boolean getStatus()
    {
        return status;
    }

    public String getAccountType()
    {
        return accountType;
    }

    public String getProfileImage()
    {
        return profileImage;
    }

    public String getLocation()
    {
        return location;
    }

    public String getPhone()
    {
        return phone;
    }
}
