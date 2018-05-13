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
import com.aiassoft.bakingapp.model.Ingredient;
import com.aiassoft.bakingapp.model.Recipe;
import com.aiassoft.bakingapp.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * JSON utilities helper class
 */
public class JsonUtils {
    private static final String LOG_TAG = MyApp.APP_TAG + JsonUtils.class.getSimpleName();

    /**
     *  Dear reviewer kindly I want to inform you that
     *  in this project I prefer to use native java code
     *  to get the JSON data.
     *
     *  Of course in the previous projects, the reviewers gave me a lot
     *  of handy hints such as the use of the library GSon.
     *
     *  Please feel free to suggest me another library like the GSon
     *  Thank you
     */

    /**
     * This method will take a json string as input and use that
     * json data to build a ArrayList of Recipe objects
     * @param json The recipes data in json format as string
     * @return     The ArrayList of Recipe objects
     */
    public static List<Recipe> parseRecipesListJson(String json) {
        //Log.d(LOG_TAG, json);

        /** ArrayList to hold the recipes list items */
        List<Recipe> returnRecipeListItems = new ArrayList<>();
        Recipe recipe;

        /** ArrayList to hold the ingredients list items */
        List<Ingredient> recipeIngredients;
        Ingredient ingredient;

        /** ArrayList to hold the steps list items */
        List<Step> recipeSteps;
        Step step;

        try {
            /** Get the recipes' data array */
            JSONArray arrRecipes = new JSONArray(json);
            for (int i=0; i<arrRecipes.length(); i++) {
                /** Get the recipes's data */
                JSONObject recipeJSON = arrRecipes.getJSONObject(i);

                recipe = new Recipe();
                recipe.setId(recipeJSON.optInt("id"));
                recipe.setName(recipeJSON.optString("name"));
                recipe.setServings(recipeJSON.optInt("servings"));
                recipe.setImage(recipeJSON.optString("image"));

                /** Get the ingredients' data array */
                JSONArray arrIngredients =  recipeJSON.getJSONArray("ingredients");
                recipeIngredients = new ArrayList<>();
                for (int ii=0; ii<arrIngredients.length(); ii++) {
                    /** Get the ingredients's data */
                    JSONObject ingredientJSON = arrIngredients.getJSONObject(ii);

                    ingredient = new Ingredient();
                    ingredient.setIngredient(ingredientJSON.optString("ingredient"));
                    ingredient.setQuantity(ingredientJSON.optInt("quantity"));
                    ingredient.setMeasure(ingredientJSON.optString("measure"));

                    recipeIngredients.add(ingredient);
                }
                recipe.setIngredients(recipeIngredients);

                /** Get the steps' data array */
                JSONArray arrSteps =  recipeJSON.getJSONArray("steps");
                recipeSteps = new ArrayList<>();
                for (int ii=0; ii<arrSteps.length(); ii++) {
                    /** Get the steps's data */
                    JSONObject stepJSON = arrSteps.getJSONObject(ii);

                    step = new Step();
                    step.setId(stepJSON.optInt("id"));
                    step.setName(stepJSON.optString("shortDescription"));
                    step.setDescription(stepJSON.optString("description"));
                    step.setVideoURL(stepJSON.optString("videoURL"));
                    step.setThumbnailURL(stepJSON.optString("thumbnailURL"));

                    recipeSteps.add(step);
                }
                recipe.setSteps(recipeSteps);

                returnRecipeListItems.add(recipe);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return returnRecipeListItems;
    }

    public static String jsonFlickrGetPicture(String json) {
        try {
            JSONObject jo = new JSONObject(json);
            JSONArray pictureItems = jo.optJSONArray("items");
            if (pictureItems.length() > 0) {
                Random rand = new Random();
                int pos = rand.nextInt(pictureItems.length());
                JSONObject picture = pictureItems.optJSONObject(pos);
                if (picture != null) {
                    JSONObject media = picture.optJSONObject("media");
                    return media.optString("m");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

}
