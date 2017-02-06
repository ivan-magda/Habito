package com.ivanmagda.habito.utils;

import android.support.annotation.NonNull;

import com.ivanmagda.habito.models.Habit;
import com.ivanmagda.habito.models.HabitRecord;
import com.ivanmagda.habito.models.ResetFrequency;
import com.ivanmagda.habito.sync.FirebaseSyncUtils;

import java.util.List;

public final class HabitoScoreUtils {

    public static void processAll(@NonNull final List<Habit> habits) {
        for (Habit habit : habits) {
            if (isNeedToResetScore(habit)) {
                resetScore(habit);
                FirebaseSyncUtils.applyChangesForHabit(habit);
            }
        }
    }

    public static void increaseScore(@NonNull final Habit habit) {
        habit.getRecord().getCheckmarks().add(System.currentTimeMillis());
        reloadScoreValue(habit);
    }

    public static void decreaseScore(@NonNull final Habit habit) {
        HabitRecord record = habit.getRecord();

        int checkmarksCount = record.getCheckmarks().size();
        if (checkmarksCount <= 0) return;

        long lastCheckmark = record.getCheckmarks().get(checkmarksCount - 1);
        ResetFrequency.Type type = ResetFrequency.typeFrom(record.getResetFreq());

        if (HabitoDateUtils.isDateInType(lastCheckmark, type)) {
            removeLastCheckmark(habit);
            reloadScoreValue(habit);
        }
    }

    public static boolean isNeedToResetScore(@NonNull final Habit habit) {
        long lastReset = habit.getRecord().getResetTimestamp();
        ResetFrequency.Type type = ResetFrequency.typeFrom(habit.getRecord().getResetFreq());
        return !HabitoDateUtils.isDateInType(lastReset, type);
    }

    public static void resetScoreIfNeeded(@NonNull final Habit habit) {
        if (isNeedToResetScore(habit)) resetScore(habit);
    }

    public static void resetScore(@NonNull final Habit habit) {
        habit.getRecord().setResetTimestamp(System.currentTimeMillis());
        reloadScoreValue(habit);
    }

    public static void reloadScoreValue(@NonNull final Habit habit) {
        HabitRecord record = habit.getRecord();
        ResetFrequency resetFrequency = new ResetFrequency(record.getResetFreq());

        HabitoListUtils listUtils = new HabitoListUtils(record.getCheckmarks());
        List<Long> filtered = listUtils.filteredBy(resetFrequency.getType());
        record.setScore(filtered.size());
    }

    private static void removeLastCheckmark(@NonNull final Habit habit) {
        HabitRecord record = habit.getRecord();
        if (record.getCheckmarks().size() > 0) {
            removeCheckmarkAtIndex(habit, record.getCheckmarks().size() - 1);
        }
    }

    private static void removeCheckmarkAtIndex(@NonNull final Habit habit, int index) {
        HabitRecord record = habit.getRecord();
        if (index < 0 || index >= record.getCheckmarks().size()) {
            throw new ArrayIndexOutOfBoundsException(index);
        } else {
            record.getCheckmarks().remove(index);
        }
    }

}
