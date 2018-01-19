package com.ivanmagda.habito.view.model

import com.ivanmagda.habito.barchart.HabitoBarChartRange
import com.ivanmagda.habito.util.HabitoDateUtils
import java.text.SimpleDateFormat
import java.util.*

class HabitDetailViewModel(var dateRange: HabitoBarChartRange.DateRange = HabitoBarChartRange.DateRange.WEEK) {

    val dateRangeString: String
        get() {
            return when (dateRange) {
                HabitoBarChartRange.DateRange.WEEK -> getFormattedWeek(HabitoDateUtils.startOfCurrentWeek,
                        HabitoDateUtils.endOfCurrentWeek)
                HabitoBarChartRange.DateRange.MONTH -> getFormattedMonth(HabitoDateUtils.startOfCurrentMonth,
                        HabitoDateUtils.endOfCurrentMonth)
                HabitoBarChartRange.DateRange.YEAR -> getFormattedYear(HabitoDateUtils.startOfCurrentYear,
                        HabitoDateUtils.endOfCurrentYear)
            }
        }

    fun getScoreString(score: Int): String {
        return score.toString()
    }

    private fun getFormattedWeek(start: Long, end: Long): String {
        return formatDates(WEEK_FORMAT, start, end)
    }

    private fun getFormattedMonth(start: Long, end: Long): String {
        return formatDates(MONTH_FORMAT, start, end)
    }

    private fun getFormattedYear(start: Long, end: Long): String {
        return formatDates(YEAR_FORMAT, start, end)
    }

    private fun formatDates(dateFormat: SimpleDateFormat, start: Long, end: Long): String {
        return (dateFormat.format(Date(start))
                + " - "
                + dateFormat.format(Date(end)))
    }

    companion object {
        private val WEEK_FORMAT = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        private val MONTH_FORMAT = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        private val YEAR_FORMAT = SimpleDateFormat("MMM yyyy", Locale.getDefault())
    }
}
