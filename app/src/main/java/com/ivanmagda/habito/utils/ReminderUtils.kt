package com.ivanmagda.habito.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.ivanmagda.habito.models.Habit
import com.ivanmagda.habito.receivers.ReminderReceiver
import java.util.*

object ReminderUtils {

    val HABIT_EXTRA_KEY = "habit"

    fun processHabit(habit: Habit, context: Context) {
        if (habit.isReminderOn) {
            setAlarm(habit, context)
        } else {
            cancelAlarm(habit, context)
        }
    }

    fun processAll(habits: List<Habit>, context: Context) {
        for (habit in habits) {
            processHabit(habit, context)
        }
    }

    fun scheduleAll(habits: List<Habit>, context: Context) {
        for (habit in habits) {
            setAlarm(habit, context)
        }
    }

    fun cancelAll(habits: List<Habit>, context: Context) {
        for (habit in habits) {
            cancelAlarm(habit, context)
        }
    }

    private fun buildPendingIntent(habit: Habit, context: Context): PendingIntent {
        val alarmIntent = Intent(context, ReminderReceiver::class.java)
        alarmIntent.putExtra(HABIT_EXTRA_KEY, habit)

        return PendingIntent.getBroadcast(context, habit.record.createdAt.toInt(), alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun setAlarm(habit: Habit, context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = buildPendingIntent(habit, context)

        if (!habit.isReminderOn) {
            alarmManager.cancel(pendingIntent)
        } else {
            val now = Calendar.getInstance()
            now.timeInMillis = System.currentTimeMillis()

            val alarmTime = Calendar.getInstance()
            alarmTime.timeInMillis = System.currentTimeMillis()
            alarmTime.set(Calendar.HOUR_OF_DAY, habit.record.reminderHour)
            alarmTime.set(Calendar.MINUTE, habit.record.reminderMin)
            alarmTime.set(Calendar.SECOND, 0)

            if (now.after(alarmTime)) {
                alarmTime.add(Calendar.DATE, 1)
            }

            alarmManager.cancel(pendingIntent)
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime.timeInMillis,
                    AlarmManager.INTERVAL_DAY, pendingIntent)
        }
    }

    private fun cancelAlarm(habit: Habit, context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = buildPendingIntent(habit, context)
        alarmManager.cancel(pendingIntent)
    }
}
