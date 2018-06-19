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

public class StepsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_main);
        Context mContext = getApplicationContext();
        int mRecipeId;

        if (findViewById(R.id.details_container_fragment) != null) {
            StepsDetailsFragment stepsDetailsFragment = new StepsDetailsFragment();
            // Add the fragment to its container using a FragmentManager and a Transaction
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.details_container_fragment, stepsDetailsFragment)
                    .commit();
        }
        SharedPreferences mSharedPreferences;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Intent intent = getIntent();
        String mRecipeName;
        if (intent.hasExtra(Constants.INT_KEY)) {
            mRecipeId = intent.getIntExtra(Constants.INT_KEY, 0);
            mRecipeName = intent.getStringExtra(Constants.RECIPE_NAME);
            mSharedPreferences.edit().putInt(Constants.INT_RECIPE, mRecipeId)
                    .putString(Constants.RECIPE_NAME, mRecipeName)
                    .apply();
        } else {
            mRecipeName = mSharedPreferences.getString(Constants.RECIPE_NAME, getString(R.string.recipe_name));
        }
        setTitle(mRecipeName);
    }
}