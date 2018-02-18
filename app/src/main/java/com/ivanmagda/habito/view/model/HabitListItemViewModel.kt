package com.ivanmagda.habito.view.model

import android.content.Context
import android.graphics.Color
import com.ivanmagda.habito.R
import com.ivanmagda.habito.model.Habit
import com.ivanmagda.habito.model.ResetFrequency
import java.text.SimpleDateFormat
import java.util.*

class HabitListItemViewModel(val context: Context, var habit: Habit? = null) {

    val backgroundColor: Int
        get() = habit!!.record.color

    val habitName: String
        get() = habit!!.record.name

    // TODO: 'getColor(Int): Int' is deprecated.
    val habitNameTextColor: Int
        get() {
            val color = habit?.record?.color
            return if (color == Color.WHITE) {
                context.resources.getColor(R.color.primary_text)
            } else {
                Color.WHITE
            }
        }

    val resetFreq: String
        get() {
            val resources = context.resources
            return when (habit!!.record.resetFreq) {
                ResetFrequency.DAY -> resources.getString(R.string.list_item_reset_today)
                ResetFrequency.WEEK -> resetFreqStringWithParameter(ResetFrequency.WEEK)
                ResetFrequency.MONTH -> resetFreqStringWithParameter(ResetFrequency.MONTH)
                ResetFrequency.YEAR -> resetFreqStringWithParameter(ResetFrequency.YEAR)
                ResetFrequency.NEVER -> {
                    val date = Date(habit!!.record.createdAt)
                    resources.getString(R.string.list_item_reset_never, FORMAT.format(date))
                }
                else -> throw IllegalArgumentException("Unsupported reset freq time")
            }
        }

    val score: String
        get() {
            val score = habit!!.record.score
            val target = habit!!.record.target
            return if (target > 0) {
                (score.toString() + "/"
                        + target.toString())
            } else {
                score.toString()
            }
        }

    private fun resetFreqStringWithParameter(freqPeriod: String): String {
        return context.resources.getString(R.string.list_item_reset_freq, freqPeriod)
    }

    companion object {
        private val FORMAT = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    }
}
