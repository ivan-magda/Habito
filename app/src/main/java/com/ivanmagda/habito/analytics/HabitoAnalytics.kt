package com.ivanmagda.habito.analytics

import android.content.Context
import android.os.Bundle

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseUser
import com.ivanmagda.habito.models.Habit

object HabitoAnalytics {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun initAnalytics(context: Context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    fun logCreateHabitWithName(name: String) {
        val params = Bundle()
        params.putString("habit_name", name)

        firebaseAnalytics.logEvent("create_habit", params)
    }

    fun logAppOpen() {
        val bundle = Bundle()
        bundle.putLong(FirebaseAnalytics.Param.START_DATE, System.currentTimeMillis())

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)
    }

    fun logLogin(user: FirebaseUser) {
        val bundle = Bundle()
        bundle.putLong(FirebaseAnalytics.Param.START_DATE, System.currentTimeMillis())
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, user.uid)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, user.displayName)

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)
    }

    fun logViewHabitListItem(habit: Habit) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, habit.id)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, habit.record.name)
        bundle.putInt(FirebaseAnalytics.Param.SCORE, habit.record.score)
        bundle.putInt("color", habit.record.color)

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, bundle)
    }
}
