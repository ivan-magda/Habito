package com.ivanmagda.habito.adapter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivanmagda.habito.R
import com.ivanmagda.habito.model.Habit
import com.ivanmagda.habito.model.HabitList
import com.ivanmagda.habito.view.model.HabitListItemViewModel
import kotlinx.android.synthetic.main.habit_list_item.view.*

/**
 * Creates a HabitsAdapter.
 *
 * @param habitList     The data source.
 * @param clickListener The on-click handler for this adapter. This single handler is called
 * when an item is clicked.
 */
class HabitsAdapter(private val habitList: HabitList,
                    var clickListener: OnClickListener? = null)
    : RecyclerView.Adapter<HabitsAdapter.HabitAdapterViewHolder>() {

    var habits: MutableList<Habit>?
        get() = habitList.habits
        set(newValue) {
            if (newValue == null) {
                habitList.clear()
            } else {
                habitList.habits = newValue
            }
            notifyDataSetChanged()
        }

    /**
     * The interface that receives onClick messages.
     */
    interface OnClickListener {
        /**
         * @param habit Selected habit.
         * @param position      Index of the selected item.
         */
        fun onClick(habit: Habit, position: Int)
    }

    fun setSortOrder(sortOrder: HabitList.SortOrder) {
        habitList.sortOrder = sortOrder
        notifyDataSetChanged()
    }

    fun clear() {
        habitList.clear()
        notifyDataSetChanged()
    }

    fun add(habit: Habit) {
        habitList.add(habit)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitAdapterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listView = layoutInflater.inflate(R.layout.habit_list_item, parent, false)
        listView.isFocusable = true

        return HabitAdapterViewHolder(listView)
    }

    override fun onBindViewHolder(viewHolder: HabitAdapterViewHolder, position: Int) {
        viewHolder.bindAtPosition(position)
    }

    override fun getItemCount(): Int {
        return habitList.habits?.size ?: 0
    }

    private fun getHabit(index: Int): Habit {
        return habitList.habits!![index]
    }

    /**
     * Cache of the children views for a habits list item.
     */
    inner class HabitAdapterViewHolder(private val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val viewModel: HabitListItemViewModel

        init {
            view.setOnClickListener(this)
            viewModel = HabitListItemViewModel(view.context)
        }

        override fun onClick(view: View) {
            clickListener?.onClick(getHabit(adapterPosition), adapterPosition)
        }

        fun bindAtPosition(position: Int) {
            viewModel.habit = getHabit(position)

            if (view is CardView) {
                view.setCardBackgroundColor(viewModel.backgroundColor)
            } else {
                view.setBackgroundColor(viewModel.backgroundColor)
            }

            view.tv_list_item_habit_title.text = viewModel.habitName
            view.tv_list_item_habit_title.setTextColor(viewModel.habitNameTextColor)
            view.tv_list_item_reset_period.text = viewModel.resetFreq
            view.tv_list_item_count.text = viewModel.score
        }
    }
}
