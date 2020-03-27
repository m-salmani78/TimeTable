package com.example.timetable.todoList;

import java.sql.Time;

public class Item {
    private boolean done = false;
    private String subject, comment;
    private Time time;
    private Week week;

    public enum Week {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
    }
    Item() {
        subject = "";
        comment = "";
        time = new Time(0L);
    }

    public Item(boolean done, String subject, String comment, Time time, Week week) {
        this.done = done;
        this.subject = subject;
        this.comment = comment;
        this.time = time;
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

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Week getWeek() {
        return week;
    }

    public void setWeek(Week week) {
        this.week = week;
    }
}
