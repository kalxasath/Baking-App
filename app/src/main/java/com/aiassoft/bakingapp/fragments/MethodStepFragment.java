package com.aiassoft.bakingapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class MethodStepFragment extends Fragment {

    // Mandatory constructor for instantiating the fragment
    public MethodStepFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_method_step, container, false);

        SimpleExoPlayerView mPlayer = rootView.findViewById(R.id.sepv_player);
        ImageView mThumbnail = rootView.findViewById(R.id.iv_image);
        TextView mRecipeStepInstruction = rootView.findViewById(R.id.tv_recipe_step_instruction);

        /** bind the information to show in the views */

        /** return the rootView */
        return rootView;
    }
}
