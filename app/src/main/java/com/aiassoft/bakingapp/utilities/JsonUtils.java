package com.aiassoft.bakingapp.utilities;

import android.util.Log;

import com.aiassoft.bakingapp.MyApp;
import com.aiassoft.bakingapp.model.RecipeListItem;

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
    public static List<RecipeListItem> parseMoviesListJson(String json) {
        Log.d(LOG_TAG, json);

        /* ArrayList to hold the movies list items */
        List<RecipeListItem> recipeListItems = new ArrayList<RecipeListItem>();
        RecipeListItem recipeListItem;

        try {
            /** Creates a new JSONObject with name/value mappings from the json string */
            JSONObject moviesData = new JSONObject(json);

            /** Get the movies' data array */
            JSONArray arrResults =  moviesData.getJSONArray("results");

            int maxResults = arrResults.length();
            for (int i=0; i<maxResults; i++) {
                /** Get the movie's data */
                JSONObject movie = arrResults.getJSONObject(i);

                recipeListItem = new RecipeListItem();
                recipeListItem.setId(movie.optInt("id"));
                recipeListItem.setName(movie.optString("poster_path"));

                recipeListItems.add(recipeListItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipeListItems;
    }

}
