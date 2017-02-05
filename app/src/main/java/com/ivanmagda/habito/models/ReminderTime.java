package com.ivanmagda.habito.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class ReminderTime {

    private int mHour;
    private int mMinutes;

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public ReminderTime() {
        final Calendar c = Calendar.getInstance();
        this.mHour = c.get(Calendar.HOUR_OF_DAY);
        this.mMinutes = c.get(Calendar.MINUTE);
    }

    public ReminderTime(int hour, int minutes) {
        this.mHour = hour;
        this.mMinutes = minutes;
    }

    public int getHour() {
        return mHour;
    }

    public void setHour(int hour) {
        this.mHour = hour;
    }

    public int getMinutes() {
        return mMinutes;
    }

    public void setMinutes(int minutes) {
        this.mMinutes = minutes;
    }

    public String getTimeString() {
        return FORMATTER.format(getTodayReminderDate());
    }

    public static String getTimeString(int hour, int minutes) {
        return getTimeString(new ReminderTime(hour, minutes));
    }

    public static String getTimeString(ReminderTime reminderTime) {
        return reminderTime.getTimeString();
    }

    public Date getTodayReminderDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, mHour);
        calendar.set(Calendar.MINUTE, mMinutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    @Override
    public String toString() {
        return "ReminderTime{" +
                "mHour=" + mHour +
                ", mMinutes=" + mMinutes +
                '}';
    }

}
