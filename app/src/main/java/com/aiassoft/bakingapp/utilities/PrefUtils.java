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

package com.aiassoft.bakingapp.utilities;

import android.content.SharedPreferences;

import com.aiassoft.bakingapp.Const;
import com.aiassoft.bakingapp.MyApp;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by gvryn on 25/05/18.
 */

/** for each widget save the recipe position, so we can populate the ingredients
 * with out to have to ask the user every time
 */
public final class PrefUtils {
    private static final String LOG_TAG = MyApp.APP_TAG + PrefUtils.class.getSimpleName();

    private PrefUtils() {
        throw new AssertionError("No Instances for you!");
    }

    /**
     * Save the recipe position to the widget
     * @param widgetId the widget id
     * @param recipePosition the recipe position
     */
    public static void setWidgetRecipePosition(int widgetId, int recipePosition) {
        SharedPreferences sp = MyApp.getContext().getSharedPreferences(Const.PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(Const.WIDGET_RECIPE_ID + widgetId, recipePosition);
        ed.apply();
    }

    /**
     * return the recipe position for a widget
     * @param widgetId the widget id
     * @return the recipe position
     */
    public static int getWidgetRecipePosition(int widgetId) {
        SharedPreferences sp = MyApp.getContext().getSharedPreferences(Const.PREFS_NAME, MODE_PRIVATE);
        return sp.getInt(Const.WIDGET_RECIPE_ID + widgetId, Const.INVALID_INT);
    }

    /**
     * removes from shared preferences the widget with id
     * @param widgetId the if from the widget to be removed
     */
    public static void rmWidgetRecipePosition(int widgetId) {
        SharedPreferences sp = MyApp.getContext().getSharedPreferences(Const.PREFS_NAME, MODE_PRIVATE);
        sp.edit().remove(Const.WIDGET_RECIPE_ID + widgetId).apply();
    }

    /**
     * clears the whole shared preferences memory for the Const.PREFS_NAME
     */
    public static void clearWidgets() {
        SharedPreferences sp = MyApp.getContext().getSharedPreferences(Const.PREFS_NAME, MODE_PRIVATE);
        sp.edit().clear().apply();
    }

}
