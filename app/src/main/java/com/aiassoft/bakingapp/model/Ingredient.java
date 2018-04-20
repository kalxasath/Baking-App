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

public class Ingredient {
    private static final String LOG_TAG = MyApp.APP_TAG + Recipe.class.getSimpleName();

    private String ingredient;
    private int quantity;
    private String measure;

    /**
     * No args constructor for use in serialization
     */
    public Ingredient() {
    }

    /**
     * Constructor to initialize all the class fields from the parameters
     * @param ingredient The ingredient name
     * @param quantity   The required quantity
     * @param measure    The quantity measure
     */
    public Ingredient(String ingredient, int quantity, String measure) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.measure = measure;
    }

    public String getIngredient() { return ingredient; }

    public void setIngredient(String ingredient) { this.ingredient = ingredient; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getMeasure() { return measure; }

    public void setMeasure() { this.measure = measure; }

}
