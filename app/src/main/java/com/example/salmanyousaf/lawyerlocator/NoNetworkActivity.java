package com.example.salmanyousaf.lawyerlocator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;


import com.example.salmanyousaf.lawyerlocator.Helper.Utils;

import static com.example.salmanyousaf.lawyerlocator.Contracts.Contracts.PASSWORD;
import static com.example.salmanyousaf.lawyerlocator.Contracts.Contracts.SENDEREMAIL;

public class NoNetworkActivity extends AppCompatActivity {

    private NetworkInfo networkInfo;
    private SharedPreferences sharedPreferences;

    private Utils utils = new Utils(this);

    private RelativeLayout mView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_no_network);

        mView = (RelativeLayout) findViewById(R.id.noNetworkRoot);

        Button button = (Button) findViewById(R.id.buttonRetry);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                networkInfo = connectivityManager.getActiveNetworkInfo();

                if(networkInfo == null)
                {
                    //Toast.makeText(NoNetworkActivity.this, "No internet connection found!", Toast.LENGTH_SHORT).show();
                    Snackbar.make(mView, "No internet connection found!", Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    sharedPreferences = utils.GetLoginSharedPreferences();

                    String email = sharedPreferences.getString(SENDEREMAIL, null);
                    String password = sharedPreferences.getString(PASSWORD, null);

                    Intent i;

                    if(email == null && password == null)
                    {
                        i = new Intent(NoNetworkActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                    else
                    {
                        i = new Intent(NoNetworkActivity.this, DataActivity.class);
                        startActivity(i);
                    }

                    finish();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        utils.Quit();
        //super.onBackPressed();
    }


}//class ends.
