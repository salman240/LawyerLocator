package com.example.salmanyousaf.lawyerlocator.Model;


public class Reserve
{
    //Data Model for Reserve.
    private int mId;

    private String mSender;

    private String mReciever;

    private String mDatetime;

    private Boolean isReserve;

    public Reserve(int id, String sender, String reciever, String datetime, Boolean isreserve)
    {
        mId = id;
        mSender = sender;
        mReciever = reciever;
        mDatetime = datetime;
        isReserve = isreserve;
    }


    public int getResId()
    {
        return mId;
    }

    public String getResSender()
    {
        return mSender;
    }

    public String getResReciever()
    {
        return mReciever;
    }

    public String  getResDateTime()
    {
        return mDatetime;
    }

    public Boolean getIsReserve()
    {
        return isReserve;
    }

}//class ends
