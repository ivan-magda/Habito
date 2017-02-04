package com.ivanmagda.habito.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public final class FirebaseUtils {

    public static final String HABITS_REFERENCE_PATH = "habits";
    public static final String USER_ID_KEY = "userId";

    public static DatabaseReference getHabitsReference() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference().child(FirebaseUtils.HABITS_REFERENCE_PATH);
    }

    public static Query getCurrentUserHabitsQuery() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference habitsReference = getHabitsReference();
            return habitsReference.orderByChild(USER_ID_KEY).equalTo(user.getUid());
        }
        return null;
    }

    public static void setOfflineModeEnabled(boolean enabled) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(enabled);
    }

    private FirebaseUtils() {
    }

}
