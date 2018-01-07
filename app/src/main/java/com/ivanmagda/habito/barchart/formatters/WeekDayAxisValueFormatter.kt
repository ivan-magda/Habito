package com.ivanmagda.habito.barchart.formatters

import com.github.mikephil.charting.components.AxisBase
import com.ivanmagda.habito.utils.HabitoDateUtils
import java.text.SimpleDateFormat
import java.util.*

class WeekDayAxisValueFormatter : HabitoBaseIAxisValueFormatter() {

    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        return FORMATTER.format(Date(getDateForValue(value)))
    }

    override fun getDateForValue(value: Float): Long {
        val dateUtils = HabitoDateUtils

        val calendar = dateUtils.getCalendarWithTime(dateUtils.startOfCurrentWeek)
        calendar.add(Calendar.DATE, value.toInt())

        return calendar.timeInMillis
    }

    companion object {
        private val FORMATTER = SimpleDateFormat("EEE", Locale.getDefault())
    }
}

