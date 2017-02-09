package com.ivanmagda.habito.view.model;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.ivanmagda.habito.R;
import com.ivanmagda.habito.barchart.HabitoBarChartRange;
import com.ivanmagda.habito.barchart.formatters.HabitoBaseIAxisValueFormatter;
import com.ivanmagda.habito.barchart.formatters.MonthAxisValueFormatter;
import com.ivanmagda.habito.barchart.formatters.WeekDayAxisValueFormatter;
import com.ivanmagda.habito.barchart.formatters.YearAxisValueFormatter;
import com.ivanmagda.habito.models.Habit;
import com.ivanmagda.habito.utils.HabitoStringUtils;

public class HabitoBarChartViewModel {

    private Habit mHabit;
    private HabitoBarChartRange.DateRange mDateRange;

    public HabitoBarChartViewModel(Habit habit, HabitoBarChartRange.DateRange dateRange) {
        this.mHabit = habit;
        this.mDateRange = dateRange;
    }

    public HabitoBaseIAxisValueFormatter getXAxisFormatter() {
        switch (mDateRange) {
            case WEEK:
                return new WeekDayAxisValueFormatter();
            case MONTH:
                return new MonthAxisValueFormatter();
            case YEAR:
                return new YearAxisValueFormatter();
            default:
                throw new IllegalArgumentException("Receive illegal date range");
        }
    }

    public String getBarDataSetName(@NonNull final Context context) {
        Resources resources = context.getResources();
        switch (mDateRange) {
            case WEEK:
                String week = resources.getString(R.string.week).toLowerCase();
                return HabitoStringUtils.capitalized(
                        resources.getString(R.string.bar_chart_set_name, week));
            case MONTH:
                String month = resources.getString(R.string.month).toLowerCase();
                return HabitoStringUtils.capitalized(
                        resources.getString(R.string.bar_chart_set_name, month));
            case YEAR:
                String year = resources.getString(R.string.year).toLowerCase();
                return HabitoStringUtils.capitalized(
                        resources.getString(R.string.bar_chart_set_name, year));
            default:
                throw new IllegalArgumentException("Receive illegal date range");
        }
    }

}
