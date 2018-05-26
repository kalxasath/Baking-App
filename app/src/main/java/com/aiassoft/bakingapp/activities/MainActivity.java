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

package com.aiassoft.bakingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aiassoft.bakingapp.Const;
import com.aiassoft.bakingapp.MyApp;
import com.aiassoft.bakingapp.R;
import com.aiassoft.bakingapp.adapters.RecipesListAdapter;
import com.aiassoft.bakingapp.model.Recipe;
import com.aiassoft.bakingapp.utilities.AppUtils;
import com.aiassoft.bakingapp.utilities.JsonUtils;
import com.aiassoft.bakingapp.utilities.NetworkUtils;

import com.aiassoft.bakingapp.widgets.IngredientsWidgetProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity
        implements RecipesListAdapter.RecipesAdapterOnClickHandler,
        LoaderCallbacks<List<Recipe>> {

    private static final String LOG_TAG = MyApp.APP_TAG + MainActivity.class.getSimpleName();

    /* The Recipes List Adapter */
    private RecipesListAdapter mRecipesListAdapter;

    private static Context mContext;

    /** The views in the xml file */
    /** The recycler view */
    @BindView(R.id.rv_recipes) RecyclerView mRecyclerView;

    /** The Error Message Block,
     * is used to display errors and will be hidden if there are no error
     */
    @BindView(R.id.ll_error_message) LinearLayout mErrorMessageBlock;

    /** The view holding the error message */
    @BindView(R.id.tv_error_message_text) TextView mErrorMessageText;

    /**
     * The ProgressBar that will indicate to the user that we are loading data.
     * It will be hidden when no data is loading.
     */
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApp.setDataInitialized(false);

        mContext = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /*
         * The gridLayoutManager is responsible for measuring and positioning item views within a
         * RecyclerView into a grid.
         */
        GridLayoutManager gridLayoutManager;
        if (AppUtils.isTablet()) {
            gridLayoutManager = new GridLayoutManager(this, 3);
        } else {
            gridLayoutManager = new GridLayoutManager(this, 1);
        }

        /* setLayoutManager associates the gridLayoutManager with our RecyclerView */
        mRecyclerView.setLayoutManager(gridLayoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);

        /*
         * The RecipesListAdapter is responsible for linking our recipes' data with the Views that
         * will end up displaying our recipe data.
         */
        mRecipesListAdapter = new RecipesListAdapter(this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mRecipesListAdapter);

        /* We will check if we are connected to the internet */
        if (! NetworkUtils.isOnline()) {
            /* We are not connected, show the Error Block
             * with the propriety error message
             */
            //showErrorMessage(R.string.error_check_your_network_connectivity);
            Toast.makeText(this, R.string.error_check_your_network_connectivity , LENGTH_SHORT).show();
            finish();
            return;
        }

        /* Otherwise fetch recipes' data from the internet */
        fetchRecipesList();
    } // onCreate

    /**
     * Fetch the recipes' data from fixed URL
     */
    private void fetchRecipesList() {
        /* Create a bundle to pass the web url to the loader */
        Bundle loaderArgs = new Bundle();
        loaderArgs.putString(Const.LOADER_EXTRA, Const.RECIPE_LIST_URL);

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

                    /** If Error Message Block is visible, hide it */
                    if (mErrorMessageBlock.getVisibility() == View.VISIBLE) {
                        showRecipesListView();
                    }
                    mLoadingIndicator.setVisibility(View.VISIBLE);

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
                String recipeListUrl = loaderArgs.getString(Const.LOADER_EXTRA);
                if (recipeListUrl == null || TextUtils.isEmpty(recipeListUrl)) {
                    /* If null or empty string is passed, return immediately */
                    return null;
                }

                /** try to fetch the data from the network */
                try {
                    URL getTheRecipesUrl = new URL(recipeListUrl);

                    /** if succeed returns a List of MoviesReviewsListItem objects */
                    List<Recipe> recipes = JsonUtils.parseRecipesListJson(
                            NetworkUtils.getResponseFromHttpUrl(getTheRecipesUrl));

                    if (Const.MULTIPLY_THE_RECIPES) {
                        /** ONLY FOR THE TEST PHASE, ADD MORE RECIPES */
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        for(int i=1; i<=Const.RECIPES_MULTIPLIER; i++) {
                            List<Recipe> cloneRecipes = new ArrayList<>();
                            for(Recipe recipe: recipes) {
                                Recipe r = gson.fromJson(gson.toJson(recipe), Recipe.class);
                                cloneRecipes.add(r);
                            }
                            recipes.addAll(cloneRecipes);
                        }
                    }

                    if (Const.RECIPES_ADD_RANDOM_IMAGE_IF_NOT_EXIST) {
                        for(Recipe recipe: recipes) {
                            if (recipe.getImage().isEmpty())
                                recipe.setImage(NetworkUtils.getRandomImage(recipe.getName(), true));
                        }
                        /** try another time */
                        for(Recipe recipe: recipes) {
                            if (recipe.getImage().isEmpty())
                                recipe.setImage(NetworkUtils.getRandomImage(recipe.getName(), false));
                        }
                    }

                    return recipes;

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
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        /** reset previously loaded data, if*/
        mRecipesListAdapter.invalidateData();

        /** Update the adapters data with the new one */
        mRecipesListAdapter.setRecipesData(data);

        /** Check for error */
        if (null == data) {
            /** If an error has occurred, show the error message */
            showErrorMessage(R.string.unexpected_fetch_error);
        } else {
            IngredientsWidgetProvider.sendRefreshBroadcast(mContext);
            /** Else show the recipes list */
            showRecipesListView();
        }
    } // onLoadFinished

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {
        /**
         * We aren't using this method in this application, but we are required to Override
         * it to implement the LoaderCallbacks<List<MoviesReviewsListItem>> interface
         */
    }

    /**
     * This method is used when we need to reset the data
     */
    private void invalidateData() {
        mRecipesListAdapter.invalidateData();
    }

    /**
     * This method is for responding to clicks from our list.
     *
     * @param recipePosition the Position from the selected recipe
     */
    @Override
    public void onClick(int recipePosition) {
        /** Prepare to call the detail activity, to show the recipe's details */
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(Const.EXTRA_RECIPE_POS, recipePosition);
        startActivity(intent);
    }

    /**
     * This method will make the View for the movies list visible and
     * hides the error message block.
     */
    private void showRecipesListView() {
        /** First, make sure the error block is invisible */
        mErrorMessageBlock.setVisibility(View.INVISIBLE);
        /** Then, make sure the recipes list is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    } // showMoviesListView

    /**
     * This method will make the error message visible,
     * populate the error message with the corresponding error message block,
     * and hides the recipe details.
     * @param errorId The error message string id
     */
    private void showErrorMessage(int errorId) {
        /** First, hide the currently visible recipes list */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /** Then, show the error block */
        mErrorMessageBlock.setVisibility(View.VISIBLE);
        /** Show the corresponding error message */
        mErrorMessageText.setText(getString(errorId));
    } // showErrorMessage

    /**
     * Called when a tap occurs in the refresh button
     * @param view The view which reacted to the click
     */
    public void onRefreshButtonClick(View view) {
        /** Again check if we are connected to the internet */
        if (NetworkUtils.isOnline()) {
            /** If the network connectivity is restored
             * show the recipes List to hide the error block, and
             * fetch recipes' data from the internet
             */
            showRecipesListView();
            fetchRecipesList();
        }
    } // onRefreshButtonClick

}
