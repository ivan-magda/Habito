package com.ivanmagda.habito.view.model

import android.content.Context
import com.ivanmagda.habito.R
import com.ivanmagda.habito.barchart.HabitoBarChartRange
import com.ivanmagda.habito.barchart.formatters.HabitoBaseIAxisValueFormatter
import com.ivanmagda.habito.barchart.formatters.MonthAxisValueFormatter
import com.ivanmagda.habito.barchart.formatters.WeekDayAxisValueFormatter
import com.ivanmagda.habito.barchart.formatters.YearAxisValueFormatter
import com.ivanmagda.habito.util.HabitoStringUtils

class HabitoBarChartViewModel(private val dateRange: HabitoBarChartRange.DateRange) {

    val xAxisFormatter: HabitoBaseIAxisValueFormatter
        get() {
            return when (dateRange) {
                HabitoBarChartRange.DateRange.WEEK -> WeekDayAxisValueFormatter()
                HabitoBarChartRange.DateRange.MONTH -> MonthAxisValueFormatter()
                HabitoBarChartRange.DateRange.YEAR -> YearAxisValueFormatter()
            }
        }

    fun getBarDataSetName(context: Context): String {
        val resources = context.resources
        when (dateRange) {
            HabitoBarChartRange.DateRange.WEEK -> {
                val week = resources.getString(R.string.week).toLowerCase()
                return HabitoStringUtils.capitalized(
                        resources.getString(R.string.bar_chart_set_name, week))
            }
            HabitoBarChartRange.DateRange.MONTH -> {
                val month = resources.getString(R.string.month).toLowerCase()
                return HabitoStringUtils.capitalized(
                        resources.getString(R.string.bar_chart_set_name, month))
            }
            HabitoBarChartRange.DateRange.YEAR -> {
                val year = resources.getString(R.string.year).toLowerCase()
                return HabitoStringUtils.capitalized(
                        resources.getString(R.string.bar_chart_set_name, year))
            }
            else -> throw IllegalArgumentException("Receive illegal date range")
        }
    }
}
