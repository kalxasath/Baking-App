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

package com.aiassoft.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiassoft.bakingapp.MyApp;
import com.aiassoft.bakingapp.R;
import com.aiassoft.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecipesListAdapter} exposes a list of recipes to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class RecipesListAdapter extends RecyclerView.Adapter<RecipesListAdapter.RecipesAdapterViewHolder> {

    private static final String LOG_TAG = MyApp.APP_TAG + RecipesListAdapter.class.getSimpleName();

    /**
     * Defining an on-click handler to make it easy for an Activity
     * to interface with the RecyclerView
     */
    private final RecipesAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives OnClick messages
     */
    public interface RecipesAdapterOnClickHandler {
        void onClick(int recipeId);
    }

    /**
     * Creates a RecipesAdapter
     *
     * @param clickHandler The on-click handler for this adapter. This single handler
     *                     is called when an item is clicked
     */
    public RecipesListAdapter(RecipesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    /**
     * Cache of the children views for a Recipe list item
     */
    public class RecipesAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        /* layout bindings */
        @BindView(R.id.iv_recipe_image) ImageView mRecipeImage;
        @BindView(R.id.tv_name) TextView mName;
        @BindView(R.id.tv_servings) TextView mServings;

        public RecipesAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            mRecipeImage.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            //int selectedRecipe = MyApp.mRecipesData.get(adapterPosition).getId();
            mClickHandler.onClick(adapterPosition);
        }
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout.
     * @return A new RecipeVideosAdapterViewHolder that holds the View for each list item
     */
    @Override
    public RecipesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecipesAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the recipe
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param RecipesAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                contents of the item at the given position in the data set.
     * @param position                The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(RecipesAdapterViewHolder RecipesAdapterViewHolder, int position) {
        Recipe recipe = MyApp.mRecipesData.get(position);

        RecipesAdapterViewHolder.mName.setText(recipe.getName());
        RecipesAdapterViewHolder.mServings.setText(
                RecipesAdapterViewHolder.mServings.getContext().getString(R.string.recipe_servings)
                + " " + recipe.getServings());

        if (recipe.getImage().isEmpty()) {
            Picasso.with(RecipesAdapterViewHolder.mRecipeImage.getContext())
                    .load(R.drawable.no_recipe_image_available)
                    .placeholder(R.drawable.no_recipe_image_available)
                    .into(RecipesAdapterViewHolder.mRecipeImage);
        } else {
            Picasso.with(RecipesAdapterViewHolder.mRecipeImage.getContext())
                    .load(recipe.getImage())
                    .placeholder(R.drawable.no_recipe_image_available)
                    .into(RecipesAdapterViewHolder.mRecipeImage);
        }

    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our RecipeIds list
     */
    @Override
    public int getItemCount() {
        if (null == MyApp.mRecipesData) return 0;
        return MyApp.mRecipesData.size();
    }

    /**
     * This method is used to set the recipe on a RecipesAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new RecipesAdapter to display it.
     *
     * @param recipesData The new recipes data to be displayed.
     */
    public void setRecipesData(List<Recipe> recipesData) {
        if (recipesData == null) return;
        MyApp.mRecipesData.addAll(recipesData);
        MyApp.setDataInitialized(true);
        notifyDataSetChanged();
    }

    /**
     * This method is used when we are resetting data
     */
    public void invalidateData() {
        MyApp.mRecipesData = new ArrayList<Recipe>();
        MyApp.setDataInitialized(false);
        notifyDataSetChanged();
    }

}
