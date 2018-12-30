package com.example.salmanyousaf.lawyerlocator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.salmanyousaf.lawyerlocator.Helper.Utils;

import java.util.Objects;

public class AdminActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Objects.requireNonNull(getSupportActionBar()).hide();

        //setTitle("Admin Panel");

        WebView webView = findViewById(R.id.webWiew);

        webView.setWebViewClient(new WebViewClient());

        webView.getSettings().setJavaScriptEnabled(true);

        Utils utils = new Utils(this);

//        String ip = utils.getIp();

//        webView.loadUrl("http://" + ip + "/Admin/");

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //Toast.makeText(this, "Logging Out!", Toast.LENGTH_SHORT).show();
        finish();
        super.onBackPressed();
    }
}
