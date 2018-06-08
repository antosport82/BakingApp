package com.example.anfio.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.anfio.bakingapp.R;
import com.example.anfio.bakingapp.adapters.IngredientsListAdapter;
import com.example.anfio.bakingapp.adapters.StepsListAdapter;
import com.example.anfio.bakingapp.data.RecipeContract;
import com.example.anfio.bakingapp.models.Ingredient;
import com.example.anfio.bakingapp.models.Step;
import com.example.anfio.bakingapp.utilities.Constants;

import java.util.ArrayList;

public class StepsActivity extends AppCompatActivity {

    private static final String TAG = "Antonio StepsA";

    private Context mContext;
    private int mRecipeId;
    private RecyclerView mRvIngredients;
    private RecyclerView mRvSteps;
    private IngredientsListAdapter mIngredientsListAdapter;
    private StepsListAdapter mStepsListAdapter;
    private boolean mTwoPane = false;

    private final LoaderManager.LoaderCallbacks<Cursor> ingredientsLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            String selection = "recipe_id = ?";
            String[] selectionArgs = new String[]{String.valueOf(mRecipeId)};
            return new CursorLoader(mContext, RecipeContract.IngredientEntry.CONTENT_URI_INGREDIENT, null, selection, selectionArgs, null);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            if (data.getCount() != 0) {
                ArrayList<Ingredient> ingredients = cursorToIngredients(data);
                mIngredientsListAdapter.setIngredientData(ingredients);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            mIngredientsListAdapter.setIngredientData(null);
        }
    };
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
                ArrayList<Step> steps = cursorToSteps(data);
                SharedPreferences sharedPreferences;
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                sharedPreferences.edit().putInt(String.valueOf(mRecipeId), steps.size()).apply();
                mStepsListAdapter.setStepData(steps);
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
            Intent intent = new Intent(StepsActivity.this, StepsDetailsActivity.class);
            intent.putExtra(Constants.INT_STEP, id);
            startActivity(intent);
        }
    };

    private ArrayList<Ingredient> cursorToIngredients(Cursor cursor) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            double quantity = cursor.getDouble(cursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_INGREDIENT_QUANTITY));
            String measure = cursor.getString(cursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_INGREDIENT_MEASURE));
            String name = cursor.getString(cursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_INGREDIENT_NAME));
            ingredients.add(new Ingredient(quantity, measure, name));
        }
        return ingredients;
    }

    private ArrayList<Step> cursorToSteps(Cursor cursor) {
        ArrayList<Step> steps = new ArrayList<>();
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            int stepId = cursor.getInt(cursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_STEP_ID));
            String shortDescription = cursor.getString(cursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_STEP_SHORT_DESCRIPTION));
            steps.add(new Step(stepId, shortDescription));
        }
        return steps;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_main);
        mContext = getApplicationContext();

        if (findViewById(R.id.details_container) != null){
            mTwoPane = true;

            StepsDetailsFragment stepsDetailsFragment = new StepsDetailsFragment();
            // Add the fragment to its container using a FragmentManager and a Transaction
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.details_container, stepsDetailsFragment)
                    .commit();
        }
        SharedPreferences mSharedPreferences;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Intent intent = getIntent();
        int key;
        if (intent.hasExtra(Constants.INT_KEY)) {
            key = intent.getIntExtra(Constants.INT_KEY, 0);
            mRecipeId = key;
            mSharedPreferences.edit().putInt(Constants.INT_RECIPE, key).apply();
        } else {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            mRecipeId = mSharedPreferences.getInt(Constants.INT_RECIPE, 0);
        }

        LinearLayoutManager ingredientsLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRvIngredients = findViewById(R.id.rv_ingredients);
        mRvIngredients.setLayoutManager(ingredientsLayoutManager);
        mIngredientsListAdapter = new IngredientsListAdapter(mContext);
        mRvIngredients.setAdapter(mIngredientsListAdapter);
        LinearLayoutManager stepsLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRvSteps = findViewById(R.id.rv_steps);
        mRvSteps.setLayoutManager(stepsLayoutManager);
        mStepsListAdapter = new StepsListAdapter(mContext, clickHandler);
        mRvSteps.setAdapter(mStepsListAdapter);

        getSupportLoaderManager().initLoader(Constants.INGREDIENTS_LOADER, null, ingredientsLoader);
        getSupportLoaderManager().initLoader(Constants.STEPS_MAIN_LOADER, null, stepsLoader);
    }
}