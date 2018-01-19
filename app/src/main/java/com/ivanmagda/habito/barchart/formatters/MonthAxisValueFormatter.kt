package com.ivanmagda.habito.barchart.formatters

import com.github.mikephil.charting.components.AxisBase
import com.ivanmagda.habito.util.HabitoDateUtils
import java.text.SimpleDateFormat
import java.util.*

class MonthAxisValueFormatter : HabitoBaseIAxisValueFormatter() {

    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        return FORMATTER.format(getDateForValue(value))
    }

    override fun getDateForValue(value: Float): Long {
        val dateUtils = HabitoDateUtils

        val calendar = dateUtils.getCalendarWithTime(dateUtils.startOfCurrentMonth)
        calendar.set(Calendar.WEEK_OF_MONTH, value.toInt() + 1)
        var startOfWeek = dateUtils.getStartOfWeek(calendar.timeInMillis)

        if (Date(startOfWeek).before(Date(dateUtils.startOfCurrentMonth))) {
            startOfWeek = dateUtils.startOfCurrentMonth
        }

        return startOfWeek
    }

    companion object {
        private val FORMATTER = SimpleDateFormat("M/d", Locale.getDefault())
    }
}
