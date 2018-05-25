package com.aiassoft.bakingapp.services;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.aiassoft.bakingapp.Const;
import com.aiassoft.bakingapp.MyApp;
import com.aiassoft.bakingapp.R;
import com.aiassoft.bakingapp.widgets.ListviewRemoteViewsService;

import static com.aiassoft.bakingapp.utilities.PrefUtils.getWidgetRecipePosition;

/**
 * Created by gvryn on 25/05/18.
 */

public class IngredientsWidgetUpdateService extends Service {

    private static final String LOG_TAG = MyApp.APP_TAG + IngredientsWidgetUpdateService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        int[] allWidgetIds = intent
                .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        /*
        int appWidgetId = intent.getIntExtra(Const.EXTRA_WIDGET_ID, Const.INVALID_INT);

        if (appWidgetId == Const.INVALID_INT) {
            Log.d(LOG_TAG, "onStartCommand, invalid widgetId received");
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }
        Log.d(LOG_TAG, "onStartCommand, widgetId: " + appWidgetId);
        */

        appWidgetManager.notifyAppWidgetViewDataChanged(allWidgetIds, R.id.lv_ingredients);

        for (int appWidgetId : allWidgetIds) {
            /**
             * Get widgets RecipePosition
             */
            int recipePosition = getWidgetRecipePosition(appWidgetId);

            if (! MyApp.hasData() || recipePosition == Const.INVALID_INT) {
                Log.d(LOG_TAG, "onStartCommand, MyApp.hasData: " + MyApp.hasData() + ", recipePos: " + recipePosition);
                stopSelf();
                return super.onStartCommand(intent, flags, startId);
            }

            Log.d(LOG_TAG, "onStartCommand, recipePosition: " + recipePosition);
            Log.d(LOG_TAG, "onStartCommand, Recipe: " + MyApp.mRecipesData.get(recipePosition).getName());

            /** Reaches the view on widget and displays the number */
            RemoteViews rv = new RemoteViews(getPackageName(), R.layout.ingredients_widget_provider);
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
            setListviewRemoteViewsIntent.setAction("foo");
            setListviewRemoteViewsIntent.setData(Uri.parse(setListviewRemoteViewsIntent.toUri(Intent.URI_INTENT_SCHEME)));

            /** Finally set the remote Adapter */
            rv.setRemoteAdapter(R.id.lv_ingredients, setListviewRemoteViewsIntent);

            //setting an empty view in case of no data
            rv.setEmptyView(R.id.lv_ingredients, R.id.empty_view);

            Log.d(LOG_TAG, "onStartCommand, start updateAppWidget");
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
        stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }
}
