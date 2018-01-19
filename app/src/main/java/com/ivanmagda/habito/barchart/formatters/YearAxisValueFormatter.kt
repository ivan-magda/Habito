package com.ivanmagda.habito.barchart.formatters

import com.github.mikephil.charting.components.AxisBase
import com.ivanmagda.habito.util.HabitoDateUtils
import java.text.SimpleDateFormat
import java.util.*

class YearAxisValueFormatter : HabitoBaseIAxisValueFormatter() {

    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        val yearString = FORMATTER.format(Date(getDateForValue(value)))
        return yearString.substring(0, 1)
    }

    override fun getDateForValue(value: Float): Long {
        val calendar = HabitoDateUtils.currentCalendar
        calendar.set(Calendar.MONTH, value.toInt())

        return calendar.timeInMillis
    }

    companion object {
        private val FORMATTER = SimpleDateFormat("MMM", Locale.getDefault())
    }
}
