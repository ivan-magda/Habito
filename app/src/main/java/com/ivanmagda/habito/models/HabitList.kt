package com.ivanmagda.habito.models

import java.util.*
import kotlin.collections.ArrayList

class HabitList(habits: MutableList<Habit>? = ArrayList(), sortOrder: SortOrder = SortOrder.NAME) {

    var habits = habits
        set(newValue) = if (newValue == null) {
            clear()
        } else {
            field = newValue
            sort()
        }

    var sortOrder = sortOrder
        set(newValue) {
            if (sortOrder != newValue) {
                field = newValue
                sort()
            }
        }

    enum class SortOrder(val value: Int) {
        NAME(0), DATE(1);

        companion object {
            fun fromValue(value: Int): SortOrder {
                return when (value) {
                    NAME.value -> NAME
                    DATE.value -> DATE
                    else -> throw IllegalArgumentException("Illegal sort type value")
                }
            }
        }
    }

    init {
        sort()
    }

    fun add(habit: Habit) {
        habits?.add(habit)
        sort()
    }

    fun clear() {
        habits?.clear()
    }

    private fun sort() {
        if (habits!!.isEmpty()) {
            return
        }

        when (sortOrder) {
            HabitList.SortOrder.NAME -> Collections.sort(habits, SortByName())
            HabitList.SortOrder.DATE -> {
                // Sort in decreasing order.
                Collections.sort(habits!!, SortByDate())
                Collections.reverse(habits!!)
            }
        }
    }

    // Sort in increasing order.
    private inner class SortByName : Comparator<Habit> {
        override fun compare(lhs: Habit, rhs: Habit): Int {
            val lhsName = lhs.record.name
            val rhsName = rhs.record.name
            return lhsName.toLowerCase().compareTo(rhsName.toLowerCase())
        }
    }

    private inner class SortByDate : Comparator<Habit> {
        override fun compare(lhs: Habit, rhs: Habit): Int {
            val lhsDate = lhs.record.createdAt
            val rhsDate = rhs.record.createdAt
            return lhsDate.compareTo(rhsDate)
        }
    }
}
