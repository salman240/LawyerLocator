package com.example.salmanyousaf.lawyerlocator;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyProfile extends AppCompatActivity {

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.tvEmail)
    TextView email;

    @BindView(R.id.tvName)
    TextView name;

    @BindView(R.id.tvContact)
    TextView phone;

    @BindView(R.id.tvAccount)
    TextView accountType;

    @BindView(R.id.tvLocation)
    TextView location;

    private Unbinder unbinder;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        unbinder = ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.myProfile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
