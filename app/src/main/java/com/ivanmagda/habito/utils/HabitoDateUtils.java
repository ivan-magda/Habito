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

    public static long getStartOfThisWeek() {
        Calendar calendar = getCurrentCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        // get start of this week in milliseconds
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        return calendar.getTimeInMillis();
    }

    public static boolean sameDay(long lhs, long rhs) {
        Calendar lhsCal = Calendar.getInstance();
        Calendar rhsCal = Calendar.getInstance();
        lhsCal.setTimeInMillis(lhs);
        rhsCal.setTimeInMillis(rhs);
        return (lhsCal.get(Calendar.YEAR) == rhsCal.get(Calendar.YEAR))
                && (lhsCal.get(Calendar.DAY_OF_YEAR) == rhsCal.get(Calendar.DAY_OF_YEAR));
    }

    public static int getNumberOfWeeksInCurrentMonth() {
        Calendar calendar = getCalendarWithTime(getStartOfCurrentMonth());
        return calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
    }

    public static long getStartOfCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTimeInMillis();
    }

    public static long getEndOfCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTimeInMillis();
    }

    private HabitoDateUtils() {
    }

}
