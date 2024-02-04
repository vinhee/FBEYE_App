package com.example.fbeyeapp;

public class employeeAttendance {
    Object Date;
    Object ClockIn;
    Object ClockOut;

    public employeeAttendance(Object date, Object clockIn, Object clockOut) {
        Date = date;
        ClockIn = clockIn;
        ClockOut = clockOut;
    }

    public Object getDate() {
        return Date;
    }

    public void setDate(Object date) {
        Date = date;
    }

    public Object getClockIn() {
        return ClockIn;
    }

    public void setClockIn(Object clockIn) {
        ClockIn = clockIn;
    }

    public Object getClockOut() {
        return ClockOut;
    }

    public void setClockOut(Object clockOut) {
        ClockOut = clockOut;
    }
}
