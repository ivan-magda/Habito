package com.ivanmagda.habito.app;

import android.app.Application;

import com.ivanmagda.habito.sync.FirebaseSyncUtils;

public class HabitApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseSyncUtils.setOfflineModeEnabled(true);
    }

}
