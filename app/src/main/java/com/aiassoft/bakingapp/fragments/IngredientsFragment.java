package com.aiassoft.bakingapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiassoft.bakingapp.R;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

/**
 * Created by gvryn on 15/05/18.
 */

public class IngredientsFragment extends Fragment {

    // Mandatory constructor for instantiating the fragment
    public IngredientsFragment() {
    }

    /**
     * Inflates the fragment layout
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /** Inflate the layout*/
        View rootView = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);


        RecyclerView rvIngredients = rootView.findViewById(R.id.rv_ingredients);

        /** bind the information to show in the views */

        /** return the rootView */
        return rootView;
    }
}
