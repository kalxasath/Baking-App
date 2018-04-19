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
 * The Movies List Item Object, holds all the necessary information of a list item
 */
public class RecipeListItem {
    private static final String LOG_TAG = MyApp.APP_TAG + RecipeListItem.class.getSimpleName();

    private int id;
    private String name;

    /**
     * No args constructor for use in serialization
     */
    public RecipeListItem() {
    }

    /**
     * Constructor to initialize all the class fields from the parameters
     * @param id         The Recipe Id
     * @param name The name
     */
    public RecipeListItem(int id, int page, String name) {
        this.id = id;
        this.name = name;
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

}
