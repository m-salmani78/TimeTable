package com.example.timetable.datamodel;

import java.sql.Time;

public class Item {
    private int done = -1, duration, id;
    private String subject, comment;
    private Time timeBegin;
    private Week week;
    private boolean deleted=false;

    public enum Week {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;

    }
    public Item(int id) {
        this.id = id;
        subject = "";
        comment = "";
        timeBegin = new Time(0L);
    }

    public Item(int id, String subject, String comment, Time time, Week week) {
        this.id = id;
        this.subject = subject;
        this.comment = comment;
        this.timeBegin = time;
        this.week = week;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int isDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Time getTimeBegin() {
        return timeBegin;
    }

    public void setTimeBegin(Time timeBegin) {
        this.timeBegin = timeBegin;
    }

    public Week getWeek() {
        return week;
    }

    public void setWeek(Week week) {
        this.week = week;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
