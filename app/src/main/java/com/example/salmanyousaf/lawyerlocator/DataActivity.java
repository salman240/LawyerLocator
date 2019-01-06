package com.example.salmanyousaf.lawyerlocator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.salmanyousaf.lawyerlocator.Helper.Utils;
import com.example.salmanyousaf.lawyerlocator.Model.Firebase.Signup;
import com.example.salmanyousaf.lawyerlocator.Utilities.FixAppBarLayoutBehavior;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.paperdb.Paper;

import static com.example.salmanyousaf.lawyerlocator.Helper.Utils.encodeEmail;

public class DataActivity extends AppCompatActivity{

    private TabLayout tabLayout;
    private Utils utils = new Utils(this);

    private DatabaseReference databaseReference;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        //AppBar hotfix
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).setBehavior(new FixAppBarLayoutBehavior());

        //Firebase init
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setOffscreenPageLimit(2);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

        if (!utils.isConnectedToInternet()) { //checking if user is already signed in
            Snackbar.make(findViewById(android.R.id.content), "You are offline", Snackbar.LENGTH_LONG).show();
        }

        //setting status to active
        if(Paper.book().read("email") != null)
        {
            databaseReference.child(encodeEmail(Paper.book().read("email").toString())).child("status").setValue("true");
            //Todo Add listener
        }
        else
        {
            Log.w(DataActivity.class.getName(), "Email not set");
        }
    }


    @Override
    protected void onDestroy() {
        //setting status to not active
        if(Paper.book().read("email") != null)
        {
            databaseReference.child(encodeEmail(Paper.book().read("email").toString())).child("status").setValue("false");
            //Todo Add listener
        }
        else
        {
            Log.w(DataActivity.class.getName(), "Email not set");
        }

        super.onDestroy();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.setting)
        {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        }
        else if(id == R.id.logout)
        {
            final AlertDialog alertDialog = new AlertDialog.Builder(DataActivity.this).create();

            // Setting Dialog Title
            alertDialog.setTitle("Logout");

            // Setting Dialog Chat
            alertDialog.setMessage("Are you sure you want to logout?");

            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.ic_exit_to_app_green_24dp);


            // Setting Buttons + and -
            alertDialog.setButton(-1, "Logout", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    databaseReference.child(encodeEmail(Paper.book().read("email").toString())).child("status").setValue("false");

                    Paper.book().destroy();
                    Paper.book().write("isFirstTime", false);

                    Intent intent = new Intent(DataActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            alertDialog.setButton(-2, "Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });

            // Showing Alert Chat
            alertDialog.show();
        }
        else if(id == R.id.ProfileInfo)
        {
            Signup profile = Paper.book().read("user");

            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Your Profile")
                    .setContentText("Email : " + profile.getEmail() +
                                    "\n\nName : " + profile.getName() +
                                    "\n\nAccount Type : " + profile.getAccountType() +
                                    "\n\nGender : " + profile.getGender() +
                                    "\n\nLocation : " + profile.getLocation() +
                                    "\n\nPhone : " + profile.getPhone())
                    .setConfirmText("OK")
                    .show();
//            Intent intent = new Intent(DataActivity.this, MyProfile.class);
//            startActivity(intent);
        }
        else if(id == R.id.aboutUs)
        {
            utils.About();
        }

        return super.onOptionsItemSelected(item);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        FragmentPeople fragmentPeople = new FragmentPeople();

        adapter.addFragment(fragmentPeople, "PEOPLE");
        adapter.addFragment(new FragmentChat(), "CHAT");
        adapter.addFragment(new FragmentReserve(), "RESERVE");
        viewPager.setAdapter(adapter);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setupTabIcons() {
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_people_white_24dp);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.ic_chat_white_24dp);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.ic_storage_white_24dp);
    }


    public void onBackPressed() {
//        utils.Quit();
        super.onBackPressed();
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}