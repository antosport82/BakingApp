package com.example.anfio.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anfio.bakingapp.R;
import com.example.anfio.bakingapp.adapters.StepsListAdapter;
import com.example.anfio.bakingapp.data.RecipeContract;
import com.example.anfio.bakingapp.models.Step;
import com.example.anfio.bakingapp.utilities.Constants;
import com.example.anfio.bakingapp.utilities.RecipeJsonUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StepsFragment extends Fragment {

    private Context mContext;
    private int mRecipeId;
    @BindView(R.id.rv_steps)
    RecyclerView mRvSteps;
    private StepsListAdapter mStepsListAdapter;
    private StepsListAdapter mStepsListAdapterTwoPane;
    private boolean mTwoPane;
    private Unbinder unbinder;

    private final LoaderManager.LoaderCallbacks<Cursor> stepsLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            String selection = "recipe_id = ?";
            String[] selectionArgs = new String[]{String.valueOf(mRecipeId)};
            return new CursorLoader(mContext, RecipeContract.StepEntry.CONTENT_URI_STEP, null, selection, selectionArgs, null);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            if (data.getCount() != 0) {
                ArrayList<Step> steps = RecipeJsonUtils.cursorToSteps(data);
                SharedPreferences sharedPreferences;
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                sharedPreferences.edit().putInt(String.valueOf(mRecipeId), steps.size()).apply();
                if (mTwoPane) {
                    mStepsListAdapterTwoPane.setStepData(steps);
                } else {
                    mStepsListAdapter.setStepData(steps);
                }
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            mStepsListAdapter.setStepData(null);
        }
    };
    private final StepsListAdapter.StepAdapterOnClickHandler clickHandler = new StepsListAdapter.StepAdapterOnClickHandler() {
        @Override
        public void onClick(int id) {
            Intent intent = new Intent(getActivity(), StepsDetailsActivity.class);
            intent.putExtra(Constants.INT_STEP, id);
            startActivity(intent);
        }
    };

    private final StepsListAdapter.StepAdapterOnClickHandler clickHandlerTwoPane = new StepsListAdapter.StepAdapterOnClickHandler() {
        @Override
        public void onClick(int id) {
            StepsDetailsFragment stepsDetailsFragment = new StepsDetailsFragment();
            // Add the fragment to its container using a FragmentManager and a Transaction
            if (getActivity() != null) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                stepsDetailsFragment.setStep(id);
                fragmentManager.beginTransaction()
                        .replace(R.id.details_container_fragment, stepsDetailsFragment)
                        .commit();
            }
        }
    };

    public StepsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            mRecipeId = getActivity().getIntent().getIntExtra(Constants.INT_RECIPE, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        View rootView = inflater.inflate(R.layout.fragment_steps_main, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        LinearLayoutManager stepsLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRvSteps.setLayoutManager(stepsLayoutManager);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getContext();
        if (this.getActivity() != null) {
            // check if it is a two-pane layout
            if (this.getActivity().findViewById(R.id.details_container_fragment) != null) {
                mTwoPane = true;
                mStepsListAdapterTwoPane = new StepsListAdapter(mContext, clickHandlerTwoPane);
                mRvSteps.setAdapter(mStepsListAdapterTwoPane);
            } else {
                mTwoPane = false;
                mStepsListAdapter = new StepsListAdapter(mContext, clickHandler);
                mRvSteps.setAdapter(mStepsListAdapter);
            }
        }
        // get recipe id and start the loader to load the steps of that recipe
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mRecipeId = sharedPreferences.getInt(Constants.INT_RECIPE, 0);
        getLoaderManager().initLoader(Constants.STEPS_MAIN_LOADER, null, stepsLoader);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}