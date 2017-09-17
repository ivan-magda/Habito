package com.ivanmagda.habito.analytics;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseUser;
import com.ivanmagda.habito.models.Habit;

public final class HabitoAnalytics {

    private static FirebaseAnalytics sFirebaseAnalytics;

    public static void initAnalytics(Context context) {
        // Obtain the FirebaseAnalytics instance.
        sFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public static FirebaseAnalytics getInstance(Context context) {
        if (sFirebaseAnalytics == null) initAnalytics(context);
        return sFirebaseAnalytics;
    }

    public static void logCreateHabitWithName(@NonNull final String name) {
        Bundle params = new Bundle();
        params.putString("habit_name", name);
        sFirebaseAnalytics.logEvent("create_habit", params);
    }

    public static void logAppOpen() {
        Bundle bundle = new Bundle();
        bundle.putLong(FirebaseAnalytics.Param.START_DATE, System.currentTimeMillis());
        sFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);
    }

    public static void logLogin(FirebaseUser user) {
        Bundle bundle = new Bundle();
        bundle.putLong(FirebaseAnalytics.Param.START_DATE, System.currentTimeMillis());
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, user.getUid());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, user.getDisplayName());
        sFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
    }

    public static void logViewHabitListItem(Habit habit) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, habit.getId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, habit.getRecord().getName());
        bundle.putInt(FirebaseAnalytics.Param.SCORE, habit.getRecord().getScore());
        bundle.putInt("color", habit.getRecord().getColor());
        sFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, bundle);
    }

    private HabitoAnalytics() {
    }

}
