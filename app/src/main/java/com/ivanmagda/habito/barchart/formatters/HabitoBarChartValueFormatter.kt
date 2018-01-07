package com.ivanmagda.habito.barchart.formatters

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler

class HabitoBarChartValueFormatter : IValueFormatter {
    override fun getFormattedValue(value: Float, entry: Entry, dataSetIndex: Int, viewPortHandler: ViewPortHandler): String {
        return value.toInt().toString()
    }
}
