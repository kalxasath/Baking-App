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

import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.aiassoft.bakingapp.model.Recipe;
import com.aiassoft.bakingapp.utilities.JsonUtils;
import com.aiassoft.bakingapp.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Recipe>> {

    private static final String LOG_TAG = MyApp.APP_TAG + MainActivity.class.getSimpleName();

    /**
     * Used to identify the WEB URL that is being used in the loader.loadInBackground
     * to get the movies' data from themoviedb.org
     */
    private static final String LOADER_EXTRA = "web_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* We will check if we are connected to the internet */
        if (! NetworkUtils.isOnline()) {
            /* We are not connected, show the Error Block
             * with the propriety error message
             */
            //showErrorMessage(R.string.error_check_your_network_connectivity);
            Toast.makeText(this, R.string.error_check_your_network_connectivity , LENGTH_SHORT).show();
            finish();
        } else {
            /* Otherwise fetch recipes' data from the internet */
            fetchRecipesList();
        }
    } // onCreate

    /**
     * Fetch the recipes' data from fixed URL
     */
    private void fetchRecipesList() {
        /* Create a bundle to pass the web url to the loader */
        Bundle loaderArgs = new Bundle();
        loaderArgs.putString(LOADER_EXTRA, Const.RECIPE_LIST_URL);

        /*
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<Recipe>> theRecipesListLoader = loaderManager.getLoader(Const.RECIPES_LOADER_ID);

        if (theRecipesListLoader == null) {
            loaderManager.initLoader(Const.RECIPES_LOADER_ID, loaderArgs, this);
        } else {
            loaderManager.restartLoader(Const.RECIPES_LOADER_ID, loaderArgs, this);
        }

    } // fetchRecipesList

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id The ID whose loader is to be created.
     * @param loaderArgs The WEB URL for fetching the movies' data.
     *
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<List<Recipe>>(this) {

            /* This Recipe array will hold and help cache our movies list data */
            List<Recipe> mCachedRecipeListData = null;

            /**
             * Subclasses of AsyncTaskLoader must implement this to take care of loading their data.
             */
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (loaderArgs == null) {
                    return;
                }

                if (mCachedRecipeListData != null) {
                    deliverResult(mCachedRecipeListData);
                } else {
                    /**
                    / * If Error Message Block is visible, hide it * /
                    if (mErrorMessageBlock.getVisibility() == View.VISIBLE) {
                        showMoviesListView();
                    }
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    */
                    forceLoad();
                }
            } // onStartLoading

            /**
             * This is the method of the AsyncTaskLoader that will load and parse the JSON data
             * from themoviedb.org in the background.
             *
             * @return Movies' data from themoviedb.org as a List of MoviesReviewsListItem.
             *         null if an error occurs
             */
            @Override
            public List<Recipe> loadInBackground() {

                // Get the access type argument that is passed on loader initialization */
                String recipeListUrl = loaderArgs.getString(LOADER_EXTRA);
                if (recipeListUrl == null || TextUtils.isEmpty(recipeListUrl)) {
                    /* If null or empty string is passed, return immediately */
                    return null;
                }

                /** try to fetch the data from the network */
                try {
                    URL getTheRecipesUrl = new URL(recipeListUrl);

                    /** if succeed returns a List of MoviesReviewsListItem objects */
                    return JsonUtils.parseRecipesListJson(
                            NetworkUtils.getResponseFromHttpUrl(getTheRecipesUrl));

                } catch (Exception e) {
                    /** If fails returns null to significate the error */
                    e.printStackTrace();
                    return null;
                }

            } // loadInBackground

            /**
             * Sends the result of the load to the registered listener.
             *
             * @param data The result of the load
             */
            public void deliverResult(List<Recipe> data) {
                mCachedRecipeListData = data;
                super.deliverResult(data);
            } // deliverResult

        }; // AsyncTaskLoader

    } // Loader

    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
        // TODO: mpla mpla
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {
        /*
         * We aren't using this method in this application, but we are required to Override
         * it to implement the LoaderCallbacks<List<MoviesReviewsListItem>> interface
         */
    }


}
