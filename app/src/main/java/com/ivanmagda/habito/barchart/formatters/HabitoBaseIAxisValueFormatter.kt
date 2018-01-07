package com.ivanmagda.habito.barchart.formatters

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter

abstract class HabitoBaseIAxisValueFormatter : IAxisValueFormatter {

    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        return value.toString()
    }

    abstract fun getDateForValue(value: Float): Long
}
