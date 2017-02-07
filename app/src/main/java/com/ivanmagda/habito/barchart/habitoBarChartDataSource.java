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

    public interface Delegate {
        int numberOfEntries();
    }

    private Habit mHabit;
    private HabitoBarChartRange.DateRange mDateRange;
    private Delegate mDelegate;
    private int mMaxValue = 0;

    public HabitoBarChartDataSource(@NonNull Habit habit, @NonNull HabitoBarChartRange.DateRange dateRange,
                                    @NonNull Delegate delegate) {
        this.mHabit = habit;
        this.mDateRange = dateRange;
        this.mDelegate = delegate;
    }

    /**
     * @return Max value within a given range (e.g. max value between days, weeks of months).
     */
    public int getMaxValue() {
        return mMaxValue;
    }

    public List<BarEntry> buildData() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        long baseDate = getBaseDate();
        mMaxValue = 0;

        for (int i = 0; i < mDelegate.numberOfEntries(); i++) {
            long currentDate = getDateForEntryAtIndex(baseDate, i);

            int countInRange = 0;
            for (long checkmarkDate : mHabit.getRecord().getCheckmarks()) {
                if (isMeetCompareRule(currentDate, checkmarkDate)) countInRange++;
            }
            entries.add(new BarEntry(i, countInRange));

            if (countInRange > mMaxValue) mMaxValue = countInRange;
        }

        return entries;
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
