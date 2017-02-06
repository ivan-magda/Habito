package com.ivanmagda.habito.sync;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ivanmagda.habito.models.Habit;
import com.ivanmagda.habito.models.HabitRecord;

public final class FirebaseSyncUtils {

    public static final String HABITS_REFERENCE_PATH = "habits";
    public static final String USER_ID_KEY = "userId";

    public static DatabaseReference getHabitsReference() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference().child(HABITS_REFERENCE_PATH);
    }

    public static Query getCurrentUserHabitsQuery() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return getHabitsReference().orderByChild(USER_ID_KEY).equalTo(user.getUid());
        }
        return null;
    }

    public static void setOfflineModeEnabled(boolean enabled) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(enabled);
    }

    public static void createNewHabitRecord(HabitRecord record) {
        getHabitsReference().push().setValue(record);
    }

    public static void applyChangesForHabit(@NonNull final Habit habit) {
        getHabitsReference().child(habit.getId()).setValue(habit.getRecord());
    }

    public static void deleteHabit(@NonNull final Habit habit) {
        getHabitsReference().child(habit.getId()).setValue(null);
    }

    private FirebaseSyncUtils() {
    }

}
