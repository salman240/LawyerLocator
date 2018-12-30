package com.example.salmanyousaf.lawyerlocator.Model;


public class Chat
{
    //Data Model for Chat.


    private int mId;

    private String mSender;

    private String mReciever;

    private String mDatetime;

    public Chat(int id, String sender, String reciever, String datetime)
    {
        mId = id;
        mSender = sender;
        mReciever = reciever;
        mDatetime = datetime;
    }


    public int getChatId()
    {
        return mId;
    }

    public String getChatSender()
    {
        return mSender;
    }

    public String getChatReciever()
    {
        return mReciever;
    }

    public String  getChatDateTime()
    {
        return mDatetime;
    }

}//class ends
