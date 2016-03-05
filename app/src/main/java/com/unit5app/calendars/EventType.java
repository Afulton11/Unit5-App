package com.unit5app.calendars;

/**
 * @author Andrew
 * @version 2/26/16
 */
public enum EventType {

    regular(0), holiday(1), lateStart(2), noSchool(3), lastDayBeforeBreak(4), endOf(5), meeting(6);

    private int id;

    EventType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
