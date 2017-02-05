package com.ivanmagda.habito.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ivanmagda.habito.models.Habit;
import com.ivanmagda.habito.sync.ReminderIntentService;
import com.ivanmagda.habito.utils.ReminderUtils;

public class ReminderReceiver extends BroadcastReceiver {

    public ReminderReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(ReminderUtils.HABIT_EXTRA_KEY)) {
            Habit habit = intent.getParcelableExtra(ReminderUtils.HABIT_EXTRA_KEY);
            Intent serviceIntent = ReminderIntentService.buildInstance(habit, context);
            context.startService(serviceIntent);
        }
    }

}
