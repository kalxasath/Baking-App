package com.aiassoft.bakingapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aiassoft.bakingapp.Const;
import com.aiassoft.bakingapp.MyApp;
import com.aiassoft.bakingapp.R;
import com.aiassoft.bakingapp.adapters.IngredientsListAdapter;
import com.aiassoft.bakingapp.model.Recipe;

/**
 * Created by gvryn on 15/05/18.
 */

public class IngredientsFragment extends Fragment {

    private static final String LOG_TAG = MyApp.APP_TAG + IngredientsFragment.class.getSimpleName();

    private static Recipe mRecipe = null;
    private static int mRecipePos = Const.INVALID_INT;

    /** The Ingredients List Adapter */
    private IngredientsListAdapter mIngredientsListAdapter;

    /** The views in the xml file */
    /** The Ingredients recycler view */
    RecyclerView mIngredientsRecyclerView;


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
        /** Load the saved state */
        if (savedInstanceState != null) {
            mRecipePos = savedInstanceState.getInt(Const.STATE_RECIPE_POS, Const.INVALID_INT);
        }

        /** Inflate the layout*/
        View rootView = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);


        mIngredientsRecyclerView = rootView.findViewById(R.id.rv_ingredients);

        /** bind the information to show in the views */
        LinearLayoutManager linearLayoutVideoManager
                = new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false);

        mIngredientsRecyclerView.setLayoutManager(linearLayoutVideoManager);
        mIngredientsRecyclerView.setHasFixedSize(false);

        /**
         * The mIngredientsListAdapter is responsible for linking our recipe's ingredients data with the Views that
         * will end up displaying our ingredients' data.
         */
        mIngredientsListAdapter = new IngredientsListAdapter();

        /** Setting the adapter attaches it to the RecyclerView in our layout. */
        mIngredientsRecyclerView.setAdapter(mIngredientsListAdapter);

        mIngredientsListAdapter.invalidateData();
        //TODO PASS THE RECIPE ID TO GET THE DATA
        if (mRecipePos != Const.INVALID_INT) {
            mRecipe = MyApp.mRecipesData.get(mRecipePos);
            mIngredientsListAdapter.setIngredientsData(mRecipe.getIngredients());
        }

        /** return the rootView */
        return rootView;
    }

    /** invoked when the activity may be temporarily destroyed, save the instance state here */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(Const.STATE_RECIPE_POS, mRecipePos);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    public void setRecipePos(int recipePos) {
        this.mRecipePos = recipePos;
    }
}
