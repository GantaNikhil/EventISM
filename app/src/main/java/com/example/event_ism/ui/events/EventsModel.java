package com.example.event_ism.ui.events;

public class EventsModel {

    String by;

    String event;

    String count;

    String date;

    String docid;

    String eventid;

    String enrollcheck;

    public EventsModel(String event, String by, String count, String date, String docid, String eventid,String enrollcheck) {
        this.by = by;
        this.count = count;
        this.event = event;
        this.date = date;
        this.docid = docid;
        this.eventid = eventid;
        this.enrollcheck=enrollcheck;
    }

    public EventsModel() {
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
}
