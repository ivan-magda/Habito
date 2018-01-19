package com.ivanmagda.habito.utils

import com.ivanmagda.habito.model.Habit
import com.ivanmagda.habito.model.ResetFrequency
import com.ivanmagda.habito.sync.FirebaseSyncUtils

object HabitoScoreUtils {

    fun processAll(habits: List<Habit>) {
        for (habit in habits) {
            if (isNeedToResetScore(habit)) {
                resetScore(habit)
                FirebaseSyncUtils.applyChangesForHabit(habit)
            }
        }
    }

    fun increaseScore(habit: Habit) {
        habit.record.checkmarks.add(System.currentTimeMillis())
        reloadScoreValue(habit)
    }

    fun decreaseScore(habit: Habit) {
        val record = habit.record

        val checkmarksCount = record.checkmarks.size
        if (checkmarksCount <= 0) {
            return
        }

        val lastCheckmark = record.checkmarks[checkmarksCount - 1]
        val type = ResetFrequency.typeFrom(record.resetFreq)

        if (HabitoDateUtils.isDateInType(lastCheckmark, type)) {
            removeLastCheckmark(habit)
            reloadScoreValue(habit)
        }
    }

    private fun isNeedToResetScore(habit: Habit): Boolean {
        val lastReset = habit.record.resetTimestamp
        val type = ResetFrequency.typeFrom(habit.record.resetFreq)

        return !HabitoDateUtils.isDateInType(lastReset, type)
    }

    fun resetScore(habit: Habit) {
        habit.record.resetTimestamp = System.currentTimeMillis()
        reloadScoreValue(habit)
    }

    private fun reloadScoreValue(habit: Habit) {
        val record = habit.record
        val resetFrequency = ResetFrequency(record.resetFreq)
        val filtered = HabitoListUtils(record.checkmarks).filteredBy(resetFrequency.type)

        record.score = filtered.size
    }

    private fun removeLastCheckmark(habit: Habit) {
        val record = habit.record
        if (record.checkmarks.size > 0) {
            removeCheckmarkAtIndex(habit, record.checkmarks.size - 1)
        }
    }

    private fun removeCheckmarkAtIndex(habit: Habit, index: Int) {
        val record = habit.record
        if (index < 0 || index >= record.checkmarks.size) {
            throw ArrayIndexOutOfBoundsException(index)
        } else {
            record.checkmarks.removeAt(index)
        }
    }
}
