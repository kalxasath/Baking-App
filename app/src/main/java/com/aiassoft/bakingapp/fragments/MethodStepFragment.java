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

package com.aiassoft.bakingapp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiassoft.bakingapp.Const;
import com.aiassoft.bakingapp.MyApp;
import com.aiassoft.bakingapp.R;
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
import com.squareup.picasso.Picasso;

/**
 * Created by gvryn on 15/05/18.
 */

public class MethodStepFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String LOG_TAG = MyApp.APP_TAG + MethodStepFragment.class.getSimpleName();

    private static Step mRecipeStep = null;

    private static Context mContext;
    private static SimpleExoPlayer mExoPlayer;
    private static long mExoPlayerPosition = 0;
    private static boolean mExoPlayerState = true;
    private static boolean mInvalidateExoPlayerPosition = false;

    private static SimpleExoPlayerView mPlayer;
    private static ImageView mThumbnail;
    private static TextView mRecipeStepInstruction;

    private static MediaSessionCompat mMediaSession;
    private static PlaybackStateCompat.Builder mStateBuilder;

    // Mandatory constructor for instantiating the fragment
    public MethodStepFragment() {
    }

    /**
     * Inflates the fragment layout
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInvalidateExoPlayerPosition = false;
        mExoPlayerPosition = 0;
        mExoPlayerState = true;

        /** Load the saved state */
        if (savedInstanceState != null) {
            mRecipeStep = savedInstanceState.getParcelable(Const.STATE_METHODS_STEP);
            mExoPlayerPosition = savedInstanceState.getLong(Const.STATE_EXOPLAYER_POS, 0);
            mExoPlayerState = savedInstanceState.getBoolean(Const.STATE_EXOPLAYER_STATE, false);
        }

        mContext = container.getContext();

        /** Inflate the layout*/
        View rootView = inflater.inflate(R.layout.fragment_method_step, container, false);

        mPlayer = rootView.findViewById(R.id.sepv_player);
        mThumbnail = rootView.findViewById(R.id.iv_image);
        mRecipeStepInstruction = rootView.findViewById(R.id.tv_recipe_step_instruction);

        /** bind the information to show in the views */
        mRecipeStepInstruction.setText(mRecipeStep.getDescription());
        //displayMedia(mRecipeStep);

        /** return the rootView */
        return rootView;
    }

    /** invoked when the activity may be temporarily destroyed, save the instance state here */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Parcelable recipeStepState = mRecipeStep;

        if (mExoPlayer != null) {
            mExoPlayerPosition = mExoPlayer.getCurrentPosition();
            mExoPlayerState = mExoPlayer.getPlayWhenReady();
        }

        outState.putParcelable(Const.STATE_METHODS_STEP, recipeStepState);
        outState.putLong(Const.STATE_EXOPLAYER_POS, mExoPlayerPosition);
        outState.putBoolean(Const.STATE_EXOPLAYER_STATE, mExoPlayerState);


        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mInvalidateExoPlayerPosition) {
            mExoPlayerPosition = 0;
            mExoPlayerState = true;
            mInvalidateExoPlayerPosition = false;
        } else if (mExoPlayer != null) {
            mExoPlayerPosition = mExoPlayer.getCurrentPosition();
            mExoPlayerState = mExoPlayer.getPlayWhenReady();
        }
        releasePlayer();
        /**
        if (mExoPlayer != null) {
            if (mInvalidateExoPlayerPosition) {
                mExoPlayerPosition = 0;
                mInvalidateExoPlayerPosition = false;
            } else
                mExoPlayerPosition = mExoPlayer.getCurrentPosition();

            mExoPlayer.setPlayWhenReady(false);
        }
        if (mMediaSession != null)
            mMediaSession.setActive(false);
        */
    }

    @Override
    public void onResume() {
        super.onResume();
        displayMedia(mRecipeStep);
        /**
        if (mExoPlayer != null)
            mExoPlayer.setPlayWhenReady(true);
        if (mMediaSession != null)
            mMediaSession.setActive(true);
        */
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
                Picasso.with(mContext)
                        .load(thumbnailUrl)
                        .placeholder(R.drawable.no_video_available)
                        .error(R.drawable.no_video_available)
                        .into(mThumbnail);
            } else {
                Picasso.with(mContext)
                        .load(R.drawable.no_video_available)
                        .into(mThumbnail);
            }
        }
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {
        if (mMediaSession == null) {
            // Create a MediaSessionCompat.
            mMediaSession = new MediaSessionCompat(mContext, LOG_TAG);

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
            mMediaSession.setCallback(new MethodStepFragment.MySessionCallback());

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
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);
            mPlayer.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(mContext, "AiasBakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    mContext, userAgent), new DefaultExtractorsFactory(), null, null);
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

    public void setRecipeStep(Step recipeStep) {
        mRecipeStep = recipeStep;
        // OnPause should invalidate the current player position
        mInvalidateExoPlayerPosition = true;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

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
