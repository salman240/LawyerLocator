package com.example.salmanyousaf.lawyerlocator;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.salmanyousaf.lawyerlocator.Helper.Utils;

import java.util.Objects;

import butterknife.BindAnim;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.paperdb.Paper;

public class SplashScreen extends AppCompatActivity {

    @BindView(R.id.textViewLogo)
    TextView textViewLogo;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindAnim(R.anim.bounce_animation)
    Animation bounceAnimation;

    private Unbinder unbinder;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        unbinder = ButterKnife.bind(this);
        Paper.init(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //show splash only the first time
        if(Paper.book().read("isFirstTime"))
        {
            Paper.book().write("isFirstTime", false);

            textViewLogo.setAnimation(bounceAnimation);
            final int DELAY = 2000;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSplash();
                }
            }, DELAY);
        }
        else
        {
            onSplash();
        }
    }

    public void onSplash()
    {
        if(progressBar != null)
            progressBar.setAlpha(0);

        final Boolean rememberMe = Paper.book().read("rememberMe");
        if (rememberMe != null) {
            if (rememberMe) {
                Intent intent = new Intent(SplashScreen.this, DataActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() { }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

}//class ends.
