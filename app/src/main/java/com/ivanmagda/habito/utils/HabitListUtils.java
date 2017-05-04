package com.ivanmagda.habito.utils;

import com.google.firebase.database.DataSnapshot;

import com.ivanmagda.habito.models.*;
import java.util.ArrayList;
import java.util.List;

public final class HabitListUtils {

    public static List<Habit> CreateHabitListFromDataSnapshot(DataSnapshot dataSnapshot)
    {

        List<Habit> habits = new ArrayList<>((int) dataSnapshot.getChildrenCount());
        for (DataSnapshot data : dataSnapshot.getChildren()) {
            HabitRecord parsedRecord = data.getValue(HabitRecord.class);
            habits.add(new Habit(data.getKey(), parsedRecord));
        }
        return habits;

    }

    private HabitListUtils() {
    }

}