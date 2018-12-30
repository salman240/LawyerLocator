package com.example.salmanyousaf.lawyerlocator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.RelativeLayout;

import com.example.salmanyousaf.lawyerlocator.Helper.Utils;
import com.example.salmanyousaf.lawyerlocator.Model.Firebase.Signup;
import com.example.salmanyousaf.lawyerlocator.Utilities.FixAppBarLayoutBehavior;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.paperdb.Paper;

public class DataActivity extends AppCompatActivity{

    private TabLayout tabLayout;
    private Utils utils = new Utils(this);

    private String senderEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        Paper.init(this);

        //AppBar hotfix
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).setBehavior(new FixAppBarLayoutBehavior());

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

//        senderEmail = utils.setStatus();
        assert senderEmail != null;
        if(senderEmail.equals(""))
        {
            Log.e(DataActivity.class.getName(), "Please implement functionality to update status, offline/online!");
        }
        else
        {
//           setStatus("Online");
        }
    }


    @Override
    protected void onDestroy() {
        senderEmail = utils.setStatus();
        if(senderEmail != null && senderEmail.equals(""))
        {
            Log.e(DataActivity.class.getName(), "Sender Name not Found!");
        }
        else
        {
//            setStatus("Offline");
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

            // Setting Dialog Message
            alertDialog.setMessage("Are you sure you want to logout?");

            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.ic_exit_to_app_black_24dp);


            // Setting Buttons + and -
            alertDialog.setButton(-1, "Logout", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //clearing data in Login Shared pref and go to Login Activity
//                    utils.ClearPreference(lOGIN_PREFERENCE);
//                    if(senderEmail == null)
//                    {
//                        Log.e(DataActivity.class.getName(), "Sender Name not Found!");
//                    }
//                    else
//                    {
////                        setStatus("Offline");
//                    }

                    Paper.book().destroy();

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

            // Showing Alert Message
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


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_people_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_chat_white_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_storage_white_24dp);
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