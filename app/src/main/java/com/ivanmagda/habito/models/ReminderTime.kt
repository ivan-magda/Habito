package com.ivanmagda.habito.models

import java.text.SimpleDateFormat
import java.util.*

class ReminderTime(val hour: Int, val minutes: Int) {

    val timeString: String
        get() = FORMATTER.format(todayReminderDate)

    val todayReminderDate: Date
        get() {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minutes)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            return calendar.time
        }

    override fun toString(): String {
        return "ReminderTime{" +
                "mHour=" + hour +
                ", mMinutes=" + minutes +
                '}'
    }

    companion object {

        private val FORMATTER = SimpleDateFormat("HH:mm", Locale.getDefault())

        fun getTimeString(hour: Int, minutes: Int): String {
            return getTimeString(ReminderTime(hour, minutes))
        }

        fun getTimeString(reminderTime: ReminderTime): String {
            return reminderTime.timeString
        }
    }
}
