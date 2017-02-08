package com.ivanmagda.habito.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ivanmagda.habito.R;
import com.ivanmagda.habito.activities.DetailHabitActivity;
import com.ivanmagda.habito.models.Habit;
import com.ivanmagda.habito.sync.WidgetFetchService;
import com.ivanmagda.habito.view.model.HabitListItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {

    private List<Habit> mHabitList = new ArrayList<>();
    private Context mContext = null;
    private int appWidgetId;

    public ListProvider(Context context, Intent intent) {
        this.mContext = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        populateListItem();
    }

    private void populateListItem() {
        if (WidgetFetchService.habitList != null) {
            mHabitList = new ArrayList<>(WidgetFetchService.habitList);
        } else {
            mHabitList = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return mHabitList.size();
    }

    @Override
    public long getItemId(int position) {
        return mHabitList.get(position).getRecord().getCreatedAt();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Habit habit = mHabitList.get(position);
        HabitListItemViewModel viewModel = new HabitListItemViewModel(mContext, habit);

        final RemoteViews views = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_list_item);
        views.setTextViewText(R.id.tv_widget_name, viewModel.getHabitName());
        views.setTextViewText(R.id.tv_widget_score, viewModel.getScore());

        final Intent fillInIntent = new Intent();
        fillInIntent.putExtra(DetailHabitActivity.HABIT_EXTRA_KEY, habit);
        views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

        return views;
    }


    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }

}
