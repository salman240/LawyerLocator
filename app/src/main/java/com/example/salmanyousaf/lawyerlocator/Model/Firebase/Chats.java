package com.example.salmanyousaf.lawyerlocator.Model.Firebase;


import com.example.salmanyousaf.lawyerlocator.Model.Firebase.Chat;

import java.util.List;

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
}//class ends
