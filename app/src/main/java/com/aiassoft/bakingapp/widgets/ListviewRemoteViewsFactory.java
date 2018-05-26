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

package com.aiassoft.bakingapp.widgets;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.aiassoft.bakingapp.Const;
import com.aiassoft.bakingapp.MyApp;
import com.aiassoft.bakingapp.R;
import com.aiassoft.bakingapp.model.Ingredient;

import java.util.List;

/**
 * Created by gvryn on 20/05/18.
 */

public class ListviewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String LOG_TAG = MyApp.APP_TAG + ListviewRemoteViewsFactory.class.getSimpleName();

    private Context mContext;

    private List<Ingredient> mIngredientsData;

    private int mRecipePosition;

    public ListviewRemoteViewsFactory(Context applicationContext, Intent intent) {

        mContext = applicationContext;

        mRecipePosition = intent.getIntExtra(Const.EXTRA_RECIPE_POS, Const.INVALID_INT);

        Log.d(LOG_TAG, "factory ListviewRemoteViewsFactory for Id:" + mRecipePosition);

    }

    /** Initialize the data */
    @Override
    public void onCreate() {

        Log.d(LOG_TAG, "factory onCreate");

    }

    /** called on start and when notifyAppWidgetViewDataChanged is called */
    @Override
    public void onDataSetChanged() {
        mIngredientsData = null;

        Log.d(LOG_TAG, "factory onDataSetChanged set Recipes Ingredients for " + mRecipePosition);

        if (! MyApp.hasData() || mRecipePosition == Const.INVALID_INT) {
            Log.d(LOG_TAG, "factory onDataSetChanged NOT INITIALIZED, MyApp.hasData: " + MyApp.hasData() + ", recipePos: " + mRecipePosition);
            return;
        }

        mIngredientsData = MyApp.mRecipesData.get(mRecipePosition).getIngredients();

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
        fillInIntent.putExtra(Const.EXTRA_RECIPE_POS, 2);

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
