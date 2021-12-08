package com.example.event_ism.ui.enrolls;

public class EnrollsModel {

    String by;

    String event;

    String count;

    String date;

    String docid;

    String eventid;

    String enrollcheck;
    String enrollID;

    public EnrollsModel(String event, String by, String count, String date, String docid, String eventid, String enrollcheck, String enrollID) {
        this.by = by;
        this.count = count;
        this.event = event;
        this.date = date;
        this.docid = docid;
        this.eventid = eventid;
        this.enrollcheck = enrollcheck;
        this.enrollID = enrollID;
    }

    public EnrollsModel() {
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getEnrollcheck() {
        return enrollcheck;
    }

    public void setEnrollcheck(String enrollcheck) {
        this.enrollcheck = enrollcheck;
    }

    public String getEnrollID() {
        return enrollID;
    }

    public void setEnrollID(String enrollID) {
        this.enrollID = enrollID;
    }
}
