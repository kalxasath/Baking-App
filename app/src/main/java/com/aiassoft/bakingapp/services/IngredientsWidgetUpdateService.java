/**
 * Copyright (C) 2018 by George Vrynios
 * This project was made under the supervision of Udacity
 * in the Android Developer Nanodegree Program
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aiassoft.bakingapp.services;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.aiassoft.bakingapp.Const;
import com.aiassoft.bakingapp.MyApp;
import com.aiassoft.bakingapp.R;
import com.aiassoft.bakingapp.activities.MainActivity;
import com.aiassoft.bakingapp.widgets.ListviewRemoteViewsService;

import static com.aiassoft.bakingapp.utilities.PrefUtils.getWidgetRecipePosition;

/**
 * Created by gvryn on 25/05/18.
 */

/** This service updates the widgets with the appropriate recipe ingredients */
public class IngredientsWidgetUpdateService extends Service {

    private static final String LOG_TAG = MyApp.APP_TAG + IngredientsWidgetUpdateService.class.getSimpleName();

    private AppWidgetManager mAppWidgetManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mAppWidgetManager = AppWidgetManager.getInstance(this);

        int[] allWidgetIds = intent
                .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        mAppWidgetManager.notifyAppWidgetViewDataChanged(allWidgetIds, R.id.lv_ingredients);

        for (int appWidgetId : allWidgetIds) {
            /** Reaches the view on widget */
            RemoteViews rv = new RemoteViews(getPackageName(), R.layout.ingredients_widget_provider);

            /** Get widgets RecipePosition */
            int recipePosition = getWidgetRecipePosition(appWidgetId);

            if (! MyApp.hasData() || recipePosition == Const.INVALID_INT) {
                // If the app doesn't start because of bug, or developer stop
                // invalidate widgets since we do not have an app instance with
                // data from internet
                showDeveloperInfo(rv);
            } else {

                hideDeveloperInfo(rv);
                updateRemoteViews(rv, appWidgetId, recipePosition);
            }

            mAppWidgetManager.updateAppWidget(appWidgetId, rv);
        }
        stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     *
     * @param rv the remove view
     */
    private void showDeveloperInfo(RemoteViews rv) {
        rv.setTextViewText(R.id.tv_recipe_title, MyApp.getContext().getString(R.string.appwidget_title));
        rv.setViewVisibility(R.id.tv_ingredients_title, View.GONE);
        rv.setViewVisibility(R.id.iv_ingredients_separator, View.GONE);
        rv.setViewVisibility(R.id.lv_ingredients, View.GONE);
        rv.setEmptyView(R.id.lv_ingredients, R.id.empty_view);
    }

    private void hideDeveloperInfo(RemoteViews rv) {
        rv.setViewVisibility(R.id.tv_ingredients_title, View.VISIBLE);
        rv.setViewVisibility(R.id.iv_ingredients_separator, View.VISIBLE);
        rv.setViewVisibility(R.id.lv_ingredients, View.VISIBLE);
        rv.setViewVisibility(R.id.empty_view, View.GONE);
    }

    private void updateRemoteViews(RemoteViews rv, int appWidgetId, int recipePosition) {
        rv.setTextViewText(R.id.tv_recipe_title, MyApp.mRecipesData.get(recipePosition).getName());

        /** Set up the RemoteViews object to use a RemoteViews adapter.
         This adapter connects
         to a RemoteViewsService through the specified intent.
         This is how you populate the data. */
        Intent setListviewRemoteViewsIntent;

        /** Set up the intent that starts the ListViewService, which will
         provide the views for this collection. */
        setListviewRemoteViewsIntent = new Intent(MyApp.getContext(), ListviewRemoteViewsService.class);

        /** Add the app widget ID & RecipePosition to the intent extras. */
        setListviewRemoteViewsIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setListviewRemoteViewsIntent.putExtra(Const.EXTRA_RECIPE_POS, recipePosition);
        setListviewRemoteViewsIntent.setData(Uri.parse(setListviewRemoteViewsIntent.toUri(Intent.URI_INTENT_SCHEME)));

        /** Finally set the remote Adapter */
        rv.setRemoteAdapter(R.id.lv_ingredients, setListviewRemoteViewsIntent);

        //setting an empty view in case of no data
        rv.setEmptyView(R.id.lv_ingredients, R.id.empty_view);
    }
}
