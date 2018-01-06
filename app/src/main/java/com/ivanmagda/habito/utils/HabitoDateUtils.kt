package com.ivanmagda.habito.utils

import android.text.format.DateUtils
import com.ivanmagda.habito.models.ResetFrequency
import java.util.*

object HabitoDateUtils {

    val currentCalendar: Calendar
        get() = getCalendarWithTime(System.currentTimeMillis())

    val startOfCurrentWeek: Long
        get() = getStartOfWeek(System.currentTimeMillis())

    val endOfCurrentWeek: Long
        get() {
            val calendar = getCalendarWithTime(startOfCurrentWeek)
            calendar.add(Calendar.DATE, 6)

            return calendar.timeInMillis
        }

    val numberOfWeeksInCurrentMonth: Int
        get() {
            val calendar = getCalendarWithTime(startOfCurrentMonth)

            return calendar.getActualMaximum(Calendar.WEEK_OF_MONTH)
        }

    val startOfCurrentMonth: Long
        get() {
            val calendar = currentCalendar
            calendar.set(Calendar.DAY_OF_MONTH, 1)

            return calendar.timeInMillis
        }

    val endOfCurrentMonth: Long
        get() {
            val calendar = currentCalendar
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))

            return calendar.timeInMillis
        }

    val startOfCurrentYear: Long
        get() {
            val calendar = currentCalendar
            calendar.set(Calendar.MONTH, 0)

            return calendar.timeInMillis
        }

    val endOfCurrentYear: Long
        get() {
            val calendar = currentCalendar
            calendar.set(Calendar.MONTH, 11)

            return calendar.timeInMillis
        }

    fun getCalendarWithTime(time: Long): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time

        return calendar
    }

    fun isWithinRange(toCheck: Long, start: Long, end: Long): Boolean {
        return isWithinRange(Date(toCheck), Date(start), Date(end))
    }

    fun isWithinRange(toCheck: Date, start: Date, end: Date): Boolean {
        return !(toCheck.before(start) || toCheck.after(end))
    }

    fun isDateInCurrentWeek(date: Long): Boolean {
        return isDatesInSameMonth(currentCalendar.timeInMillis, getCalendarWithTime(date).timeInMillis)
    }

    fun isDatesInSameWeek(lhsDate: Long, rhsDate: Long): Boolean {
        val lhsCalendar = getCalendarWithTime(lhsDate)
        val lhsWeek = lhsCalendar.get(Calendar.WEEK_OF_YEAR)
        val lhsYear = lhsCalendar.get(Calendar.YEAR)

        val rhsCalendar = getCalendarWithTime(rhsDate)
        val rhsWeek = rhsCalendar.get(Calendar.WEEK_OF_YEAR)
        val rhsYear = rhsCalendar.get(Calendar.YEAR)

        return lhsWeek == rhsWeek && lhsYear == rhsYear
    }

    fun isDateInCurrentMonth(date: Long): Boolean {
        return isDatesInSameMonth(currentCalendar.timeInMillis, getCalendarWithTime(date).timeInMillis)
    }

    fun isDatesInSameMonth(lhs: Long, rhs: Long): Boolean {
        val lhsCalendar = getCalendarWithTime(lhs)
        val lhsMonth = lhsCalendar.get(Calendar.MONTH)
        val lhsYear = lhsCalendar.get(Calendar.YEAR)

        val rhsCalendar = getCalendarWithTime(rhs)
        val rhsMonth = rhsCalendar.get(Calendar.MONTH)
        val rhsYear = rhsCalendar.get(Calendar.YEAR)

        return lhsMonth == rhsMonth && lhsYear == rhsYear
    }

    fun isDateInCurrentYear(date: Long): Boolean {
        val currentCalendar = currentCalendar
        val year = currentCalendar.get(Calendar.YEAR)

        val targetCalendar = getCalendarWithTime(date)
        val targetYear = targetCalendar.get(Calendar.YEAR)

        return year == targetYear
    }

    fun isDateInType(date: Long, type: ResetFrequency.Type): Boolean {
        return when (type) {
            ResetFrequency.Type.DAY -> DateUtils.isToday(date)
            ResetFrequency.Type.WEEK -> HabitoDateUtils.isDateInCurrentWeek(date)
            ResetFrequency.Type.MONTH -> HabitoDateUtils.isDateInCurrentMonth(date)
            ResetFrequency.Type.YEAR -> HabitoDateUtils.isDateInCurrentYear(date)
            ResetFrequency.Type.NEVER -> true
        }
    }

    fun getStartOfWeek(week: Long): Long {
        val calendar = getCalendarWithTime(week)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.clear(Calendar.MINUTE)
        calendar.clear(Calendar.SECOND)
        calendar.clear(Calendar.MILLISECOND)

        // get start of this week in milliseconds
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)

        return calendar.timeInMillis
    }

    fun isSameDay(lhs: Long, rhs: Long): Boolean {
        val lhsCal = Calendar.getInstance()
        val rhsCal = Calendar.getInstance()

        lhsCal.timeInMillis = lhs
        rhsCal.timeInMillis = rhs

        return lhsCal.get(Calendar.YEAR) == rhsCal.get(Calendar.YEAR) &&
                lhsCal.get(Calendar.DAY_OF_YEAR) == rhsCal.get(Calendar.DAY_OF_YEAR)
    }
}
