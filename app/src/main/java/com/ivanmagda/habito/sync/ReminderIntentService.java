package com.ivanmagda.habito.sync;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.ivanmagda.habito.R;
import com.ivanmagda.habito.activities.DetailHabitActivity;
import com.ivanmagda.habito.models.Habit;
import com.ivanmagda.habito.utils.NotificationUtils;
import com.ivanmagda.habito.utils.ReminderUtils;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */
public class ReminderIntentService extends IntentService {

    private static final String ACTION_START = "ACTION_START";

    public ReminderIntentService() {
        super(ReminderIntentService.class.getSimpleName());
    }

    public static Intent buildInstance(Habit habit, Context context) {
        Intent intent = new Intent(context, ReminderIntentService.class);
        intent.setAction(ACTION_START);
        intent.putExtra(ReminderUtils.HABIT_EXTRA_KEY, habit);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            if (ACTION_START.equals(intent.getAction())) {
                Habit habit = intent.getParcelableExtra(ReminderUtils.HABIT_EXTRA_KEY);
                processNotification(habit);
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }


    private void processNotification(Habit habit) {
        Intent resultIntent = new Intent(this, DetailHabitActivity.class);
        resultIntent.putExtra(DetailHabitActivity.HABIT_EXTRA_KEY, habit);
        final int notificationId = (int) habit.getRecord().getCreatedAt();

        NotificationUtils notificationUtils = new NotificationUtils(this);
        NotificationCompat.Builder builder = notificationUtils.getNotification(
                getString(R.string.app_name),
                habit.getRecord().getName(),
                habit.getRecord().getColor()
        );

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(notificationId,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        notificationUtils.getManager().notify(notificationId, builder.build());
    }

}
