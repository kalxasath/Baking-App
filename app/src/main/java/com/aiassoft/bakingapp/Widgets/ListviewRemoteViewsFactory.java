package com.aiassoft.bakingapp.Widgets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.aiassoft.bakingapp.Const;
import com.aiassoft.bakingapp.MyApp;
import com.aiassoft.bakingapp.R;
import com.aiassoft.bakingapp.activities.RecipeActivity;
import com.aiassoft.bakingapp.model.Ingredient;

import java.util.List;

/**
 * Created by gvryn on 20/05/18.
 */

public class ListviewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String LOG_TAG = MyApp.APP_TAG + ListviewRemoteViewsFactory.class.getSimpleName();

    Context mContext;

    List<Ingredient> mIngredientsData;

    int mRecipe;

    public ListviewRemoteViewsFactory(Context applicationContext, Intent intent) {

        Log.d(LOG_TAG, "factory ListviewRemoteViewsFactory");

        mContext = applicationContext;
    }

    /** Initialize the data */
    @Override
    public void onCreate() {

        Log.d(LOG_TAG, "factory onCreate");

        // TODO read the appropriated receipt from the preferences
        mRecipe = 1;
    }

    /** called on start and when notifyAppWidgetViewDataChanged is called */
    @Override
    public void onDataSetChanged() {
        mIngredientsData = null;

        Log.d(LOG_TAG, "factory onDataSetChanged = " + mRecipe);

        if (MyApp.mRecipesData == null) {
            Log.d(LOG_TAG, "factory onDataSetChanged RETURN NULL");
            return;
        }

        mIngredientsData = MyApp.mRecipesData.get(mRecipe).getIngredients();

        Log.d(LOG_TAG, "factory onDataSetChanged AFTER");
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mIngredientsData == null) {
            //Log.d(LOG_TAG, "factory getCount = 0");
            return 0;
        }

        //Log.d(LOG_TAG, "factory getCount = " + mIngredientsData.size());

        return mIngredientsData.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (getCount() == 0)
            return null;


        //Log.d(LOG_TAG, "factory getViewAt = " + position);

        Ingredient ingredient = mIngredientsData.get(position);

        // position will always range from 0 to getCount() - 1.
        // Construct a RemoteViews item based on the app widget item XML file, and set the
        // text based on the position.
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_widget_list_item);

        // feed row
        rv.setTextViewText(R.id.tv_name, ingredient.getIngredient());
        rv.setTextViewText(R.id.tv_quantity, mContext.getString(R.string.ingredient_quantity
                , ""+ingredient.getQuantity(), ingredient.getMeasure()));
        // end feed row

        // Next, set a fill-intent, which will be used to fill in the pending intent template
        // that is set on the collection view in ListViewWidgetProvider.

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(Const.EXTRA_RECIPE_POS, mRecipe);

        // Make it possible to distinguish the individual on-click
        // action of a given item
        rv.setOnClickFillInIntent(R.id.ll_ingredient_row, fillInIntent);

        // Return the RemoteViews object.
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
