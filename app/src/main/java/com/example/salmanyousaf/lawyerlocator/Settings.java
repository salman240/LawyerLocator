package com.example.salmanyousaf.lawyerlocator;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Objects;

import static android.support.v7.app.AppCompatDelegate.setDefaultNightMode;
import static com.example.salmanyousaf.lawyerlocator.Contracts.Contracts.IS_NIGHT;
import static com.example.salmanyousaf.lawyerlocator.Contracts.Contracts.NIGHT__PREFERENCE;

public class Settings extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Settings");

        final Switch switchSetting = findViewById(R.id.switchSettings);

        //if(GetNightMode()!= null)
        /*switchSetting.setChecked(GetNightMode());

        switchSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switchSetting.isChecked())
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    SaveNightMode(true);
                    Toast.makeText(Settings.this, "Activating Night Theme", Toast.LENGTH_SHORT).show();
                    ExitActivityWithDelay();
                }
                else
                {
                    setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    SaveNightMode(false);
                    Toast.makeText(Settings.this, "Deactivating Night Theme", Toast.LENGTH_SHORT).show();
                    ExitActivityWithDelay();
                }
            }
        });
    }


    private void SaveNightMode(Boolean mode)
    {
        sharedPreferences = getSharedPreferences(NIGHT__PREFERENCE, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_NIGHT, mode);
        editor.apply();
    }


    private Boolean GetNightMode()
    {
        sharedPreferences = getSharedPreferences(NIGHT__PREFERENCE, MODE_PRIVATE);

        return sharedPreferences.getBoolean(IS_NIGHT, false);
    }


    private void ExitActivityWithDelay()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },1000);
    */
    }

}//class ends here
