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

package com.aiassoft.bakingapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiassoft.bakingapp.Const;
import com.aiassoft.bakingapp.MyApp;
import com.aiassoft.bakingapp.R;
import com.aiassoft.bakingapp.adapters.SliderAdapter;
import com.aiassoft.bakingapp.model.Recipe;
import com.aiassoft.bakingapp.model.Step;
import com.aiassoft.bakingapp.utilities.AppUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiassoft.bakingapp.utilities.AppUtils.hideSystemUI;
import static com.aiassoft.bakingapp.utilities.AppUtils.inLandscape;
import static com.aiassoft.bakingapp.utilities.AppUtils.showSystemUI;
import static com.aiassoft.bakingapp.utilities.AppUtils.showToast;

/**
 * Created by gvryn on 04/05/18.
 */

public class StepActivity extends AppCompatActivity implements ExoPlayer.EventListener, View.OnClickListener {

    private static final String LOG_TAG = MyApp.APP_TAG + StepActivity.class.getSimpleName();

    private static Context mContext = null;
    private static Recipe mRecipe = null;
    private static List<Step> mSteps = null;
    private static int mRecipePos = Const.INVALID_INT;
    private static int mStepPos = Const.INVALID_INT;

    private static SimpleExoPlayer mExoPlayer;
    private static long mExoPlayerPosition = 0;
    private static boolean mExoPlayerState = true;

    /** not the best way but a solution with the existing code
     * to keep players state when he device is rotated
     */
    private static boolean mDirtyKeepExoPlayerState = false;

    @BindView(R.id.sepv_player) SimpleExoPlayerView mPlayer;
    @BindView(R.id.iv_image) ImageView mThumbnail;
    ViewPager mSlideViewPager;
    LinearLayout mDotArea;
    Button mBtnPrev;
    Button mBtnNext;

    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private SliderAdapter mSliderAdapter;
    private TextView[] mDots;
    private int mPrevPage = 0;




    /**
     * Creates the step activity
     *
     * @param savedInstanceState The saved state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);

        mDirtyKeepExoPlayerState = false;
        // recovering the instance state
        if (savedInstanceState != null) {

            mRecipePos = savedInstanceState.getInt(Const.STATE_RECIPE_POS, Const.INVALID_INT);
            mStepPos = savedInstanceState.getInt(Const.STATE_STEP_POS, Const.INVALID_INT);
            mExoPlayerPosition = savedInstanceState.getLong(Const.STATE_EXOPLAYER_POS, 0);
            mExoPlayerState = savedInstanceState.getBoolean(Const.STATE_EXOPLAYER_STATE, false);
            mDirtyKeepExoPlayerState = true;

        } else {
            /** should be called from another activity. if not, show error toast and return */
            Intent intent = getIntent();
            if (intent == null) {
                closeOnError();
            } else {

                /**
                 * should be called from another activity. if not, show error toast and return
                 */
                mRecipePos = intent.getIntExtra(Const.EXTRA_RECIPE_POS, Const.INVALID_INT);
                mStepPos = intent.getIntExtra(Const.EXTRA_STEP_POS, Const.INVALID_INT);
            }
        }

        if (mRecipePos == Const.INVALID_INT || mStepPos == Const.INVALID_INT) {
            // EXTRA_RECIPE_POS or EXTRA_STEP_POS not found in intent's parameter
            closeOnError();
        } else {
            initializeActivity();
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
        super.onSaveInstanceState(outState);

        //Log.d(LOG_TAG, " :: onSaveInstanceState, mRecipePos: " + mRecipePos +
        //        ", mStepPos: " + mStepPos + ", mSlideViewPager.getCurrentItem: " + mSlideViewPager.getCurrentItem() +
        //        ", mExoPlayerPosition: " + mExoPlayerPosition + ", getCurrentPosition: " + mExoPlayer.getCurrentPosition());

        if (mSlideViewPager != null)
            mStepPos = mSlideViewPager.getCurrentItem();
        if (mExoPlayer != null) {
            mExoPlayerPosition = mExoPlayer.getCurrentPosition();
            mExoPlayerState = mExoPlayer.getPlayWhenReady();
        }

        outState.putInt(Const.STATE_RECIPE_POS, mRecipePos);
        outState.putInt(Const.STATE_STEP_POS, mStepPos);
        outState.putLong(Const.STATE_EXOPLAYER_POS, mExoPlayerPosition);
        outState.putBoolean(Const.STATE_EXOPLAYER_STATE, mExoPlayerState);

        // call superclass to save any view hierarchy
        //super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "info");
    }

    private void initializeActivity() {
        if (AppUtils.isTablet()) {
            showToast("in Tabled mode");
        }

        mRecipe = MyApp.mRecipesData.get(mRecipePos);
        mSteps = mRecipe.getSteps();

        setTitle(mRecipe.getName());

        //initializeMediaSession();

        if (inLandscape()) {

            hideSystemUI(this);

            //displayMedia(mSteps.get(mStepPos));

        } else {

            showSystemUI(this);

            mSlideViewPager = this.findViewById(R.id.vp_slide_area);
            mDotArea = this.findViewById(R.id.ll_dot_area);
            mBtnPrev = this.findViewById(R.id.bt_prev);
            mBtnNext = this.findViewById(R.id.bt_next);

            addDotsIndicator(mSteps.size());

            mSliderAdapter = new SliderAdapter(this);
            mSliderAdapter.invalidateData();
            mSliderAdapter.setMethodStepsData(mSteps);

            mSlideViewPager.setAdapter(mSliderAdapter);

            mSlideViewPager.addOnPageChangeListener(viewPagerOnPageChangeListener);

            mSlideViewPager.setCurrentItem(mStepPos);

//            setPageIndicator(mSteps.size(), mStepPos);

            mBtnPrev.setOnClickListener(this);
            mBtnNext.setOnClickListener(this);
        }
    }

    private void displayMedia(Step mStep) {
        String videoUrl = mStep.getVideoUrl();
        String thumbnailUrl = mStep.getThumbnailUrl();

        if (! videoUrl.isEmpty()) {
            mThumbnail.setVisibility(View.INVISIBLE);
            mPlayer.setVisibility(View.VISIBLE);

            // Initialize the Media Session.
            //initializeMediaSession();

            // Initialize the player.
            initializePlayer(Uri.parse(videoUrl));

        } else {

            mPlayer.setVisibility(View.INVISIBLE);
            mThumbnail.setVisibility(View.VISIBLE);

            if (!thumbnailUrl.isEmpty()) {
                Picasso.with(this)
                        .load(thumbnailUrl)
                        .placeholder(R.drawable.no_video_available)
                        .error(R.drawable.no_video_available)
                        .into(mThumbnail);
            } else {
                Picasso.with(this)
                        .load(R.drawable.no_video_available)
                        .into(mThumbnail);
            }
        }
    }

    private void addDotsIndicator(int pages) {
        mDots = new TextView[pages];
        mDotArea.removeAllViews();

        for (int i = 0; i < pages; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorStepIndicator));

            mDotArea.addView(mDots[i]);
        }
    }

    private void setPageIndicator(int pages, int page) {
        mDots[mPrevPage].setTextColor(getResources().getColor(R.color.colorStepIndicator));
        mDots[page].setTextColor(getResources().getColor(R.color.colorStepIndicatorCurrent));
        mPrevPage = page;

        if(page == 0) {
            mBtnNext.setEnabled(true);
            mBtnPrev.setEnabled(false);
            mBtnPrev.setText("");
        } else if (page == pages-1) {
            mBtnNext.setEnabled(false);
            mBtnNext.setText("");
            mBtnPrev.setEnabled(true);
        } else {
            mBtnNext.setEnabled(true);
            mBtnPrev.setEnabled(true);
            mBtnNext.setText(getResources().getString(R.string.next));
            mBtnPrev.setText(getResources().getString(R.string.prev));
        }
    }

    private void initializePage(int page) {
        releasePlayer();
        setPageIndicator(mSteps.size(), page);

        if (mDirtyKeepExoPlayerState) {
            mDirtyKeepExoPlayerState = false;
        } else
            mExoPlayerState = true;

        displayMedia(mSteps.get(page));
    }

    ViewPager.OnPageChangeListener viewPagerOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int page) {
            initializePage(page);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {
        if (mMediaSession == null) {
            // Create a MediaSessionCompat.
            mMediaSession = new MediaSessionCompat(this, LOG_TAG);

            // Enable callbacks from MediaButtons and TransportControls.
            mMediaSession.setFlags(
                    MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                            MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

            // Do not let MediaButtons restart the player when the app is not visible.
            mMediaSession.setMediaButtonReceiver(null);

            // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
            mStateBuilder = new PlaybackStateCompat.Builder()
                    .setActions(
                            PlaybackStateCompat.ACTION_PLAY |
                                    PlaybackStateCompat.ACTION_PAUSE |
                                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                    PlaybackStateCompat.ACTION_PLAY_PAUSE);

            mMediaSession.setPlaybackState(mStateBuilder.build());


            // MySessionCallback has methods that handle callbacks from a media controller.
            mMediaSession.setCallback(new MySessionCallback());

            // Start the Media Session since the activity is active.
            mMediaSession.setActive(true);
        }
    }



    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            initializeMediaSession();

            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            mPlayer.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(this, "AiasBakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            playerSeekTo(mExoPlayerPosition);
            mExoPlayer.setPlayWhenReady(mExoPlayerState);
        }
    }

    private void playerSeekTo(long seekToPosition) {
        mExoPlayerPosition = seekToPosition;
        mExoPlayer.seekTo(seekToPosition);
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
        if (mMediaSession != null) {
            mMediaSession.setActive(false);
            mMediaSession = null;
        }
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
     *
     * after a while I found the very important
     * android:launchMode="singleTop" on the AndroidManifest.xml
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

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mExoPlayerPosition = mExoPlayer.getCurrentPosition();
            mExoPlayerState = mExoPlayer.getPlayWhenReady();
            mExoPlayer.setPlayWhenReady(false);
        }
        /**
        if (mMediaSession != null)
            mMediaSession.setActive(false);
        */
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        displayMedia(mSteps.get(mStepPos));
        /*
        if (mExoPlayer != null)
            mExoPlayer.setPlayWhenReady(true);
        if (mMediaSession != null)
            mMediaSession.setActive(true);
        */
    }


    /** ExoPlayer Event Listeners */

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    /**
     * Method that is called when the ExoPlayer state changes. Used to update the MediaSession
     * PlayBackState to keep in sync, and post the media notification.
     * @param playWhenReady true if ExoPlayer is playing, false if it's paused.
     * @param playbackState int describing the state of ExoPlayer. Can be STATE_READY, STATE_IDLE,
     *                      STATE_BUFFERING, or STATE_ENDED.
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if (playbackState == PlaybackStateCompat.STATE_PLAYING) {
            mExoPlayerPosition = mExoPlayer.getCurrentPosition();
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.bt_prev:
                mExoPlayerPosition = 0;
                mSlideViewPager.setCurrentItem(mSlideViewPager.getCurrentItem()-1);
                break;
            case R.id.bt_next:
                mExoPlayerPosition = 0;
                mSlideViewPager.setCurrentItem(mSlideViewPager.getCurrentItem()+1);
                break;
        }
    }


    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            playerSeekTo(0);
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
     */
    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }
}
