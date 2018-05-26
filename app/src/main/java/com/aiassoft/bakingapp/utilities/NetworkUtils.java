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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.aiassoft.bakingapp.Const;
import com.aiassoft.bakingapp.MyApp;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {
    private static final String LOG_TAG = MyApp.APP_TAG + NetworkUtils.class.getSimpleName();

    /**
     * Builds the URL to download the Pictures' data
     *
     * @param tags The Pictures tags
     * @return     The URL to get the Pictures' data according the tags
     */
    private static URL buildFlickrGetPicturesUrl(String tags, Boolean addExtraTag) {
        Uri builtUri = Uri.parse(Const.FLICKR_IMAGES_URL).buildUpon()
                .appendQueryParameter(Const.FLICKR_PARAM_FORMAT, Const.FLICKR_PARAM_FORMAT_JSON)
                .appendQueryParameter(Const.FLICKR_PARAM_TAGS, (addExtraTag ? Const.FLICKR_PARAM_ADD_TAGS : "") + tags)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //Log.d(LOG_TAG, "Builded GetPictures URL: " + url);

        return url;
    }



    public static String getRandomImage(String tags, Boolean addExtraTag) throws IOException {
        String s = getResponseFromHttpUrl(buildFlickrGetPicturesUrl(tags, addExtraTag));
        /**
         * strip out the function name since in java I don't know how
         * how to call the callback from a string. Any Suggestion?
         */
        s = s.substring(s.indexOf("(") + 1);
        s = s.substring(0, s.length()-1);

        //Log.d(LOG_TAG, "final json: " + s);

        return JsonUtils.jsonFlickrGetPicture(s);
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     *
     * @return TRUE, if we are connected to the internet, otherwise returns FALSE
     */
    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) MyApp.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}