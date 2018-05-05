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

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.aiassoft.bakingapp.model.Recipe;
import com.aiassoft.bakingapp.model.Step;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gvryn on 04/05/18.
 */

public class StepActivity extends AppCompatActivity implements ExoPlayer.EventListener {

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

    private SimpleExoPlayer mExoPlayer;
    @BindView(R.id.sepv_player_view) SimpleExoPlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;

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

//        mPlayerView.setDefaultArtwork();
        // Initialize the Media Session.
        initializeMediaSession();

        // Initialize the player.
        initializePlayer(Uri.parse(mStep.getVideoURL()));
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

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

    /**
     * Shows Media Style notification, with actions that depend on the current MediaSession
     * PlaybackState.
     * @param state The PlaybackState of the MediaSession.
     */
//    private void showNotification(PlaybackStateCompat state) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//
//        int icon;
//        String play_pause;
//        if(state.getState() == PlaybackStateCompat.STATE_PLAYING){
//            icon = R.drawable.exo_controls_pause;
//            play_pause = getString(R.string.pause);
//        } else {
//            icon = R.drawable.exo_controls_play;
//            play_pause = getString(R.string.play);
//        }
//
//
//        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
//                icon, play_pause,
//                MediaButtonReceiver.buildMediaButtonPendingIntent(this,
//                        PlaybackStateCompat.ACTION_PLAY_PAUSE));
//
//        NotificationCompat.Action restartAction = new android.support.v4.app.NotificationCompat
//                .Action(R.drawable.exo_controls_previous, getString(R.string.restart),
//                MediaButtonReceiver.buildMediaButtonPendingIntent
//                        (this, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));
//
//        PendingIntent contentPendingIntent = PendingIntent.getActivity
//                (this, 0, new Intent(this, QuizActivity.class), 0);
//
//        builder.setContentTitle(getString(R.string.guess))
//                .setContentText(getString(R.string.notification_text))
//                .setContentIntent(contentPendingIntent)
//                .setSmallIcon(R.drawable.ic_music_note)
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                .addAction(restartAction)
//                .addAction(playPauseAction)
//                .setStyle(new NotificationCompat.MediaStyle()
//                        .setMediaSession(mMediaSession.getSessionToken())
//                        .setShowActionsInCompactView(0,1));
//
//
//        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        mNotificationManager.notify(0, builder.build());
//    }


    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(this, "AiasBakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        mNotificationManager.cancelAll();
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
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


    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mMediaSession.setActive(false);
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
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
//        showNotification(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {
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
            mExoPlayer.seekTo(0);
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
