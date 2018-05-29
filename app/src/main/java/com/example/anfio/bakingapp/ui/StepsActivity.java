package com.example.anfio.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.anfio.bakingapp.R;
import com.example.anfio.bakingapp.utilities.Constants;

public class StepsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.steps_main);

        Intent intent = getIntent();
        int key = 0;
        if (intent.hasExtra(Constants.INT_KEY)) {
            key = intent.getIntExtra(Constants.INT_KEY, 0);
        }

        // Add the fragment to its container using a FragmentManager and a Transaction
        FragmentManager fragmentManager = getSupportFragmentManager();
        StepMasterFragment stepMasterFragment = new StepMasterFragment();
        stepMasterFragment.setRecipeId(key);
        fragmentManager.beginTransaction()
                .add(R.id.steps_master, stepMasterFragment)
                .commit();
    }
}