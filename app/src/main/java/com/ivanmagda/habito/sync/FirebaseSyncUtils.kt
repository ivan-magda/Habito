package com.ivanmagda.habito.sync

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.ivanmagda.habito.model.Habit
import com.ivanmagda.habito.model.HabitRecord

object FirebaseSyncUtils {

    val HABITS_REFERENCE_PATH = "habits"
    val USER_ID_KEY = "userId"

    val habitsReference: DatabaseReference
        get() {
            val database = FirebaseDatabase.getInstance()
            return database.reference.child(HABITS_REFERENCE_PATH)
        }

    val currentUserHabitsQuery: Query?
        get() {
            val user = FirebaseAuth.getInstance().currentUser
            return if (user != null) {
                habitsReference.orderByChild(USER_ID_KEY).equalTo(user.uid)
            } else null
        }

    fun setOfflineModeEnabled(enabled: Boolean) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(enabled)
    }

    fun createNewHabitRecord(record: HabitRecord) {
        habitsReference.push().setValue(record)
    }

    fun applyChangesForHabit(habit: Habit) {
        habitsReference.child(habit.id!!).setValue(habit.record)
    }

    fun deleteHabit(habit: Habit) {
        habitsReference.child(habit.id!!).setValue(null)
    }
}
