package com.ivanmagda.habito.application;

import android.app.Application;

import com.ivanmagda.habito.analytics.HabitoAnalytics;
import com.ivanmagda.habito.sync.FirebaseSyncUtils;

public class HabitApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HabitoAnalytics.initAnalytics(this);
        HabitoAnalytics.logAppOpen();
        FirebaseSyncUtils.setOfflineModeEnabled(true);
    }

}
