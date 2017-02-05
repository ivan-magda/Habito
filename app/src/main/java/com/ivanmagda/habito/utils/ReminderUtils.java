package com.ivanmagda.habito.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.ivanmagda.habito.models.Habit;
import com.ivanmagda.habito.receivers.ReminderReceiver;

import java.util.Calendar;
import java.util.List;

public class ReminderUtils {

    private static final String TAG = "ReminderUtils";

    public static final String HABIT_EXTRA_KEY = "habit";

    private static PendingIntent buildPendingIntent(@NonNull final Habit habit,
                                                    @NonNull final Context context) {
        Intent alarmIntent = new Intent(context, ReminderReceiver.class);
        alarmIntent.putExtra(HABIT_EXTRA_KEY, habit);
        return PendingIntent.getBroadcast(context, (int) habit.getRecord().getCreatedAt(),
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void setAlarm(@NonNull final Habit habit, @NonNull final Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = buildPendingIntent(habit, context);

        if (!habit.isReminderOn()) {
            alarmManager.cancel(pendingIntent);
        } else {
            Calendar now = Calendar.getInstance();
            now.setTimeInMillis(System.currentTimeMillis());
            Calendar alarmTime = Calendar.getInstance();
            alarmTime.setTimeInMillis(System.currentTimeMillis());
            alarmTime.set(Calendar.HOUR_OF_DAY, habit.getRecord().getReminderHour());
            alarmTime.set(Calendar.MINUTE, habit.getRecord().getReminderMin());
            alarmTime.set(Calendar.SECOND, 0);

            if (now.after(alarmTime)) {
                alarmTime.add(Calendar.DATE, 1);
            }

            alarmManager.cancel(pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public static void cancelAlarm(@NonNull final Habit habit, @NonNull final Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = buildPendingIntent(habit, context);
        alarmManager.cancel(pendingIntent);
    }

    public static void processOn(@NonNull final Habit habit, @NonNull final Context context) {
        if (habit.isReminderOn()) {
            setAlarm(habit, context);
        } else {
            cancelAlarm(habit, context);
        }
    }

    public static void processAll(@NonNull final List<Habit> habits, @NonNull final Context context) {
        for (Habit habit : habits) {
            processOn(habit, context);
        }
    }

    public static void scheduleAll(@NonNull final List<Habit> habits, @NonNull final Context context) {
        for (Habit habit : habits) {
            setAlarm(habit, context);
        }
    }

    public static void cancelAll(@NonNull final List<Habit> habits, @NonNull final Context context) {
        for (Habit habit : habits) {
            cancelAlarm(habit, context);
        }
    }

    private ReminderUtils() {
    }

}
