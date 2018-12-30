package com.example.salmanyousaf.lawyerlocator.Helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.salmanyousaf.lawyerlocator.R;
import com.example.salmanyousaf.lawyerlocator.SignupActivity;

import org.joda.time.DateTime;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;
import static com.example.salmanyousaf.lawyerlocator.Contracts.Contracts.SENDEREMAIL;
import static com.example.salmanyousaf.lawyerlocator.Contracts.Contracts.lOGIN_PREFERENCE;

public class Utils {

    private Context mContext;
    private SharedPreferences sharedPreferences;

    public Utils(Context context)
    {
        mContext = context;
    }

    //It will show the Alert Dilaog to exit an Activity
    public void Quit() {
        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();

        // Setting Dialog Title
        alertDialog.setTitle("Quit");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to quit?");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_exit_to_app_black_24dp);


        // Setting Buttons + and -
        alertDialog.setButton(-1,"Quit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ((Activity)mContext).finish();
            }
        });

        alertDialog.setButton(-2,"Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    //It is used to notify user when user has touched editTexts in Login or signUp  Activity and then try to exit.
    public void ChangesUnsaved() {
        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();

        // Setting Dialog Title
        alertDialog.setTitle("Discard");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to discard changes and Quit?");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_exit_to_app_black_24dp);


        // Setting Buttons + and -
        alertDialog.setButton(-1,"Discard and Quit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ((Activity)mContext).finish();
            }
        });

        alertDialog.setButton(-2,"Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    //It tells about the developers
    public void About() {
        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();

        // Setting Dialog Title
        alertDialog.setTitle("About Us");

        // Setting Dialog Message
        alertDialog.setMessage("Developed By:-\n\n\t\t\tSalman Yousaf,\n\t\t\tAsif Saeed\n\nCopyrights 2018");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_info_black_24dp);


        // Setting OK Button
        alertDialog.setButton(-1,"Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    //It shows a dialog box with 2 account tyes and used intent to go to them
    public void Register()
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();

        // Setting Dialog Title
        alertDialog.setTitle("Register");

        // Setting Dialog Message
        alertDialog.setMessage("Account Type:");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_people_black_24dp);


        // Setting Cancel Button
        alertDialog.setButton(-3,"Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });


        // Setting Client Button
        alertDialog.setButton(-2,"Client", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(mContext, SignupActivity.class);
                intent.putExtra("accountType", "client");
                mContext.startActivity(intent);
                ((Activity)mContext).finish();
            }
        });

        // Setting Lawyer Button
        alertDialog.setButton(-1,"Lawyer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(mContext, SignupActivity.class);
                intent.putExtra("accountType", "lawyer");
                mContext.startActivity(intent);
                ((Activity)mContext).finish();
            }
        });



        // Showing Alert Message
        alertDialog.show();
    }

    //It shows an Alert Dilaog to select image from gallery using Intent.
    //Select image from gallery
    @SuppressLint("IntentReset")
    public void SelectImage()
    {
//        final CharSequence[] items = {"Gallery", "Cancel"};
//
//        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
//        alertDialog.setTitle("Select Image");
//        alertDialog.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if(items[i].equals("Gallery"))
//                {
                    @SuppressLint("IntentReset")
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    Integer SELECT_FILE = 0;
                    ((Activity)mContext).startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE );
//                }
//                else if(items[i].equals("Cancel"))
//                {
//                    dialogInterface.dismiss();
//                }
//            }
//        });
//
//        alertDialog.show();
    }

    //New methods
    public boolean isConnectedToInternet()
    {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null;
    }

    //It gets the status string in Login SharedPreferences
    public String setStatus() {
        sharedPreferences = GetLoginSharedPreferences();

        return sharedPreferences.getString(SENDEREMAIL, null);
    }

    //It is used to get the values from Login SharedPreferences
    public SharedPreferences GetLoginSharedPreferences()
    {
        return sharedPreferences = mContext.getSharedPreferences(lOGIN_PREFERENCE, MODE_PRIVATE);
    }

    //It checks whether the email matches with custom defined format and then return status as true or false.
    //Email and password Format Checker
    public static boolean isEmailValid(String email) {
        String expression = "^[a-zA-Z0-9_.+-]+@(?:(?:[a-zA-Z0-9-]+\\.)?[a-zA-Z]+\\.)?(lawloc)\\.com$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //It checks whether password length is >= 8 and return true otherwise false.
    public static boolean isPasswordValid(String password) {
        return password.length() >= 8 && !password.contains(" ");
    }

    //It formats the Sql Server DateTime to custom formatted DateTime using JodaTime Library
    public String FormatDateTime(String dateTime)
    {
        DateTime time = DateTime.parse(dateTime);
        String weekDay = WeekDaysToString(time.getDayOfWeek());
        int day = time.getDayOfMonth();
        String month = MonthToString(time.getMonthOfYear());
        int year = time.getYear();
        int hour = time.getHourOfDay();
        int minute = time.getMinuteOfHour();
        //int second = datetime.getMillisOfSecond();

        //Checking if am or pm
        String timeFormatter;
        if(hour < 12)
        {
            timeFormatter = "AM";
        }
        else if(hour == 12)
        {
            timeFormatter = "PM";
        }
        else
        {
            timeFormatter = "PM";
            hour = hour - 12;
        }

        String hourPreZero = hour + "";
        String minutePreZero = minute + "";


        if(hour < 10) {
            hourPreZero = "0" + hour;
        }

        if(minute < 10) {
            minutePreZero = "0" + minute;
        }

        return hourPreZero + " : " + minutePreZero + " " + timeFormatter + " " + weekDay + ", " + day + " " +  month +  " " + year;
    }

    //WeekDays to String converter
    private String WeekDaysToString(int weekDay)
    {
        String convertedDay = null;

        switch (weekDay)
        {
            case 1:
                convertedDay = "Mon";
                break;
            case 2:
                convertedDay = "Tue";
                break;
            case 3:
                convertedDay = "Wed";
                break;
            case 4:
                convertedDay = "Thu";
                break;
            case 5:
                convertedDay = "Fri";
                break;
            case 6:
                convertedDay = "Sat";
                break;
            case 7:
                convertedDay = "Sun";
                break;
            default:
                Toast.makeText(mContext, "Error parsing the weekDay", Toast.LENGTH_SHORT).show();
                break;
        }
        return convertedDay;
    }

    //Month to String converter
    private String MonthToString(int monthOfYear)
    {
        String convertedMonth = null;

        switch (monthOfYear)
        {
            case 1:
                convertedMonth = "Jan";
                break;
            case 2:
                convertedMonth = "Feb";
                break;
            case 3:
                convertedMonth = "Mar";
                break;
            case 4:
                convertedMonth = "Apr";
                break;
            case 5:
                convertedMonth = "May";
                break;
            case 6:
                convertedMonth = "Jun";
                break;
            case 7:
                convertedMonth = "Jul";
                break;
            case 8:
                convertedMonth = "Aug";
                break;
            case 9:
                convertedMonth = "Sep";
                break;
            case 10:
                convertedMonth = "Oct";
                break;
            case 11:
                convertedMonth = "Nov";
                break;
            case 12:
                convertedMonth = "Dec";
                break;
            default:
                Toast.makeText(mContext, "Error parsing the month", Toast.LENGTH_SHORT).show();
                break;
        }
        return convertedMonth;
    }

    public static String encodeEmail(String string) {
        return string.replace(".", ",");
    }

    public static String decodeEmail(String string) {
        return string.replace(",", ".");
    }

    public static String getUsers(String accType)
    {
        String data = null;
        if(accType.equals("lawyer")) {

            data = "client";
        }
        else if(accType.equals("client"))
        {
            data = "lawyer";
        }

        return data;
    }
}
