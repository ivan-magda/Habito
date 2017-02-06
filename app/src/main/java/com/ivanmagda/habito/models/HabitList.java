package com.ivanmagda.habito.models;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class HabitList {

    public enum SortOrder {
        NAME(0), DATE(1);

        private final int value;

        SortOrder(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static SortOrder fromValue(int value) {
            if (value == NAME.getValue()) {
                return NAME;
            } else if (value == DATE.getValue()) {
                return DATE;
            } else {
                throw new IllegalArgumentException("Illegal sort type value");
            }
        }
    }

    private List<Habit> mHabits;
    private SortOrder mSortOrder;

    public HabitList(@NonNull List<Habit> habits, @NonNull SortOrder sortOrder) {
        this.mHabits = new ArrayList<>(habits);
        this.mSortOrder = sortOrder;
        sort();
    }

    public HabitList(List<Habit> habits) {
        this(habits, SortOrder.NAME);
    }

    public List<Habit> getHabits() {
        return mHabits;
    }

    public void setHabits(List<Habit> habits) {
        if (habits == null) {
            clear();
        } else {
            this.mHabits = habits;
            sort();
        }
    }

    public SortOrder getSortOrder() {
        return mSortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        if (mSortOrder != sortOrder) {
            this.mSortOrder = sortOrder;
            sort();
        }
    }

    public void add(@NonNull final Habit habit) {
        this.mHabits.add(habit);
        sort();
    }

    public void clear() {
        this.mHabits.clear();
    }

    private void sort() {
        if (mHabits.size() == 0) return;
        switch (mSortOrder) {
            case NAME:
                Collections.sort(mHabits, new SortByName());
                break;
            case DATE:
                // Sort in decreasing order.
                Collections.sort(mHabits, new SortByDate());
                Collections.reverse(mHabits);
                break;
        }
    }

    // Sort in increasing order.
    private class SortByName implements Comparator<Habit> {
        @Override
        public int compare(Habit lhs, Habit rhs) {
            String lhsName = lhs.getRecord().getName();
            String rhsName = rhs.getRecord().getName();
            return lhsName.toLowerCase().compareTo(rhsName.toLowerCase());
        }
    }

    private class SortByDate implements Comparator<Habit> {
        @Override
        public int compare(Habit lhs, Habit rhs) {
            Long lhsDate = lhs.getRecord().getCreatedAt();
            Long rhsDate = rhs.getRecord().getCreatedAt();
            return lhsDate.compareTo(rhsDate);
        }
    }

}
