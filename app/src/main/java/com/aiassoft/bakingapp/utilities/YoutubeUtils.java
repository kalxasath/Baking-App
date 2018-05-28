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

package com.aiassoft.bakingapp.utilities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.aiassoft.bakingapp.Const;
import com.aiassoft.bakingapp.MyApp;
import com.aiassoft.bakingapp.R;

/**
 * These utilities will be used to play youtube videos.
 */
public final class YoutubeUtils {

    private static final String LOG_TAG = MyApp.APP_TAG + YoutubeUtils.class.getSimpleName();

    private YoutubeUtils() {
        throw new AssertionError("No Instances for you!");
    }

    /**
     * Builds the URL to watch the Youtube video
     * over web browser
     *
     * @param key The Video key, to be watched
     * @return    The URI to watch the video
     */
    public static Uri buildHttpYoutubeWatchVideoUri(String key) {
        Uri builtUri = Uri.parse(Const.YOUTUBE_WATCH_URL
        ).buildUpon()
                .appendQueryParameter(Const.YOUTUBE_PARAM_WATCH_VIDEO, key)
                .build();
        return builtUri;
    }

    /**
     * Builds the URL to watch the Youtube video
     * over the youtube app
     *
     * @param key The Video key, to be watched
     * @return    The URI to watch the video
     */
    public static Uri buildAppYoutubeWatchVideoUri(String key) {
        Uri builtUri = Uri.parse(Const.YOUTUBE_APP_URI + key).buildUpon().build();

        return builtUri;
    }

    /**
     * Play the video via the youtube app, if the app doesn't exists
     * it will play it via the web browser
     * @param context the context to be used to start the activity
     * @param key     the video key / id
     */
    public static void watchYoutubeVideo(Context context, String key){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, buildAppYoutubeWatchVideoUri(key));
        if (appIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(appIntent);
        } else {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, buildHttpYoutubeWatchVideoUri(key));
            if (webIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(webIntent);
            } else {
                Toast.makeText(context, context.getString(R.string.youtube_player_not_found), Toast.LENGTH_SHORT).show();
            }
        }
    }


}