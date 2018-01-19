package com.ivanmagda.habito.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.ivanmagda.habito.R
import com.ivanmagda.habito.activities.DetailHabitActivity
import com.ivanmagda.habito.model.Habit
import com.ivanmagda.habito.sync.WidgetFetchService
import com.ivanmagda.habito.view.model.HabitListItemViewModel
import java.util.*

class ListProvider(val context: Context, val intent: Intent) : RemoteViewsService.RemoteViewsFactory {

    private var habitList: List<Habit> = ArrayList()

    init {
        habitList = ArrayList(WidgetFetchService.habitList)
    }

    override fun getCount(): Int {
        return habitList.size
    }

    override fun getItemId(position: Int): Long {
        return habitList[position].record.createdAt
    }

    override fun getViewAt(position: Int): RemoteViews {
        val habit = habitList[position]
        val viewModel = HabitListItemViewModel(context, habit)

        val views = RemoteViews(context.packageName, R.layout.widget_list_item)
        views.setTextViewText(R.id.tv_widget_name, viewModel.habitName)
        views.setTextViewText(R.id.tv_widget_score, viewModel.score)

        val fillInIntent = Intent()
        fillInIntent.putExtra(DetailHabitActivity.HABIT_EXTRA_KEY, habit)
        views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent)

        return views
    }


    override fun getLoadingView(): RemoteViews {
        return RemoteViews(context.packageName, R.layout.widget_list_item)
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun onCreate() {}

    override fun onDataSetChanged() {}

    override fun onDestroy() {}
}
