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
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * Created by gvrynios on 28/04/18.
 */

public class RecipeActivity extends AppCompatActivity {

    private static final String LOG_TAG = MyApp.APP_TAG + MainActivity.class.getSimpleName();

    private static Context mContext = null;

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

//        /** Intent parameter should be a valid movie id. if not, show error toast and return */
//        mMovieId = intent.getIntExtra(EXTRA_MOVIE_ID, DEFAULT_MOVIE_ID);
//        if (mMovieId == DEFAULT_MOVIE_ID) {
//            // EXTRA_MOVIE_ID not found in intent's parameter
//            closeOnError();
//        }


//        /**
//         *  Initialize Videos Section
//         */
//        LinearLayoutManager linearLayoutVideoManager
//                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//
//        mRecyclerViewVideos.setLayoutManager(linearLayoutVideoManager);
//        mRecyclerViewVideos.setHasFixedSize(true);
//
//        /**
//         * The mMovieVideosListAdapter is responsible for linking our movie's videos data with the Views that
//         * will end up displaying our videos' data.
//         */
//        mMovieVideosListAdapter = new MovieVideosListAdapter(this);
//
//        /* Setting the adapter attaches it to the RecyclerView in our layout. */
//        mRecyclerViewVideos.setAdapter(mMovieVideosListAdapter);
//
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
//         * The mMovieReviewsListAdapter is responsible for linking our movie's reviews data with the Views that
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

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.recipe_activity_error_message, Toast.LENGTH_SHORT).show();
    }


}
