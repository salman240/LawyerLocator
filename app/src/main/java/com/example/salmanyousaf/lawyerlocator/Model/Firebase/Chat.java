package com.example.salmanyousaf.lawyerlocator.Model.Firebase;

public class Chat {
    private String mSender;
    private String mReciever;
    private String mDatetime;

    public Chat(){}

    public Chat(String sender, String reciever, String datetime)
    {
        mSender = sender;
        mReciever = reciever;
        mDatetime = datetime;
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

}
