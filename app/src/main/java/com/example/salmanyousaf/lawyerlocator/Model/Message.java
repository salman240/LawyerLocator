package com.example.salmanyousaf.lawyerlocator.Model;


public class Message
{
    //Data Model for Chat.
    private int mChatID;

    private String mSender;

    private String mReciever;

    private String mMessage;

    private String mDatetime;

    public Message(String sender, String reciever, String message, int id, String datetime)
    {
        mSender = sender;
        mReciever = reciever;
        mMessage = message;
        mChatID = id;
        mDatetime = datetime;

    }


    public String getMessageSender()
    {
        return mSender;
    }

    public String getMessageReciever()
    {
        return mReciever;
    }

    public String  getMessage()
    {
        return mMessage;
    }

    public int getChatId()
    {
        return mChatID;
    }

    public String  getMessageDateTime()
    {
        return mDatetime;
    }

}//class ends
