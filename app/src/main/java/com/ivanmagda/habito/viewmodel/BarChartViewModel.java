package com.ivanmagda.habito.viewmodel;

import com.ivanmagda.habito.barchart.BarChartRange;
import com.ivanmagda.habito.models.Habit;
import com.ivanmagda.habito.utils.HabitoDateUtils;

public class BarChartViewModel {

    private Habit mHabit;
    private BarChartRange.DateRange mDateRange;

    public BarChartViewModel(Habit habit, BarChartRange.DateRange dateRange) {
        this.mHabit = habit;
        this.mDateRange = dateRange;
    }

    public int getXAxisLabelCount() {
        switch (mDateRange) {
            case WEEK:
                return 7;
            case MONTH:
                return HabitoDateUtils.getNumberOfWeeksInCurrentMonth();
            case YEAR:
                return 12;
            default:
                throw new IllegalArgumentException("Receive illegal date range");
        }
    }

    public String getBarDataSetName() {
        switch (mDateRange) {
            case WEEK:
                return "This week";
            case MONTH:
                return "This month";
            case YEAR:
                return "This year";
            default:
                throw new IllegalArgumentException("Receive illegal date range");
        }
    }

}
