package com.github.neunkasulle.chronocommand.model;

/**
 * Created by Janze on 16.01.2016.
 */
public class Message {
    int id;
    int sendTime;
    int receiveTime;
    String content;
    User sender;
    User recipient;
    boolean isRead;

    public Message(User from, User to, String message){
        this.sender = from;
        this.recipient = to;
        this. content = message;

    }
}
