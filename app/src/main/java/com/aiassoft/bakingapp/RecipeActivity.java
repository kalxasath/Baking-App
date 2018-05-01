/*
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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.aiassoft.bakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gvrynios on 28/04/18.
 */

public class RecipeActivity extends AppCompatActivity {

    private static final String LOG_TAG = MyApp.APP_TAG + MainActivity.class.getSimpleName();

    /**
     * Identifies the incoming parameter of the recipe id
     */
    public static final String EXTRA_ARRAY_POS = "array_pos";

    /**
     * If there is not a recipe id, this id will as the default one
     */
    private static final int DEFAULT_RECIPE_POS = -1;

    private static Context mContext = null;
    private static Recipe mRecipe = null;
    private static int mRecipePos = DEFAULT_RECIPE_POS;

    /* The Ingredients List Adapter */
    private IngredientsListAdapter mIngredientsListAdapter;

    /** The views in the xml file */
    /** The recycler view */
    @BindView(R.id.rv_ingredients) RecyclerView mIngredientsRecyclerView;

    /**
     * Creates the detail activity
     *
     * @param savedInstanceState The saved state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        /** should be called from another activity. if not, show error toast and return */
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        /** Intent parameter should be a valid recipe id. if not, show error toast and return */
        mRecipePos = intent.getIntExtra(EXTRA_ARRAY_POS, DEFAULT_RECIPE_POS);
        if (mRecipePos == DEFAULT_RECIPE_POS) {
            // EXTRA_RECIPE_ID not found in intent's parameter
            closeOnError();
        }

        mRecipe = MyApp.mRecipesData.get(mRecipePos);

        setTitle(mRecipe.getName());

        /**
         *  Initialize Ingredients Section
         */
        LinearLayoutManager linearLayoutVideoManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {

//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
        };

        mIngredientsRecyclerView.setLayoutManager(linearLayoutVideoManager);
        mIngredientsRecyclerView.setHasFixedSize(false);

        /**
         * The mRecipeListAdapter is responsible for linking our recipe's videos data with the Views that
         * will end up displaying our videos' data.
         */
        mIngredientsListAdapter = new IngredientsListAdapter();

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mIngredientsRecyclerView.setAdapter(mIngredientsListAdapter);
        //mIngredientsRecyclerView.setNestedScrollingEnabled(false);
//        /**
//         *  Initialize Reviews Section
//         */
//        LinearLayoutManager linearLayoutReviewsManager
//                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//
//        mRecyclerViewReviews.setLayoutManager(linearLayoutReviewsManager);
//        mRecyclerViewReviews.setHasFixedSize(true);
//
//        /**
//         * The mMovieReviewsListAdapter is responsible for linking our recipe's reviews data with the Views that
//         * will end up displaying our reviews' data.
//         */
//        mMovieReviewsListAdapter = new MovieReviewsListAdapter();
//
//        /** Setting the adapter attaches it to the RecyclerView in our layout. */
//        mRecyclerViewReviews.setAdapter(mMovieReviewsListAdapter);
//
//        /** We will check if we are connected to the internet */
//        if (!NetworkUtils.isOnline()) {
//            /** We are not connected, show the Error Block
//             *  with the propriety error message
//             */
//            showErrorMessage(R.string.error_check_your_network_connectivity);
//        } else {
//            /** Otherwise fetch movies' data from the internet */
//            fetchMoviesDetails();
//        }
        setIngredients();
    }

    private void setIngredients() {
        mIngredientsListAdapter.setIngredientsData(mRecipe.getIngredients());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.recipe_activity_error_message, Toast.LENGTH_SHORT).show();
    }


}
