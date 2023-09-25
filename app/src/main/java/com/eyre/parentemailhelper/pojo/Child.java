package com.eyre.parentemailhelper.pojo;

import java.util.ArrayList;
import java.util.List;

public class Child {

    private String name;
    private String ID;
    private List<CalenderEvent> calenderEvents = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public List<CalenderEvent> getCalenderEvents() {
        return calenderEvents;
    }

    public void addCalenderEvents(CalenderEvent calenderEvent) {
        calenderEvents.add(calenderEvent);
    }
}
