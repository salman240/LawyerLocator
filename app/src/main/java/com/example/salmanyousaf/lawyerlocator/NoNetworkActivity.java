package com.example.salmanyousaf.lawyerlocator;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.salmanyousaf.lawyerlocator.Helper.Utils;

public class NoNetworkActivity extends AppCompatActivity {

    private NetworkInfo networkInfo;

    private Utils utils = new Utils(this);

    private RelativeLayout mView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_no_network);

        mView = findViewById(R.id.noNetworkRoot);

        Button button = findViewById(R.id.buttonRetry);
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

            }
        });
    }


    @Override
    public void onBackPressed() {
        utils.Quit();
        //super.onBackPressed();
    }


}//class ends.
