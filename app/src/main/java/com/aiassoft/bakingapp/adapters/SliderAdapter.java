package com.aiassoft.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiassoft.bakingapp.R;
import com.aiassoft.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gvryn on 09/05/18.
 */

public class SliderAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;

    /* This array holds a list of Method Steps objects */
    private ArrayList<Step> mMethodStepsData = new ArrayList<>();

    @BindView(R.id.tv_recipe_step_instruction) TextView mRecipeStepInstruction;

    public SliderAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if (null == mMethodStepsData) return 0;
        return mMethodStepsData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

        View view = mLayoutInflater.inflate(R.layout.slide_step, container, false);

        ButterKnife.bind(this, view);

        mRecipeStepInstruction.setText(mMethodStepsData.get(position).getDescription());

        Log.d("SliderAdapter", "instantiateItem at: " + position + ", o:"+ view.toString());

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d("SliderAdapter", "destroyItem at: " + position + ", o:"+ object.toString());
        container.removeView((LinearLayout) object);
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


}
