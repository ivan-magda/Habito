package com.ivanmagda.habito.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.ivanmagda.habito.R;
import com.ivanmagda.habito.models.HabitList;

public final class SharedPreferencesUtils {

    public static HabitList.SortOrder getSortOrder(@NonNull final Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        int defaultValue = HabitList.SortOrder.NAME.getValue();
        int sortOrder = sharedPreferences.getInt(
                context.getString(R.string.saved_sort_order), defaultValue);
        return HabitList.SortOrder.fromValue(sortOrder);
    }

    public static void setSortOrder(@NonNull final Context context, HabitList.SortOrder sortOrder) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.saved_sort_order), sortOrder.getValue());
        editor.apply();
    }

    private static SharedPreferences getSharedPreferences(@NonNull final Context context) {
        return context.getSharedPreferences(context.getString(R.string.preference_sort_file_key),
                Context.MODE_PRIVATE);
    }

    private SharedPreferencesUtils() {
    }

}
