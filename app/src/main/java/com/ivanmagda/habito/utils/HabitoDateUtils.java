package com.ivanmagda.habito.utils;

import android.text.format.DateUtils;

import com.ivanmagda.habito.models.ResetFrequency;

import java.util.Calendar;
import java.util.Date;

public final class HabitoDateUtils {

    public static Calendar getCurrentCalendar() {
        return getCalendarWithTime(System.currentTimeMillis());
    }

    public static Calendar getCalendarWithTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar;
    }

    public static boolean isWithinRange(long toCheck, long start, long end) {
        return isWithinRange(new Date(toCheck), new Date(start), new Date(end));
    }

    public static boolean isWithinRange(Date toCheck, Date start, Date end) {
        return !(toCheck.before(start) || toCheck.after(end));
    }

    public static boolean isDateInCurrentWeek(long date) {
        Calendar currentCalendar = getCurrentCalendar();
        int week = currentCalendar.get(Calendar.WEEK_OF_YEAR);
        int year = currentCalendar.get(Calendar.YEAR);

        Calendar targetCalendar = getCalendarWithTime(date);
        int targetWeek = targetCalendar.get(Calendar.WEEK_OF_YEAR);
        int targetYear = targetCalendar.get(Calendar.YEAR);

        return (week == targetWeek) && (year == targetYear);
    }

    public static boolean isDateInCurrentMonth(long date) {
        Calendar currentCalendar = getCurrentCalendar();
        int month = currentCalendar.get(Calendar.MONTH);
        int year = currentCalendar.get(Calendar.YEAR);

        Calendar targetCalendar = getCalendarWithTime(date);
        int targetMonth = targetCalendar.get(Calendar.MONTH);
        int targetYear = targetCalendar.get(Calendar.YEAR);

        return (month == targetMonth) && (year == targetYear);
    }

    public static boolean isDateInCurrentYear(long date) {
        Calendar currentCalendar = getCurrentCalendar();
        int year = currentCalendar.get(Calendar.YEAR);

        Calendar targetCalendar = getCalendarWithTime(date);
        int targetYear = targetCalendar.get(Calendar.YEAR);

        return (year == targetYear);
    }

    public static boolean isDateInType(long date, ResetFrequency.Type type) {
        switch (type) {
            case DAY:
                return DateUtils.isToday(date);
            case WEEK:
                return HabitoDateUtils.isDateInCurrentWeek(date);
            case MONTH:
                return HabitoDateUtils.isDateInCurrentMonth(date);
            case YEAR:
                return HabitoDateUtils.isDateInCurrentYear(date);
            case NEVER:
                return true;
            default:
                throw new IllegalArgumentException("Received illegal type");
        }
    }

    private HabitoDateUtils() {
    }

}
