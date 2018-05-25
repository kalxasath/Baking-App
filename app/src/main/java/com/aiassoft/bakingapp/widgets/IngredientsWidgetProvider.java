package com.aiassoft.bakingapp.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.aiassoft.bakingapp.MyApp;
import com.aiassoft.bakingapp.services.IngredientsWidgetUpdateService;

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

        Log.d(LOG_TAG, "provider UPDATEAppWidget id: " + appWidgetId);
/*
        Intent serviceIntent = new Intent(context, IngredientsWidgetUpdateService.class);
        serviceIntent.putExtra(Const.EXTRA_WIDGET_ID, appWidgetId);
        Log.d(LOG_TAG, "provider START SERVICE");
        context.startService(serviceIntent);
*/
        /*
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);
        views.setTextViewText(R.id.tv_recipe_title, "Makaronada me kima");

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        */
    }

    /** Every widget action is performed via the OnReceive like APPWIDGET_ENABLED, APPWIDGET_UPDATE */
    //@Override
    public void onReceive_(Context context, Intent intent) {
        Log.d(LOG_TAG, "provider onReceive, received: " + intent.getAction());

//        if (intent.getAction().equals(UPDATE_WIDGET)) {
//            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//            int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, IngredientsWidgetProvider.class));
//            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_ingredients);
//        }
        /*
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
        */
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        ComponentName thisWidget = new ComponentName(context, IngredientsWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        // Build the intent to call the service
        Intent intent = new Intent(context.getApplicationContext(), IngredientsWidgetUpdateService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

        Log.d(LOG_TAG, "provider onUpdate START SERVICE");
        // Update the widgets via the service
        context.startService(intent);

        /**
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Log.d(LOG_TAG, "provider onUpdate id: " + appWidgetId);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        */


        /**
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
                // Create an Intent to launch the app or the RecipeActivity when clicked
                setOnClickIntent = new Intent(context, RecipeActivity.class);
                setOnClickIntent.putExtra(Const.EXTRA_RECIPE_POS, mRecipe);
                setOnClickIntent.setAction("foo");
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
                //clickIntentTemplate.putExtra(Const.EXTRA_RECIPE_POS, 2);
                clickIntentTemplate.setAction("foo2");
                PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                        .addNextIntentWithParentStack(clickIntentTemplate)
                        .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                rv.setPendingIntentTemplate(R.id.lv_ingredients, clickPendingIntentTemplate);

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
        */
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, IngredientsWidgetProvider.class));
//
        Log.d(LOG_TAG, "onEnabled");
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void sendRefreshBroadcast(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, IngredientsWidgetProvider.class));
        Log.d(LOG_TAG, "sendRefreshBroadcast, number of widgets to refresh: " + appWidgetIds.length);
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        intent.setComponent(new ComponentName(context, IngredientsWidgetProvider.class));
        context.sendBroadcast(intent);
    }

}



/**
 public class DemoWidget extends AppWidgetProvider {

 static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
 int appWidgetId) {

 CharSequence widgetText = context.getString(R.string.appwidget_text);
 // Construct the RemoteViews object
 RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.demo_widget);
 views.setTextViewText(R.id.appwidget_text, widgetText);

 // Instruct the widget manager to update the widget
 appWidgetManager.updateAppWidget(appWidgetId, views);
 }

 @Override
 public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
 // There may be multiple widgets active, so update all of them
 for (int appWidgetId : appWidgetIds) {
 updateAppWidget(context, appWidgetManager, appWidgetId);
 }
 }

 @Override
 public void onEnabled(Context context) {
 // Enter relevant functionality for when the first widget is created
 }

 @Override
 public void onDisabled(Context context) {
 // Enter relevant functionality for when the last widget is disabled
 }
 }


 */