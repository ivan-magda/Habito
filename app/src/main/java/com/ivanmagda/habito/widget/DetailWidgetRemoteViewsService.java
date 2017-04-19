package com.ivanmagda.habito.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class DetailWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new ListProvider(this, intent));
    }

}
