package com.ivanmagda.habito.sync

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.IBinder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.ivanmagda.habito.model.Habit
import com.ivanmagda.habito.model.HabitRecord
import com.ivanmagda.habito.widget.DetailWidgetProvider
import java.util.*

class WidgetFetchService : Service() {

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private var mUserHabitsQuery: Query? = null
    private var mValueEventListener: ValueEventListener? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID)
        }
        fetchData()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun fetchData() {
        mUserHabitsQuery = FirebaseSyncUtils.currentUserHabitsQuery

        if (mUserHabitsQuery == null) {
            populateWidget()
        } else {
            mValueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    processOnDataChange(dataSnapshot)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            }

            mUserHabitsQuery!!.addListenerForSingleValueEvent(mValueEventListener)
        }
    }

    private fun processOnDataChange(dataSnapshot: DataSnapshot) {
        habitList = ArrayList(dataSnapshot.childrenCount.toInt())
        for (data in dataSnapshot.children) {
            val parsedRecord = data.getValue(HabitRecord::class.java)
            if (parsedRecord != null) {
                habitList.add(Habit(data.key, parsedRecord))
            }
        }

        populateWidget()
    }

    /**
     * Method which sends broadcast to WidgetProvider
     * so that widget is notified to do necessary action
     * and here action == DetailWidgetProvider.DATA_FETCHED
     */
    private fun populateWidget() {
        val widgetUpdateIntent = Intent()
        widgetUpdateIntent.action = DetailWidgetProvider.DATA_FETCHED
        widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        sendBroadcast(widgetUpdateIntent)

        this.stopSelf()
    }

    companion object {
        var habitList: ArrayList<Habit> = ArrayList()
    }
}
