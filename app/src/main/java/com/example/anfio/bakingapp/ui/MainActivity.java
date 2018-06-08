package com.example.anfio.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.anfio.bakingapp.R;
import com.example.anfio.bakingapp.adapters.RecipesListAdapter;
import com.example.anfio.bakingapp.data.RecipeContract;
import com.example.anfio.bakingapp.loaders.RecipeLoader;
import com.example.anfio.bakingapp.models.Recipe;
import com.example.anfio.bakingapp.utilities.Constants;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Antonio";

    private TextView mErrorMessage;
    private ProgressBar mProgressBar;
    private RecipesListAdapter mRecipesListAdapter;
    private RecyclerView mRecyclerView;
    private Context mContext;

    private final LoaderManager.LoaderCallbacks<ArrayList<Recipe>> recipeLoader = new LoaderManager.LoaderCallbacks<ArrayList<Recipe>>() {
        @NonNull
        @Override
        public Loader<ArrayList<Recipe>> onCreateLoader(int id, @Nullable final Bundle args) {
            mProgressBar.setVisibility(View.VISIBLE);
            String url = "";
            if (args != null) {
                url = args.getString(Constants.JSON_RECIPE_LIST);
            }
            return new RecipeLoader(mContext, url);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (data != null) {
                showMoviesDataView();
                mRecipesListAdapter.setRecipeData(data);
            } else {
                showErrorMessage(getString(R.string.error_message));
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<ArrayList<Recipe>> loader) {
            mRecipesListAdapter.setRecipeData(null);
        }
    };
    private final LoaderManager.LoaderCallbacks<Cursor> cursorLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            mProgressBar.setVisibility(View.VISIBLE);
            return new CursorLoader(mContext, RecipeContract.RecipeEntry.CONTENT_URI_RECIPE, null, null, null, null);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (data.getCount() != 0) {
                showMoviesDataView();
                ArrayList<Recipe> recipes = cursorToRecipes(data);
                mRecipesListAdapter.setRecipeData(recipes);
            } else {
                showErrorMessage(getString(R.string.error_message));
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            mRecipesListAdapter.setRecipeData(null);
        }
    };
    private final RecipesListAdapter.RecipeAdapterOnClickHandler clickHandler = new RecipesListAdapter.RecipeAdapterOnClickHandler() {
        @Override
        public void onClick(int id) {
            Intent intent = new Intent(MainActivity.this, StepsActivity.class);
            intent.putExtra(Constants.INT_KEY, id);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        mErrorMessage = findViewById(R.id.tv_error_message);
        mProgressBar = findViewById(R.id.pb_loading_indicator);
        mRecipesListAdapter = new RecipesListAdapter(mContext, clickHandler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView = findViewById(R.id.rv_recipes);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mRecipesListAdapter);

        SharedPreferences mSharedPreferences;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Boolean has_data = mSharedPreferences.getBoolean(Constants.HAS_DATA, false);
        if (!has_data){
            mSharedPreferences.edit().putBoolean(Constants.HAS_DATA, true).apply();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.JSON_RECIPE_LIST, Constants.URL);
            getSupportLoaderManager().initLoader(Constants.RECIPE_MAIN_LOADER, bundle, recipeLoader);
        } else {
            getSupportLoaderManager().initLoader(Constants.CURSOR_MAIN_LOADER, null, cursorLoader);
        }
    }

    private ArrayList<Recipe> cursorToRecipes(Cursor cursor) {
        ArrayList<Recipe> recipes = new ArrayList<>();
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID));
            String name = cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME));
            String image = cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_IMAGE));
            recipes.add(new Recipe(id, name, image));
        }
        return recipes;
    }

    private void showMoviesDataView() {
        // movies are visible, error message is hidden
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(String error) {
        // error message is visible, movies are hidden
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setText(error);
        mErrorMessage.setVisibility(View.VISIBLE);
    }
}