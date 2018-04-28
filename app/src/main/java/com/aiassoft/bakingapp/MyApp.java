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

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;

import com.aiassoft.bakingapp.model.Recipe;

import java.util.ArrayList;

/**
 * Created by gvryn on 19/04/2018.
 * This application class is used to store static resources
 * being used in static methods
 */
public class MyApp extends Application {
    // static resources
    private static MyApp mInstance;

    public static final String APP_TAG = "Baking_App: ";

    /* This array holds a list of recipe objects */
    public static ArrayList<Recipe> mRecipesData = new ArrayList<>();


    /**
     * Initialize the static resources
     */
    public static void initResources() {
    }

    /**
     * @return the application instance
     */
    public static MyApp getInstance() {
        return mInstance;
    }

    /**
     * @return the application context
     */
    public static Context getContext(){
        return mInstance.getApplicationContext();
    }

    /**
     * @return true if the app supports the right to left UI. otherwise returns false
     */
    public boolean isRTL() {
        Configuration config = mInstance.getApplicationContext().getResources().getConfiguration();
        return config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    @Override
    public void onCreate() {
        mInstance = this;
        MyApp.initResources();

        super.onCreate();
    }
}
