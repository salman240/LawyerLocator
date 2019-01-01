package com.example.salmanyousaf.lawyerlocator.Helper;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

public class HelperMethods {

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
                Log.e("Logs","Error parsing the weekDay");
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
                Log.e("Logs", "Error parsing the month");
                break;
        }
        return convertedMonth;
    }

}
