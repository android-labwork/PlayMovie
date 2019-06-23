package com.aitekteam.developer.playmovie.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.aitekteam.developer.playmovie.adapters.StackRemoteViewsFactory;

public class StackWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
