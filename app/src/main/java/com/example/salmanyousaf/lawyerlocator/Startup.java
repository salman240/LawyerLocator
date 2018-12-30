package com.example.salmanyousaf.lawyerlocator;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

import net.danlew.android.joda.JodaTimeAndroid;

import io.paperdb.Paper;


public class Startup extends Application {


//    static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);

        //allow firebase to store data locally so that it will be avl offline also
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

//        Log.d(getClass().getName(), "--Starting Lawyer Locator\nCoded by Salman Yousaf\n--Initializing JodaTimeAndroid " +
//                "+ Canary Leak");

        //initializing joda time external library
        JodaTimeAndroid.init(this);

        //Storing isFirstTime to show splash screen for first time
        if(Paper.book().read("isFirstTime") == null) {
            Paper.book().write("isFirstTime", true);
        }

        //initializing leak canary
        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        // Normal app init code...
        LeakCanary.install(this);
        */

        //Checking the preference for theme settings
        /*if(GetNightMode())
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }*/


//        appComponent =  DaggerAppComponent.builder().appModule(new AppModule(this)).build();

    }



//    public static Application getApplication()
//    {
//        return appComponent.getApplication();
//    }





    /*private Boolean GetNightMode()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(NIGHT__PREFERENCE, MODE_PRIVATE);

        return sharedPreferences.getBoolean(IS_NIGHT, false);
    }*/

}
