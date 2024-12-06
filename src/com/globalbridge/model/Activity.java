package com.globalbridge.model;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Activity implements Serializable {
    private Date date;
    private String content;
    private String location;
    private boolean isCompleted;

    public Activity(Date date, String content, String location) {
        this.date = date;
        this.content = content;
        this.location = location;
        this.isCompleted = false;
    }

    // Getters and Setters
    public Date getDate() { return date; }
    public String getContent() { return content; }
    public String getLocation() { return location; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return String.format("%s | %s @ %s [%s]",
                sdf.format(date),
                content,
                location,
                isCompleted ? "완료" : "진행중");
    }
}