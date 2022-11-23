package ru.atom.chat;

import java.util.Date;

public class Message {
    private Date date;
    private String message;
    private String name;

    public Message(Date date, String message, String name) {
        this.date = date;
        this.message = message;
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
