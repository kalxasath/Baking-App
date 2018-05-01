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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiassoft.bakingapp.model.Ingredient;
import com.aiassoft.bakingapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link IngredientsListAdapter} exposes a list of ingredients to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class IngredientsListAdapter
        extends RecyclerView.Adapter<IngredientsListAdapter.IngredientsAdapterViewHolder> {

    private static final String LOG_TAG = MyApp.APP_TAG
            + IngredientsListAdapter.class.getSimpleName();

    /* This array holds a list of ingredients objects */
    private ArrayList<Ingredient> mIngredientsData = new ArrayList<>();

    /**
     * Creates a IngredientsAdapter
     *
     */
    public IngredientsListAdapter() {
    }

    /**
     * Cache of the children views for a Ingredient list item
     */
    public class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder {

        /* layout bindings */
        @BindView(R.id.ll_ingredient_row) LinearLayout mIngredientRow;
        @BindView(R.id.tv_name) TextView mName;
        @BindView(R.id.tv_quantity) TextView mQuantity;

        public IngredientsAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout.
     * @return A new IngredientVideosAdapterViewHolder that holds the View for each list item
     */
    @Override
    public IngredientsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.ingredient_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup
                , shouldAttachToParentImmediately);
        return new IngredientsAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the Ingredient
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param IngredientsAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                contents of the item at the given position in the data set.
     * @param position                The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(IngredientsAdapterViewHolder IngredientsAdapterViewHolder
            , int position) {
        Ingredient ingredient = mIngredientsData.get(position);

        Context context = IngredientsAdapterViewHolder.mIngredientRow.getContext();

        if (position % 2 == 0) {
            IngredientsAdapterViewHolder.mIngredientRow.setBackgroundColor(
                    context.getResources().getColor(R.color.ingredientsOddLineBackgroundColor));
        }

        IngredientsAdapterViewHolder.mName.setText(ingredient.getIngredient());
        IngredientsAdapterViewHolder.mQuantity.setText(
                context.getString(R.string.ingredient_quantity
                        , ""+ingredient.getQuantity(), ingredient.getMeasure()));

    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our Ingredients list
     */
    @Override
    public int getItemCount() {
        if (null == mIngredientsData) return 0;
        return mIngredientsData.size();
    }

    /**
     * This method is used to set the Ingredient on a IngredientsAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new IngredientsAdapter to display it.
     *
     * @param ingredientsData The new Ingredients data to be displayed.
     */
    public void setIngredientsData(List<Ingredient> ingredientsData) {
        if (ingredientsData == null) return;
        mIngredientsData.addAll(ingredientsData);
        notifyDataSetChanged();
    }

    /**
     * This method is used when we are resetting data
     */
    public void invalidateData() {
        mIngredientsData = new ArrayList<>();
        notifyDataSetChanged();
    }

}
