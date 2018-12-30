package com.example.salmanyousaf.lawyerlocator.Model.Firebase;

public class Rating {

    private float rating;
    private String email;

    public Rating(){ }

    public Rating(float rating, String email) {
        this.rating = rating;
        this.email = email;
    }

    public float getRating() {
        return rating;
    }

    public String getEmail() {
        return email;
    }
}//class ends.