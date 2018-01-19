package com.ivanmagda.habito.barchart

import com.github.mikephil.charting.data.BarEntry
import com.ivanmagda.habito.model.Habit
import com.ivanmagda.habito.util.HabitoDateUtils
import java.util.*

class HabitoBarChartDataSource(private val habit: Habit,
                               private val dateRange: HabitoBarChartRange.DateRange) {

    private var entries: MutableList<BarEntry>? = null

    /**
     * @return Max value within a given range (e.g. max value between days, weeks of months).
     */
    var maxValue = 0
        private set

    val data: List<BarEntry>
        get() {
            if (entries == null) {
                buildData()
            }

            return entries!!
        }

    val numberOfEntries: Int
        get() {
            return when (dateRange) {
                HabitoBarChartRange.DateRange.WEEK -> 7
                HabitoBarChartRange.DateRange.MONTH -> HabitoDateUtils.numberOfWeeksInCurrentMonth
                HabitoBarChartRange.DateRange.YEAR -> 12
            }
        }

    private val baseDate: Long
        get() {
            return when (dateRange) {
                HabitoBarChartRange.DateRange.WEEK -> HabitoDateUtils.startOfCurrentWeek
                HabitoBarChartRange.DateRange.MONTH -> HabitoDateUtils.startOfCurrentMonth
                HabitoBarChartRange.DateRange.YEAR -> HabitoDateUtils.startOfCurrentYear
            }
        }

    fun prefetch() {
        buildData()
    }

    private fun buildData() {
        entries = ArrayList()
        val baseDate = baseDate
        maxValue = 0

        for (i in 0 until numberOfEntries) {
            val currentDate = getDateForEntryAtIndex(baseDate, i)

            val countInRange = habit.record.checkmarks.count { isMeetCompareRule(currentDate, it) }
            entries!!.add(BarEntry(i.toFloat(), countInRange.toFloat()))

            if (countInRange > maxValue) {
                maxValue = countInRange
            }
        }
    }

    private fun getDateForEntryAtIndex(baseDate: Long, index: Int): Long {
        val calendar = HabitoDateUtils.getCalendarWithTime(baseDate)

        when (dateRange) {
            HabitoBarChartRange.DateRange.WEEK -> calendar.add(Calendar.DATE, index)
            HabitoBarChartRange.DateRange.MONTH -> {
                calendar.add(Calendar.DAY_OF_WEEK_IN_MONTH, index)
                val endOfMonth = Date(HabitoDateUtils.endOfCurrentMonth)
                if (calendar.time.after(endOfMonth)) {
                    return endOfMonth.time
                }
            }
            HabitoBarChartRange.DateRange.YEAR -> calendar.add(Calendar.MONTH, index)
        }

        return calendar.timeInMillis
    }

    private fun isMeetCompareRule(currentDate: Long, checkmarkDate: Long): Boolean {
        return when (dateRange) {
            HabitoBarChartRange.DateRange.WEEK -> HabitoDateUtils.isSameDay(currentDate, checkmarkDate)
            HabitoBarChartRange.DateRange.MONTH -> HabitoDateUtils.isDatesInSameMonth(currentDate, checkmarkDate) && HabitoDateUtils.isDatesInSameWeek(currentDate, checkmarkDate)
            HabitoBarChartRange.DateRange.YEAR -> HabitoDateUtils.isDatesInSameMonth(currentDate, checkmarkDate)
        }
    }
}
