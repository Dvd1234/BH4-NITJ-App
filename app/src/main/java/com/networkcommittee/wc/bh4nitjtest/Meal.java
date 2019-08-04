package com.networkcommittee.wc.bh4nitjtest;

import java.io.Serializable;

public class Meal implements Serializable
{

    public String breakfast;
    public String lunch;
    public String snacks;
    public String dinner;

    public Meal() {
    }

    public Meal(String breakfast, String lunch, String snacks, String dinner) {
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.snacks = snacks;
        this.dinner = dinner;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getSnacks() {
        return snacks;
    }

    public void setSnacks(String snacks) {
        this.snacks = snacks;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

}
