package com.github.neunkasulle.chronocommand.model;

import javax.persistence.*;

/**
 * Created by Janze on 16.01.2016.
 */
@Entity
@Table(name = "cc_messages")
public class Message {
    @Id
    @GeneratedValue
    int id;

    @Column
    int sendTime;

    @Column
    int receiveTime;

    @Column
    String content;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    User recipient;

    @Column
    boolean isRead;

    public Message(User from, User to, String message){
        this.sender = from;
        this.recipient = to;
        this.content = message;
        this.isRead = false;
    }

    public int getId() {
        return id;
    }

    public int getSendTime() {
        return sendTime;
    }

    public int getReceiveTime() {
        return receiveTime;
    }

    public String getContent() {
        return content;
    }

    public User getSender() {
        return sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
