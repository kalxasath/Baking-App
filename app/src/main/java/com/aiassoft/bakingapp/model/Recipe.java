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

import android.util.Log;

import com.aiassoft.bakingapp.MyApp;

import java.util.List;

/**
 * The Recipes List Item Object, holds all the necessary information of a list item
 */
public class Recipe {
    private static final String LOG_TAG = MyApp.APP_TAG + Recipe.class.getSimpleName();

    private int id;
    private String name;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private int servings;
    private String image;

    /**
     * No args constructor for use in serialization
     */
    public Recipe() {
    }

    /**
     * Constructor to initialize all the class fields from the parameters
     * @param id         The Recipe Id
     * @param name The name
     */
    public Recipe(int id, String name, List<Ingredient> ingredients, List<Step> steps, int servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
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

    public List<Ingredient> getIngredients() {
        return ingredients; }

    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }

    public List<Step> getSteps() { return steps; }

    public void setSteps(List<Step> steps) { this.steps = steps; }

    public int getServings() { return  servings; }

    public void setServings(int servings) { this.servings = servings; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

}
