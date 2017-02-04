package com.ivanmagda.habito.app;

import android.app.Application;

import com.ivanmagda.habito.utils.FirebaseUtils;

public class HabitApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseUtils.setOfflineModeEnabled(true);
    }

}
