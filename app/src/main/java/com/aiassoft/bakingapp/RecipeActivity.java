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
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.aiassoft.bakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gvrynios on 28/04/18.
 */

public class RecipeActivity extends AppCompatActivity
        implements MethodStepsListAdapter.MethodStepsAdapterOnClickHandler {

    private static final String LOG_TAG = MyApp.APP_TAG + RecipeActivity.class.getSimpleName();

    /**
     * Identifies the incoming parameter of the recipe pos
     */
    public static final String EXTRA_RECIPE_POS = "array_pos";

    /**
     * Identifies the save instance parameter of the recipe pos
     */
    public static final String STATE_RECIPE_POS = "array_pos";

    /**
     * If there is not a recipepos, this pos will as the default one
     */
    private static final int DEFAULT_POS = -1;

    private static Context mContext = null;
    private static Recipe mRecipe = null;
    private static int mRecipePos = DEFAULT_POS;

    /** The Ingredients List Adapter */
    private IngredientsListAdapter mIngredientsListAdapter;

    /** The Method Steps Adapter */
    private MethodStepsListAdapter mMethodStepsListAdapter;

    /** The views in the xml file */
    /** The Ingredients recycler view */
    @BindView(R.id.rv_ingredients) RecyclerView mIngredientsRecyclerView;

    /** The Method Steps recycler view */
    @BindView(R.id.rv_method_steps) RecyclerView mMethodStepsRecyclerView;

    /**
     * Creates the recipe activity
     *
     * @param savedInstanceState The saved state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        Log.d(LOG_TAG, " :: onCreate");

        // recovering the instance state
        if (savedInstanceState != null) {
            mRecipePos = savedInstanceState.getInt(STATE_RECIPE_POS, DEFAULT_POS);
        } else {

            /**
             * should be called from another activity. if not, show error toast and return
             * Actually closeOnError doesn't stop the code workflow and we get the error
             * ArrayIndexOutOfBoundsException
             */
            Intent intent = getIntent();
            if (intent == null) {
                closeOnError();
            } else {

                /** Intent parameter should be a valid recipe pos. if not, show error toast and return */
                mRecipePos = intent.getIntExtra(EXTRA_RECIPE_POS, DEFAULT_POS);
            }
        }

        if (mRecipePos == DEFAULT_POS) {
            // STATE_RECIPE_POS / EXTRA_RECIPE_POS not found in state / intent's parameter
            closeOnError();
        } else {

            initializeActivity();
            populateRecipeData();
        }
    }

    /**
     * https://developer.android.com/guide/components/activities/activity-lifecycle#saras
     *
     * This callback is called only when there is a saved instance that is previously saved by using
     * onSaveInstanceState(). We restore some state in onCreate(), while we can optionally restore
     * other state here, possibly usable after onStart() has completed.
     * The savedInstanceState Bundle is same as the one used in onCreate().
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(LOG_TAG, " :: onRestoreInstanceState");
    }

    /** invoked when the activity may be temporarily destroyed, save the instance state here */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, " :: onSaveInstanceState");
        outState.putInt(STATE_RECIPE_POS, mRecipePos);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    private void initializeActivity() {
        mRecipe = MyApp.mRecipesData.get(mRecipePos);

        setTitle(mRecipe.getName());

        /**
         *  Initialize Ingredients Section
         */
        LinearLayoutManager linearLayoutVideoManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

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

        /**
         *  Initialize Method Steps Section
         */
        LinearLayoutManager linearLayoutMethodStepsManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mMethodStepsRecyclerView.setLayoutManager(linearLayoutMethodStepsManager);
        mMethodStepsRecyclerView.setHasFixedSize(true);

        /**
         * The mMethodStepsListAdapter is responsible for linking our recipe's method steps data
         * with the Views that will end up displaying our method steps' data.
         */
        mMethodStepsListAdapter = new MethodStepsListAdapter(this);

        /** Setting the adapter attaches it to the RecyclerView in our layout. */
        mMethodStepsRecyclerView.setAdapter(mMethodStepsListAdapter);
    }

    private void populateRecipeData() {
        mIngredientsListAdapter.setIngredientsData(mRecipe.getIngredients());
        mMethodStepsListAdapter.setMethodStepsData(mRecipe.getSteps());
    }

    private void closeOnError() {
        String err = this.getString(R.string.activity_error_message_missing_extras, RecipeActivity.class.getSimpleName());
        Log.e(LOG_TAG, err);
        Toast.makeText(this, err, Toast.LENGTH_LONG).show();
        super.finish();
        //super.onBackPressed();
        // fragment getActivity().onBackPressed();
    }


    /**
     * This method is for responding to clicks from our list.
     *
     * @param stepPosition the Position from the selected step
     */
    @Override
    public void onClick(int stepPosition) {
        /** Prepare to call the step activity, to show the step's details */
        Intent intent = new Intent(this, StepActivity.class);
        intent.putExtra(StepActivity.EXTRA_RECIPE_POS, mRecipePos);
        intent.putExtra(StepActivity.EXTRA_STEP_POS, stepPosition);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return true;
    }
}
