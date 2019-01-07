package com.example.salmanyousaf.lawyerlocator.Model.Firebase;


import android.support.annotation.Keep;

import java.util.List;

@Keep
public class Chats
{
    private String chatSender;
    private String chatReciever;
    private List<Chat> mChats;
    private String mDate;

    public Chats(){}

    public Chats(String sender, String reciever, String date, List<Chat> chats)
    {
        chatSender = sender;
        chatReciever = reciever;
        mDate = date;
        mChats = chats;
    }

    public String getChatSender()
    {
        return chatSender;
    }

    public String getChatReciever()
    {
        return chatReciever;
    }

    public List<Chat> getmChats() {
        return mChats;
    }

    public String getmDate() {
        return mDate;
    }

    public void setChatSender(String chatSender) {
        this.chatSender = chatSender;
    }

    public void setChatReciever(String chatReciever) {
        this.chatReciever = chatReciever;
    }

    public void setmChats(List<Chat> mChats) {
        this.mChats = mChats;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
}//class ends
