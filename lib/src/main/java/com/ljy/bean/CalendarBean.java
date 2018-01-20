package com.ljy.bean;

/**
 * Created by Mr.LJY on 2018/1/19.
 */

public class CalendarBean {
    private int year;
    private int month;//1-12
    private int day;//1-31
    private boolean isSign;
    private boolean isToday;

    public CalendarBean() {

    }
    public CalendarBean(int year, int month, int day) {
        this.year=year;
        this.month=month;
        this.day=day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isSign() {
        return isSign;
    }

    public void setSign(boolean sign) {
        this.isSign = sign;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        this.isToday = today;
    }

    @Override
    public String toString() {
        return year +"/" + month +"/" + day;
    }
}
