package com.ivanmagda.habito.view.model;

import android.support.annotation.NonNull;

import com.ivanmagda.habito.barchart.HabitoBarChartRange;
import com.ivanmagda.habito.utils.HabitoDateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class HabitDetailViewModel {

    private static final SimpleDateFormat WEEK_FORMAT = new SimpleDateFormat("d MMM yyyy",
            Locale.getDefault());
    private static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("d MMM yyyy",
            Locale.getDefault());
    private static final SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("MMM yyyy",
            Locale.getDefault());

    private HabitoBarChartRange.DateRange mDateRange = HabitoBarChartRange.DateRange.WEEK;

    public HabitDetailViewModel() {
    }

    public HabitDetailViewModel(@NonNull HabitoBarChartRange.DateRange dateRange) {
        this.mDateRange = dateRange;
    }

    public void setDateRange(HabitoBarChartRange.DateRange dateRange) {
        this.mDateRange = dateRange;
    }

    public String getScoreString(int score) {
        return String.valueOf(score);
    }

    public String getDateRangeString() {
        switch (mDateRange) {
            case WEEK:
                return getFormattedWeek(HabitoDateUtils.getStartOfCurrentWeek(),
                        HabitoDateUtils.getEndOfCurrentWeek());
            case MONTH:
                return getFormattedMonth(HabitoDateUtils.getStartOfCurrentMonth(),
                        HabitoDateUtils.getEndOfCurrentMonth());
            case YEAR:
                return getFormattedYear(HabitoDateUtils.getStartOfCurrentYear(),
                        HabitoDateUtils.getEndOfCurrentYear());
            default:
                throw new IllegalArgumentException("Receive illegal date range");
        }
    }

    private String getFormattedWeek(long start, long end) {
        return formatDates(WEEK_FORMAT, start, end);
    }

    private String getFormattedMonth(long start, long end) {
        return formatDates(MONTH_FORMAT, start, end);
    }

    private String getFormattedYear(long start, long end) {
        return formatDates(YEAR_FORMAT, start, end);
    }

    private String formatDates(SimpleDateFormat dateFormat, long start, long end) {
        return dateFormat.format(new Date(start))
                + " - "
                + dateFormat.format(new Date(end));
    }

}
