package com.ivanmagda.habito.barchart;

import android.support.annotation.NonNull;

import com.github.mikephil.charting.data.BarEntry;
import com.ivanmagda.habito.models.Habit;
import com.ivanmagda.habito.utils.HabitoDateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public final class HabitoBarChartDataSource {

    private Habit mHabit;
    private List<BarEntry> mEntries;
    private HabitoBarChartRange.DateRange mDateRange;
    private int mMaxValue = 0;

    public HabitoBarChartDataSource(@NonNull Habit habit,
                                    @NonNull HabitoBarChartRange.DateRange dateRange) {
        this.mHabit = habit;
        this.mDateRange = dateRange;
    }

    public void prefetch() {
        buildData();
    }

    /**
     * @return Max value within a given range (e.g. max value between days, weeks of months).
     */
    public int getMaxValue() {
        return mMaxValue;
    }

    public List<BarEntry> getData() {
        if (mEntries == null) {
            buildData();
        }
        return mEntries;
    }

    public int getNumberOfEntries() {
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

    private void buildData() {
        mEntries = new ArrayList<>();
        long baseDate = getBaseDate();
        mMaxValue = 0;

        for (int i = 0; i < getNumberOfEntries(); i++) {
            long currentDate = getDateForEntryAtIndex(baseDate, i);

            int countInRange = 0;
            for (long checkmarkDate : mHabit.getRecord().getCheckmarks()) {
                if (isMeetCompareRule(currentDate, checkmarkDate)) countInRange++;
            }
            mEntries.add(new BarEntry(i, countInRange));

            if (countInRange > mMaxValue) mMaxValue = countInRange;
        }
    }

    private long getBaseDate() {
        switch (mDateRange) {
            case WEEK:
                return HabitoDateUtils.getStartOfCurrentWeek();
            case MONTH:
                return HabitoDateUtils.getStartOfCurrentMonth();
            case YEAR:
                return HabitoDateUtils.getStartOfCurrentYear();
            default:
                throw new IllegalArgumentException("Illegal date range");
        }
    }

    private long getDateForEntryAtIndex(long baseDate, int index) {
        Calendar calendar = HabitoDateUtils.getCalendarWithTime(baseDate);
        switch (mDateRange) {
            case WEEK:
                calendar.add(Calendar.DATE, index);
                break;
            case MONTH:
                calendar.add(Calendar.DAY_OF_WEEK_IN_MONTH, index);
                Date endOfMonth = new Date(HabitoDateUtils.getEndOfCurrentMonth());
                if (calendar.getTime().after(endOfMonth)) {
                    return endOfMonth.getTime();
                }
                break;
            case YEAR:
                calendar.add(Calendar.MONTH, index);
                break;
            default:
                throw new IllegalArgumentException("Illegal date range");
        }
        return calendar.getTimeInMillis();
    }

    private boolean isMeetCompareRule(long currentDate, long checkmarkDate) {
        switch (mDateRange) {
            case WEEK:
                return HabitoDateUtils.isSameDay(currentDate, checkmarkDate);
            case MONTH:
                return HabitoDateUtils.isDatesInSameMonth(currentDate, checkmarkDate)
                        && HabitoDateUtils.isDatesInSameWeek(currentDate, checkmarkDate);
            case YEAR:
                return HabitoDateUtils.isDatesInSameMonth(currentDate, checkmarkDate);
            default:
                throw new IllegalArgumentException("Illegal date range");
        }
    }

}
