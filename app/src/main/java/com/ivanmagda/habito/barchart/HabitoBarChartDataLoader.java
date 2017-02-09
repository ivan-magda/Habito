package com.ivanmagda.habito.barchart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.ivanmagda.habito.models.Habit;

/**
 * Prefetch data for the bar chart and returns the data source object.
 */
public class HabitoBarChartDataLoader extends AsyncTaskLoader<HabitoBarChartDataSource> {

    private Habit mHabit;
    private HabitoBarChartRange.DateRange mDateRange;

    public HabitoBarChartDataLoader(Context context,
                                    @NonNull Habit habit,
                                    @NonNull HabitoBarChartRange.DateRange dateRange) {
        super(context);
        this.mHabit = habit;
        this.mDateRange = dateRange;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public HabitoBarChartDataSource loadInBackground() {
        HabitoBarChartDataSource dataSource = new HabitoBarChartDataSource(mHabit, mDateRange);
        dataSource.prefetch();
        return dataSource;
    }

}
