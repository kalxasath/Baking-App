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

package com.aiassoft.bakingapp;

/*
 * Defining a class of constants to use them from any where
 *
 */
public class Const {
    /**
     * We never need to create an instance of the Const class
     * because the Const is simply a class filled,
     * with App related constants that are all static.
     */
    private Const() {}

    /**
     * Set to TRUE if you want to multiply the recipes
     */
    public static final boolean MULTIPLY_THE_RECIPES = false;
    /**
     *  large multiplier increases the starting time because all the recipes are pictureless
     *  and the find picture procedure is not optimized
     */
    public static final int RECIPES_MULTIPLIER = 0;
    /**
     * Add a random picture from flickr for each recipe
     */
    public static final boolean RECIPES_ADD_RANDOM_IMAGE_IF_NOT_EXIST = false;

    /**
     * Used to identify the WEB URL that is being used in the loader.loadInBackground
     * to get the recipes' data
     */
    public static final String LOADER_EXTRA = "web_url";

    /**
     * Identifies the Intent incoming parameter of the recipe pos
     */
    public static final String EXTRA_RECIPE_POS = "EXTRA_RECIPE_POS";
    public static final String EXTRA_STEP_POS = "EXTRA_STEP_POS";

    /**
     * Identifies the save instance parameters
     */
    public static final String STATE_RECIPE_POS = "STATE_RECIPE_POS";
    public static final String STATE_STEP_POS = "STATE_STEP_POS";

    /**
     * Identifies the save instance parameter of the recipe pos on the recycler view
     */
    public static final String STATE_METHODS_STEP_RECYCLER = "STATE_METHODS_STEP_RECYCLER";
    public static final String STATE_METHODS_STEP = "STATE_METHODS_STEP";
    public static final String STATE_CURRENT_STEP_POSITION = "STATE_CURRENT_STEP_POSITION";

    /**
     * Indicates a not initialized integer POS or STEP
     */
    public static final int INVALID_INT = -1;




    /**
     * This ID will be used to identify the Loader responsible for loading our recipes list.
     */
    public static final int RECIPES_LOADER_ID = 0;

    /**
     * recipes list URI definitions
     */
    public static final String RECIPE_LIST_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    /**
     * random images from flickr
     */
    public static final String FLICKR_IMAGES_URL = "https://api.flickr.com/services/feeds/photos_public.gne";
    public static final String FLICKR_PARAM_TAGS = "tags";
    public static final String FLICKR_PARAM_FORMAT = "format";
    public static final String FLICKR_PARAM_FORMAT_JSON = "json";
    public static final String FLICKR_PARAM_ADD_TAGS = "Recipe ";


    /**
     * youtube definitions
     */
    public static final String YOUTUBE_APP_URI = "vnd.youtube:";
    public static final String YOUTUBE_WATCH_URL = "https://www.youtube.com/watch";
    public static final String YOUTUBE_PARAM_WATCH_VIDEO = "v";

}
