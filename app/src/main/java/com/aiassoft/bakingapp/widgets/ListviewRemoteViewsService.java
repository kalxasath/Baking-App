package com.aiassoft.bakingapp.widgets;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.aiassoft.bakingapp.MyApp;

/**
 * Created by gvryn on 20/05/18.
 */

public class ListviewRemoteViewsService extends RemoteViewsService {

    private static final String LOG_TAG = MyApp.APP_TAG + ListviewRemoteViewsService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        Log.d(LOG_TAG, "service onGetViewFactory, return the ListviewRemoteViewsFactory");

        return new ListviewRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
