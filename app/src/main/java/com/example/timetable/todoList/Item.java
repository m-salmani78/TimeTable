package com.example.timetable.todoList;

import java.sql.Time;

public class Item {
    private boolean done = false;
    private String subject, comment;
    private Time timeBegin,timeEnd;
    private Week week;

    public enum Week {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;
    }
    public Item() {
        subject = "";
        comment = "";
        timeBegin = new Time(0L);
    }
    public Item(boolean done, String subject, String comment, Time time, Week week) {
        this.done = done;
        this.subject = subject;
        this.comment = comment;
        this.timeBegin = time;
        this.week = week;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
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

    public Time getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Time timeEnd) {
        this.timeEnd = timeEnd;
    }
}
