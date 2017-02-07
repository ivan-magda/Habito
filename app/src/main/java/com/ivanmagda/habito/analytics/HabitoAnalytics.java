package com.ivanmagda.habito.analytics;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseUser;
import com.ivanmagda.habito.models.Habit;

public final class HabitoAnalytics {

    private static FirebaseAnalytics firebaseAnalytics;

    public static void initAnalytics(Context context) {
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public static FirebaseAnalytics getInstance(Context context) {
        if (firebaseAnalytics == null) initAnalytics(context);
        return firebaseAnalytics;
    }

    public static void logCreateHabitWithName(@NonNull final String name) {
        Bundle params = new Bundle();
        params.putString("habit_name", name);
        firebaseAnalytics.logEvent("create_habit", params);
    }

    public static void logAppOpen() {
        Bundle bundle = new Bundle();
        bundle.putLong(FirebaseAnalytics.Param.START_DATE, System.currentTimeMillis());
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);
    }

    public static void logLogin(FirebaseUser user) {
        Bundle bundle = new Bundle();
        bundle.putLong(FirebaseAnalytics.Param.START_DATE, System.currentTimeMillis());
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, user.getUid());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, user.getDisplayName());
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
    }

    public static void logViewHabitListItem(Habit habit) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, habit.getId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, habit.getRecord().getName());
        bundle.putInt(FirebaseAnalytics.Param.SCORE, habit.getRecord().getScore());
        bundle.putInt("color", habit.getRecord().getColor());
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, bundle);
    }

    private HabitoAnalytics() {
    }

}
