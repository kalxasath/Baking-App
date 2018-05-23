package com.aiassoft.bakingapp.Widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.aiassoft.bakingapp.Const;
import com.aiassoft.bakingapp.MyApp;
import com.aiassoft.bakingapp.R;
import com.aiassoft.bakingapp.activities.MainActivity;
import com.aiassoft.bakingapp.activities.RecipeActivity;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {

    private static final String LOG_TAG = MyApp.APP_TAG + IngredientsWidgetProvider.class.getSimpleName();

    public static final String UPDATE_WIDGET = "android.appwidget.action.APPWIDGET_UPDATE";
    public static final String EXTRA_ITEM = "EXTRA_ITEM";

    private static int mRecipe;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Log.d(LOG_TAG, "provider updateAppWidget");

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);
        views.setTextViewText(R.id.tv_recipe_title, "Makaronada me kima");

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    // TODO after retrieving the new data from internet send a broadcast to update widget
    // TODO Store recipe id in preferences, add preferences in widget service to read the receipe
    // TODO if app has no data, display message tap to open the app and wait until the data are fetched from internet
    // TODO Also explain how to set widgets recipe from menu
    // TODO after setting new recipe in preferences send a broadcat to update widgets data
    // TODO if more than 1 widget display message only one widget is allowed

    /** Every widget action is performed via the OnReceive like APPWIDGET_ENABLED, APPWIDGET_UPDATE */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "provider onReceive, received: " + intent.getAction());

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        if (intent.getAction().equals(UPDATE_WIDGET)) {

            int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, IngredientsWidgetProvider.class));
            Log.d(LOG_TAG, "provider onReceive, appWidgetIds[] count: " + appWidgetIds.length);

            if (appWidgetIds.length > 1) {
                Toast.makeText(context, "Only one widget is allowed", Toast.LENGTH_LONG).show();
            } else {
                Log.d(LOG_TAG, "provider onReceive, call notifyAppWidgetViewDataChanged");
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_ingredients);
            }
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(LOG_TAG, "provider onUpdate");

        mRecipe = 1;
        Intent setOnClickIntent;
        Intent setListviewRemoteViewsIntent;

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            // Set up the intent that starts the ListViewService, which will
            // provide the views for this collection.
            setListviewRemoteViewsIntent = new Intent(context, ListviewRemoteViewsService.class);

            // Add the app widget ID to the intent extras.
            setListviewRemoteViewsIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            //intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            // Instantiate the RemoteViews object for the app widget layout.
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);

            if(! MyApp.hasData()) {
                Log.d(LOG_TAG, "provider onUpdate, INVISIBLE VIEWS");
                // Create an Intent to launch the app or the RecipeActivity when clicked
                setOnClickIntent = new Intent(context, MainActivity.class);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, setOnClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                rv.setOnClickPendingIntent(R.id.ll_widget, pendingIntent);

                rv.setTextViewText(R.id.tv_recipe_title, context.getString(R.string.appwidget_title));
                rv.setViewVisibility(R.id.tv_ingredients_title, View.GONE);
                rv.setViewVisibility(R.id.iv_ingredients_separator, View.GONE);
                rv.setViewVisibility(R.id.lv_ingredients, View.GONE);
                rv.setEmptyView(R.id.lv_ingredients, R.id.empty_view);
            } else {
                // TODO include the real mRecipe
                // Create an Intent to launch the app or the RecipeActivity when clicked
                setOnClickIntent = new Intent(context, RecipeActivity.class);
                setOnClickIntent.putExtra(Const.EXTRA_RECIPE_POS, mRecipe);
                Log.d(LOG_TAG, "provider onUpdate, SET EXTRAS TO " + mRecipe);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, setOnClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                rv.setOnClickPendingIntent(R.id.ll_widget, pendingIntent);

                rv.setTextViewText(R.id.tv_recipe_title, MyApp.mRecipesData.get(mRecipe).getName());
                rv.setViewVisibility(R.id.tv_ingredients_title, View.VISIBLE);
                rv.setViewVisibility(R.id.iv_ingredients_separator, View.VISIBLE);
                rv.setViewVisibility(R.id.lv_ingredients, View.VISIBLE);
                rv.setViewVisibility(R.id.empty_view, View.GONE);


                Log.d(LOG_TAG, "provider onUpdate, setRemoteAdapter");

                // Set up the RemoteViews object to use a RemoteViews adapter.
                // This adapter connects
                // to a RemoteViewsService  through the specified intent.
                // This is how you populate the data.
                rv.setRemoteAdapter(R.id.lv_ingredients, setListviewRemoteViewsIntent);

                // template to handle the click listener for each item
                Intent clickIntentTemplate = new Intent(context, RecipeActivity.class);
                PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                        .addNextIntentWithParentStack(clickIntentTemplate)
                        .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                //rv.setPendingIntentTemplate(R.id.lv_ingredients, clickPendingIntentTemplate);

                //setting an empty view in case of no data
                rv.setEmptyView(R.id.lv_ingredients, R.id.empty_view);
            }

            //
            // Do additional processing specific to this app widget...
            //
            //Log.d(LOG_TAG, "provider onUpdate, updateAppWidget");
            appWidgetManager.updateAppWidget(appWidgetId, rv);

            //updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, IngredientsWidgetProvider.class));
//
//        Log.d(LOG_TAG, "no of widgets: " + appWidgetIds.length);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, IngredientsWidgetProvider.class));
        context.sendBroadcast(intent);
    }
}

