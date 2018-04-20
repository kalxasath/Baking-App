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

import android.util.Log;

import com.aiassoft.bakingapp.MyApp;
import com.aiassoft.bakingapp.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * JSON utilities helper class
 */
public class JsonUtils {
    private static final String LOG_TAG = MyApp.APP_TAG + JsonUtils.class.getSimpleName();

    /**
     * This method will take a json string as input and use that
     * json data to build a ArrayList of MoviesListItem objects
     * @param json The Movies List data in json format as string
     * @return     The ArrayList of MoviesListItem objects
     */
    public static List<Recipe> parseRecipesListJson(String json) {
        Log.d(LOG_TAG, json);

        /* ArrayList to hold the movies list items */
        List<Recipe> recipeListItems = new ArrayList<Recipe>();
        Recipe recipe;

        try {
            /** Creates a new JSONObject with name/value mappings from the json string */
            JSONObject moviesData = new JSONObject(json);

            /** Get the movies' data array */
            JSONArray arrResults =  moviesData.getJSONArray("results");

            int maxResults = arrResults.length();
            for (int i=0; i<maxResults; i++) {
                /** Get the movie's data */
                JSONObject movie = arrResults.getJSONObject(i);

                recipe = new Recipe();
                recipe.setId(movie.optInt("id"));
                recipe.setName(movie.optString("poster_path"));

                recipeListItems.add(recipe);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipeListItems;
    }

}
