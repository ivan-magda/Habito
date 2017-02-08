package com.ivanmagda.habito.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

public class DetailWidgetRemoteViewsService extends RemoteViewsService {

    private static final String TAG = "DetailWidgetRemoteViews";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        return (new ListProvider(this, intent));
    }

}
