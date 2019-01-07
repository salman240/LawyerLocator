package com.example.salmanyousaf.lawyerlocator;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import net.danlew.android.joda.JodaTimeAndroid;

import io.paperdb.Paper;

import static com.example.salmanyousaf.lawyerlocator.Contracts.Contracts.IS_NIGHT;
import static com.example.salmanyousaf.lawyerlocator.Contracts.Contracts.NIGHT__PREFERENCE;


public class Startup extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);

        //allow firebase to store data locally so that it will be avl offline also
        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //initializing joda time external library
        JodaTimeAndroid.init(this);

        //Storing isFirstTime to show splash screen for first time
        if(Paper.book().read("isFirstTime") == null) {
            Paper.book().write("isFirstTime", true);
        }

/*
        //initializing leak canary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        // Normal app init code...
        LeakCanary.install(this);
*/

        //Checking the preference for theme settings
        if(GetNightMode())
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

    }


    private Boolean GetNightMode()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(NIGHT__PREFERENCE, MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_NIGHT, false);
    }

}
