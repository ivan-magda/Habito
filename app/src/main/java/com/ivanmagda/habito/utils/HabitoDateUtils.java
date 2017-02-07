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
        return isDatesInSameMonth(getCurrentCalendar().getTimeInMillis(),
                getCalendarWithTime(date).getTimeInMillis());
    }

    public static boolean isDatesInSameWeek(long lhsDate, long rhsDate) {
        Calendar lhsCalendar = getCalendarWithTime(lhsDate);
        int lhsWeek = lhsCalendar.get(Calendar.WEEK_OF_YEAR);
        int lhsYear = lhsCalendar.get(Calendar.YEAR);

        Calendar rhsCalendar = getCalendarWithTime(rhsDate);
        int rhsWeek = rhsCalendar.get(Calendar.WEEK_OF_YEAR);
        int rhsYear = rhsCalendar.get(Calendar.YEAR);

        return (lhsWeek == rhsWeek) && (lhsYear == rhsYear);
    }

    public static boolean isDateInCurrentMonth(long date) {
        return isDatesInSameMonth(getCurrentCalendar().getTimeInMillis(),
                getCalendarWithTime(date).getTimeInMillis());
    }

    public static boolean isDatesInSameMonth(long lhs, long rhs) {
        Calendar lhsCalendar = getCalendarWithTime(lhs);
        int lhsMonth = lhsCalendar.get(Calendar.MONTH);
        int lhsYear = lhsCalendar.get(Calendar.YEAR);

        Calendar rhsCalendar = getCalendarWithTime(rhs);
        int rhsMonth = rhsCalendar.get(Calendar.MONTH);
        int rhsYear = rhsCalendar.get(Calendar.YEAR);

        return (lhsMonth == rhsMonth) && (lhsYear == rhsYear);
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

    public static long getStartOfCurrentWeek() {
        return getStartOfWeek(System.currentTimeMillis());
    }

    public static long getStartOfWeek(long week) {
        Calendar calendar = getCalendarWithTime(week);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        // get start of this week in milliseconds
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        return calendar.getTimeInMillis();
    }

    public static long getEndOfCurrentWeek() {
        Calendar calendar = getCalendarWithTime(getStartOfCurrentWeek());
        calendar.add(Calendar.DATE, 6);
        return calendar.getTimeInMillis();
    }

    public static boolean isSameDay(long lhs, long rhs) {
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
        Calendar calendar = getCurrentCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTimeInMillis();
    }

    public static long getEndOfCurrentMonth() {
        Calendar calendar = getCurrentCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTimeInMillis();
    }

    public static long getStartOfCurrentYear() {
        Calendar calendar = getCurrentCalendar();
        calendar.set(Calendar.MONTH, 0);
        return calendar.getTimeInMillis();
    }

    public static long getEndOfCurrentYear() {
        Calendar calendar = getCurrentCalendar();
        calendar.set(Calendar.MONTH, 11);
        return calendar.getTimeInMillis();
    }

    private HabitoDateUtils() {
    }

}
