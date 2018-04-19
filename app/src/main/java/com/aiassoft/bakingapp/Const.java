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

import android.net.Uri;

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
     * themoviedb.org URI definitions
     */
    public static final String THE_RECIPE_LIST_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    /**
     * youtube definitions
     */
    public static final String YOUTUBE_APP_URI = "vnd.youtube:";
    public static final String YOUTUBE_WATCH_URL = "https://www.youtube.com/watch";
    public static final String YOUTUBE_PARAM_WATCH_VIDEO = "v";

}
