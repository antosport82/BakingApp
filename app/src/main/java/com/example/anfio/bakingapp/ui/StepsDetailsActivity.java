package com.example.anfio.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.anfio.bakingapp.R;
import com.example.anfio.bakingapp.utilities.Constants;

public class StepsDetailsActivity extends AppCompatActivity {

    private int mStepId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);
        Context context = getApplicationContext();

        Intent intent = getIntent();
        if (intent.hasExtra(Constants.INT_STEP)) {
            mStepId = intent.getIntExtra(Constants.INT_STEP, 0);
        }
        SharedPreferences mSharedPreferences;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String recipeName = mSharedPreferences.getString(Constants.RECIPE_NAME, getString(R.string.recipe_name));
        setTitle(recipeName);
        StepsDetailsFragment stepsDetailsFragment = new StepsDetailsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        // activity is not two-pane
        stepsDetailsFragment.setPane(false);
        stepsDetailsFragment.setStep(mStepId);
        fragmentManager.beginTransaction()
                .add(R.id.details_container_fragment, stepsDetailsFragment)
                .commit();
    }
}