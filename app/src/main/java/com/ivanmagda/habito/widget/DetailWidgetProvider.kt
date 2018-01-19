package com.ivanmagda.habito.widget

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.app.TaskStackBuilder
import android.widget.RemoteViews

import com.ivanmagda.habito.R
import com.ivanmagda.habito.activity.DetailHabitActivity
import com.ivanmagda.habito.sync.WidgetFetchService

/**
 * Provider for a scrollable habit detail widget
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
class DetailWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val serviceIntent = Intent(context, WidgetFetchService::class.java)
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            context.startService(serviceIntent)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == DATA_FETCHED) {
            val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID)
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val remoteViews = updateWidgetListView(context, appWidgetId)
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun updateWidgetListView(context: Context, appWidgetId: Int): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_detail)

        // Set up the collection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setRemoteAdapter(context, views, appWidgetId)
        } else {
            setRemoteAdapterV11(context, views, appWidgetId)
        }

        val clickIntentTemplate = Intent(context, DetailHabitActivity::class.java)
        val clickPendingIntentTemplate = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(clickIntentTemplate)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setPendingIntentTemplate(R.id.widget_list, clickPendingIntentTemplate)
        views.setEmptyView(R.id.widget_list, R.id.widget_empty)

        return views
    }

    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private fun setRemoteAdapter(context: Context, views: RemoteViews, appWidgetId: Int) {
        val intent = Intent(context, DetailWidgetRemoteViewsService::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
        views.setRemoteAdapter(R.id.widget_list, intent)
    }

    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    private fun setRemoteAdapterV11(context: Context, views: RemoteViews, appWidgetId: Int) {
        val intent = Intent(context, DetailWidgetRemoteViewsService::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
        views.setRemoteAdapter(appWidgetId, R.id.widget_list, intent)
    }

    companion object {
        // String to be sent on Broadcast as soon as Data is Fetched
        // should be included on WidgetProvider manifest intent action
        // to be recognized by this WidgetProvider to receive broadcast
        val DATA_FETCHED = "com.ivanmagda.habito.DATA_FETCHED"
    }
}