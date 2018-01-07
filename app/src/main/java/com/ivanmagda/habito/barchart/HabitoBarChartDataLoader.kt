package com.ivanmagda.habito.barchart

import android.content.Context
import android.support.v4.content.AsyncTaskLoader

import com.ivanmagda.habito.models.Habit

/**
 * Prefetch data for the bar chart and returns the data source object.
 */
class HabitoBarChartDataLoader(context: Context,
                               private val habit: Habit,
                               private val dateRange: HabitoBarChartRange.DateRange)
    : AsyncTaskLoader<HabitoBarChartDataSource>(context) {

    override fun onStartLoading() {
        forceLoad()
    }

    override fun loadInBackground(): HabitoBarChartDataSource {
        val dataSource = HabitoBarChartDataSource(habit, dateRange)
        dataSource.prefetch()

        return dataSource
    }
}
