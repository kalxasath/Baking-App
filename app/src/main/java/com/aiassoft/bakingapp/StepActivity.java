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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.aiassoft.bakingapp.model.Recipe;
import com.aiassoft.bakingapp.model.Step;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by gvryn on 04/05/18.
 */

public class StepActivity extends AppCompatActivity {

    private static final String LOG_TAG = MyApp.APP_TAG + StepActivity.class.getSimpleName();

    /**
     * Identifies the incoming parameter of the recipe pos
     */
    public static final String EXTRA_RECIPE_POS = "recipe_pos";

    /**
     * Identifies the incoming parameter of the step pos
     */
    public static final String EXTRA_STEP_POS = "step_pos";

    /**
     * If there is not a pos, this pos will as the default one
     */
    private static final int DEFAULT_POS = -1;

    private static Context mContext = null;
    private static Recipe mRecipe = null;
    private static List<Step> mSteps = null;
    private static Step mStep = null;
    private static int mRecipePos = DEFAULT_POS;
    private static int mStepPos = DEFAULT_POS;

    private static Toolbar mToolbar;

    /**
     * Creates the step activity
     *
     * @param savedInstanceState The saved state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

//        ActionBar actionBar = this.getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }

        /** should be called from another activity. if not, show error toast and return */
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        } else {

            /**
             * should be called from another activity. if not, show error toast and return
             * Actually closeOnError doesn't stop the code workflow and we get the error
             * ArrayIndexOutOfBoundsException
             */
            mRecipePos = intent.getIntExtra(EXTRA_RECIPE_POS, DEFAULT_POS);
            mStepPos = intent.getIntExtra(EXTRA_STEP_POS, DEFAULT_POS);
        }

        if (mRecipePos == DEFAULT_POS || mStepPos == DEFAULT_POS) {
            // EXTRA_RECIPE_POS or EXTRA_STEP_POS not found in intent's parameter
            closeOnError();
        } else {
            initializeActivity();
        }
    }

    private void initializeActivity() {
        mRecipe = MyApp.mRecipesData.get(mRecipePos);
        mSteps = mRecipe.getSteps();
        mStep = mSteps.get(mStepPos);

        setTitle(mRecipe.getName());
    }

    private void closeOnError() {
        String err = this.getString(R.string.activity_error_message_missing_extras, StepActivity.class.getSimpleName());
        Log.e(LOG_TAG, err);
        Toast.makeText(this, err, Toast.LENGTH_LONG).show();
        finish();
    }

    /**
     * Notes for me:
     * It's very important to catch the back button and call the finish
     * otherwise the previous activity is recreated without parameters
     * both Intent and savedInstanceState are NULL
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return true;
    }


}
