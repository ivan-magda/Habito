package com.ivanmagda.habito.barchart

import android.content.Context
import com.ivanmagda.habito.R
import java.util.*

object HabitoBarChartRange {

    enum class DateRange {
        WEEK, MONTH, YEAR;

        fun stringValue(context: Context): String {
            val resources = context.resources
            return when (this) {
                WEEK -> resources.getString(R.string.week)
                MONTH -> resources.getString(R.string.month)
                YEAR -> resources.getString(R.string.year)
            }
        }

        companion object {
            fun fromString(string: String, context: Context): DateRange {
                return when (string) {
                    WEEK.stringValue(context) -> WEEK
                    MONTH.stringValue(context) -> MONTH
                    YEAR.stringValue(context) -> YEAR
                    else -> throw IllegalArgumentException("Illegal date string")
                }
            }
        }
    }

    fun allStringValues(context: Context): List<String> {
        return Arrays.asList(DateRange.WEEK.stringValue(context), DateRange.MONTH.stringValue(context), DateRange.YEAR.stringValue(context))
    }
}
