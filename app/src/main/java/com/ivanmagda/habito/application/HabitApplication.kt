package com.ivanmagda.habito.application

import android.app.Application

import com.ivanmagda.habito.analytics.HabitoAnalytics
import com.ivanmagda.habito.sync.FirebaseSyncUtils

class HabitApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        HabitoAnalytics.initAnalytics(this)
        HabitoAnalytics.logAppOpen()

        FirebaseSyncUtils.setOfflineModeEnabled(true)
    }
}
