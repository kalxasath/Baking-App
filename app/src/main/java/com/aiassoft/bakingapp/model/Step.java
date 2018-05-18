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

package com.aiassoft.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.aiassoft.bakingapp.MyApp;

/**
 * Created by gvryn on 20/04/18.
 */

public class Step implements Parcelable {
    private static final String LOG_TAG = MyApp.APP_TAG + Recipe.class.getSimpleName();

    private int id;
    private String name;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    /**
     * No args constructor for use in serialization
     */
    public Step() {
    }

    /**
     * Constructor to initialize all the class fields from the parameters
     * @param id            The step id
     * @param name          Step's short description
     * @param description   Step's full description
     * @param videoURL      The video
     * @param thumbnailURL  A image thumbnail
     */
    public Step(int id, String name, String description, String videoURL, String thumbnailURL) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    /**
     * The private constructor gets an parcel object and
     * sets the class fields from the parcel object
     * @param in
     */
    public Step(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.description = in.readString();
        this.videoURL = in.readString();
        this.thumbnailURL = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() { return videoURL; }

    public void setVideoURL(String videoURL) { this.videoURL = videoURL; }

    public String getThumbnailUrl() { return thumbnailURL; }

    public void  setThumbnailURL(String thumbnailURL) { this.thumbnailURL = thumbnailURL; }

    /**
     * Required by Parcelable
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Puts the class fields to the parcel object
     * @param dest a Parcel object
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    /**
     * Receive and decode whatever is inside the parcel
     */
    public final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
