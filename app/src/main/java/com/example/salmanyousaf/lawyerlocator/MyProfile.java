package com.example.salmanyousaf.lawyerlocator;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.salmanyousaf.lawyerlocator.Helper.Base64ToBitmap;
import com.example.salmanyousaf.lawyerlocator.Helper.Utils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.salmanyousaf.lawyerlocator.Contracts.Contracts.ACCOUNTTYPE;
import static com.example.salmanyousaf.lawyerlocator.Contracts.Contracts.LOCATION;
import static com.example.salmanyousaf.lawyerlocator.Contracts.Contracts.PHONE;
import static com.example.salmanyousaf.lawyerlocator.Contracts.Contracts.PROFILEIMAGE;
import static com.example.salmanyousaf.lawyerlocator.Contracts.Contracts.SENDEREMAIL;
import static com.example.salmanyousaf.lawyerlocator.Contracts.Contracts.USERNAME;

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

        Utils utils = new Utils(this);
        SharedPreferences sharedPreferences = utils.GetLoginSharedPreferences();

        email.setText(sharedPreferences.getString(SENDEREMAIL, null));
        name.setText(sharedPreferences.getString(USERNAME, null));
        phone.setText(sharedPreferences.getString(PHONE, null));
        accountType.setText(Objects.requireNonNull(sharedPreferences.getString(ACCOUNTTYPE, null)).toUpperCase());
        location.setText(sharedPreferences.getString(LOCATION, null));

        Base64ToBitmap base64ToBitmap = new Base64ToBitmap();
        Bitmap bitmap = base64ToBitmap.ConvertBase64ToBitmap(Objects.requireNonNull(sharedPreferences.getString(PROFILEIMAGE, null)));
        imageView.setImageBitmap(bitmap);

    }


    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
