package com.networkcommittee.wc.bh4nitjtest;

import java.io.Serializable;

public class WeekDay implements Serializable
{



    Meal monday = new Meal();
    Meal tuesday = new Meal();
    Meal wednesday = new Meal();
    Meal thursday = new Meal();
    Meal friday = new Meal();
    Meal saturday = new Meal();
    Meal sunday = new Meal();

    public Meal getMonday() {
        return monday;
    }

    public void setMonday(Meal monday) {
        this.monday = monday;
    }

    public Meal getTuesday() {
        return tuesday;
    }

    public void setTuesday(Meal tuesday) {
        this.tuesday = tuesday;
    }

    public Meal getWednesday() {
        return wednesday;
    }

    public void setWednesday(Meal wednesday) {
        this.wednesday = wednesday;
    }

    public Meal getThursday() {
        return thursday;
    }

    public void setThursday(Meal thursday) {
        this.thursday = thursday;
    }

    public Meal getFriday() {
        return friday;
    }

    public void setFriday(Meal friday) {
        this.friday = friday;
    }

    public Meal getSaturday() {
        return saturday;
    }

    public void setSaturday(Meal saturday) {
        this.saturday = saturday;
    }

    public Meal getSunday() {
        return sunday;
    }

    public void setSunday(Meal sunday) {
        this.sunday = sunday;
    }

}
