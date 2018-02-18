package com.ivanmagda.habito.sync

import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.TaskStackBuilder
import android.support.v4.content.WakefulBroadcastReceiver
import com.ivanmagda.habito.activity.DetailHabitActivity
import com.ivanmagda.habito.model.Habit
import com.ivanmagda.habito.util.NotificationUtils
import com.ivanmagda.habito.util.ReminderUtils

// TODO: 'WakefulBroadcastReceiver' is deprecated.

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
class ReminderIntentService : IntentService(ReminderIntentService::class.java.simpleName) {

    override fun onHandleIntent(intent: Intent?) {
        try {
            if (intent?.action == ACTION_START) {
                val habit = intent.getParcelableExtra<Habit>(ReminderUtils.HABIT_EXTRA_KEY)
                processNotification(habit)
            }
        } finally {
            if (intent != null) {
                WakefulBroadcastReceiver.completeWakefulIntent(intent)
            }
        }
    }

    private fun processNotification(habit: Habit) {
        val resultIntent = Intent(this, DetailHabitActivity::class.java)
        resultIntent.putExtra(DetailHabitActivity.HABIT_EXTRA_KEY, habit)
        val notificationId = habit.record.createdAt.toInt()

        val notificationUtils = NotificationUtils(this)
        val builder = notificationUtils.getNotification(contentText = habit.record.name, color = habit.record.color)

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackBuilder.getPendingIntent(notificationId, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(resultPendingIntent)

        notificationUtils.manager.notify(notificationId, builder.build())
    }

    companion object {
        private val ACTION_START = "ACTION_START"

        fun buildInstance(habit: Habit, context: Context): Intent {
            val intent = Intent(context, ReminderIntentService::class.java)
            intent.action = ACTION_START
            intent.putExtra(ReminderUtils.HABIT_EXTRA_KEY, habit)

            return intent
        }
    }
}
