package com.github.neunkasulle.chronocommand.model;

import javax.persistence.*;

/**
 * Created by Janze on 16.01.2016.
 * Class for storing user to user messages
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

    /**
     * Creates a new Message
     * @param from The sender of the message
     * @param to the recipient
     * @param message the text of the message
     */
    public Message(User from, User to, String message){
        this.sender = from;
        this.recipient = to;
        this.content = message;
        this.isRead = false;
    }

    /**
     * Returns its internal DB id
     * @return the id as integer
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the sending timestamp
     * @return the sending timestamp
     */
    public int getSendTime() {
        return sendTime;
    }

    /**
     * Gets the receiving timestamp
     * @return the receiving timestamp
     */
    public int getReceiveTime() {
        return receiveTime;
    }

    /**
     * returns the Message text
     * @return the message text
     */
    public String getContent() {
        return content;
    }

    /**
     * Gets the Sender
     * @return the sending user
     */
    public User getSender() {
        return sender;
    }

    /**
     * Gets the recipient
     * @return the receiving user
     */
    public User getRecipient() {
        return recipient;
    }

    /**
     * Gets the status if the message has been read
     * @return true if read, false otherwise
     */
    public boolean isRead() {
        return isRead;
    }

    /**
     * Sets the message to read status
     */
    public void setRead() {
        isRead = true;
    }
}
