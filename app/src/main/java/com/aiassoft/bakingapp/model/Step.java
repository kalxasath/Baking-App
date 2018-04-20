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

import com.aiassoft.bakingapp.MyApp;

/**
 * Created by gvryn on 20/04/18.
 */

public class Step {
    private static final String LOG_TAG = MyApp.APP_TAG + Recipe.class.getSimpleName();

    private int id;
    private String shortDescription;
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
     * @param id               The step id
     * @param shortDescription Step's short description
     * @param description      Step's full description
     * @param videoURL         The video
     * @param thumbnailURL     A image thumbnail
     */
    public Step(int id, String shortDescription, String description, String videoURL, String thumbnailURL) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() { return videoURL; }

    public void setVideoURL(String videoURL) { this.videoURL = videoURL; }

    public String getThumbnailURL() { return thumbnailURL; }

    public void  setThumbnailURL(String thumbnailURL) { this.thumbnailURL = thumbnailURL; }

}
