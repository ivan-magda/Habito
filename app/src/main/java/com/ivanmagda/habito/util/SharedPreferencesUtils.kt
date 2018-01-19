package com.ivanmagda.habito.util

import android.content.Context
import android.content.SharedPreferences

import com.ivanmagda.habito.R
import com.ivanmagda.habito.model.HabitList

object SharedPreferencesUtils {

    fun getSortOrder(context: Context): HabitList.SortOrder {
        val sharedPreferences = getSharedPreferences(context)

        val defaultValue = HabitList.SortOrder.NAME.value
        val sortOrder = sharedPreferences.getInt(context.getString(R.string.saved_sort_order), defaultValue)

        return HabitList.SortOrder.fromValue(sortOrder)
    }

    fun setSortOrder(context: Context, sortOrder: HabitList.SortOrder) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putInt(context.getString(R.string.saved_sort_order), sortOrder.value)
        editor.apply()
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(context.getString(R.string.preference_sort_file_key),
                Context.MODE_PRIVATE)
    }
}
