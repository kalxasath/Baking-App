package com.aiassoft.bakingapp.utilities;

import android.content.SharedPreferences;

import com.aiassoft.bakingapp.Const;
import com.aiassoft.bakingapp.MyApp;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by gvryn on 25/05/18.
 */

public class PrefUtils {
    private static final String LOG_TAG = MyApp.APP_TAG + PrefUtils.class.getSimpleName();

    public static void setWidgetRecipePosition(int widgetId, int recipePosition) {
        SharedPreferences sp = MyApp.getContext().getSharedPreferences(Const.PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(Const.WIDGET_RECIPE_ID + widgetId, recipePosition);
        ed.apply();
    }

    public static int getWidgetRecipePosition(int widgetId) {
        SharedPreferences sp = MyApp.getContext().getSharedPreferences(Const.PREFS_NAME, MODE_PRIVATE);
        return sp.getInt(Const.WIDGET_RECIPE_ID + widgetId, Const.INVALID_INT);
    }

}
