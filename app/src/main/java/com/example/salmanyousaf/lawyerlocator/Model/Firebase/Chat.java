package com.example.salmanyousaf.lawyerlocator.Model.Firebase;

public class Chat {
    private String messageSender;
    private String messageReciever;
    private String datetime;
    private String message;

    public Chat(){}

    public Chat(String sender, String reciever, String datetime, String message)
    {
        messageSender = sender;
        messageReciever = reciever;
        this.datetime = datetime;
        this.message = message;
    }

    public String getMessageSender() {
        return messageSender;
    }

    public String getMessageReciever() {
        return messageReciever;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getMessage() {
        return message;
    }
}
