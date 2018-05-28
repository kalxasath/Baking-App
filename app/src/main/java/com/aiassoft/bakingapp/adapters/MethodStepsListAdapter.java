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
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiassoft.bakingapp.Const;
import com.aiassoft.bakingapp.MyApp;
import com.aiassoft.bakingapp.R;
import com.aiassoft.bakingapp.model.Step;
import com.aiassoft.bakingapp.utilities.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link MethodStepsListAdapter} exposes a list of Method Steps to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class MethodStepsListAdapter
        extends RecyclerView.Adapter<MethodStepsListAdapter.MethodStepsAdapterViewHolder> {

    private static final String LOG_TAG = MyApp.APP_TAG + MethodStepsListAdapter.class.getSimpleName();

    /* This array holds a list of Method Steps objects */
    private ArrayList<Step> mMethodStepsData = new ArrayList<>();

    private View mCurrentSelectedView;
    private int mSelectedViewPos = Const.INVALID_INT;

    /**
     * Defining an on-click handler to make it easy for an Activity
     * to interface with the RecyclerView
     */
    private final MethodStepsAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives OnClick messages
     */
    public interface MethodStepsAdapterOnClickHandler {
        void onClick(int position);
    }

    /**
     * Creates a MethodStepsAdapter
     *
     */
    public MethodStepsListAdapter(MethodStepsAdapterOnClickHandler clickHandler, int selectedViewPos) {
        mClickHandler = clickHandler;
        mSelectedViewPos = selectedViewPos;
    }

    /**
     * Cache of the children views for a Method Step list item
     */
    public class MethodStepsAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        /** layout bindings */
        @BindView(R.id.ll_method_step_row) LinearLayout mMethodStepRow;
        @BindView(R.id.tv_method_step_name) TextView mMethodStepName;
        @BindView(R.id.tv_method_description) TextView mMethodStepDescription;

        public MethodStepsAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            mSelectedViewPos = Const.INVALID_INT;

            if (mCurrentSelectedView != null)
                mCurrentSelectedView.setSelected(false);

            v.setSelected(true);
            mCurrentSelectedView = v;
            int adapterPosition = getAdapterPosition();
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
     * @return A new IngredientVideosAdapterViewHolder that holds the View for each list item
     */
    @Override
    public MethodStepsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.method_step_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup
                , shouldAttachToParentImmediately);
        return new MethodStepsAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the Method Step
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param MethodStepsAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                contents of the item at the given position in the data set.
     * @param position                The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MethodStepsAdapterViewHolder MethodStepsAdapterViewHolder
            , int position) {
        Step step = mMethodStepsData.get(position);

        Context context = MethodStepsAdapterViewHolder.mMethodStepRow.getContext();

        if (position % 2 == 0) {
            //MethodStepsAdapterViewHolder.mMethodStepRow.setBackgroundColor(
            //        context.getResources().getColor(R.color.ingredientsOddLineBackgroundColor));
            MethodStepsAdapterViewHolder.mMethodStepRow.setBackground(
                    ResourcesCompat.getDrawable(context.getResources(), R.drawable.background_odd_selector, null));
        }

        MethodStepsAdapterViewHolder.mMethodStepName.setText(step.getName());

        String abbreviation;
        String description = step.getDescription();
        if (description.length() >= 35) {
            abbreviation = description.substring(0, 35)+ "...";
        } else {
            abbreviation = description;
        }
        MethodStepsAdapterViewHolder.mMethodStepDescription.setText(abbreviation);

        if (mSelectedViewPos == position) {
            //MethodStepsAdapterViewHolder.mMethodStepRow.setSelected(true);
            mCurrentSelectedView = MethodStepsAdapterViewHolder.mMethodStepRow;
            highlightedSelectedView();
        }
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our Method Steps list
     */
    @Override
    public int getItemCount() {
        if (null == mMethodStepsData) return 0;
        return mMethodStepsData.size();
    }

    /**
     * This method is used to set the Method Step on a IngredientsAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new IngredientsAdapter to display it.
     *
     * @param methodStepsData The new Method Step data to be displayed.
     */
    public void setMethodStepsData(List<Step> methodStepsData) {
        if (methodStepsData == null) return;
        mMethodStepsData.addAll(methodStepsData);
        notifyDataSetChanged();
    }

    /**
     * This method is used when we are resetting data
     */
    public void invalidateData() {
        mMethodStepsData = new ArrayList<>();
        notifyDataSetChanged();
    }

    /**
     * This method is used when want to reset the highlighted view
     */
    public void invalidateSelectedView() {
        if (mCurrentSelectedView != null)
            mCurrentSelectedView.setSelected(false);

        mCurrentSelectedView = null;
    }

    /**
     * This method is used when want to highlighted the selected view
     */
    public void highlightedSelectedView() {
        if (mCurrentSelectedView != null)
            mCurrentSelectedView.setSelected(true);
    }
}
