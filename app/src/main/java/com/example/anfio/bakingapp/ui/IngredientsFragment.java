package com.example.anfio.bakingapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anfio.bakingapp.R;
import com.example.anfio.bakingapp.adapters.IngredientsListAdapter;
import com.example.anfio.bakingapp.data.RecipeContract;
import com.example.anfio.bakingapp.models.Ingredient;
import com.example.anfio.bakingapp.utilities.Constants;
import com.example.anfio.bakingapp.utilities.RecipeJsonUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class IngredientsFragment extends Fragment {

    private Context mContext;
    private int mRecipeId;
    @BindView(R.id.rv_ingredients)
    RecyclerView mRvIngredients;
    private IngredientsListAdapter mIngredientsListAdapter;
    private Unbinder unbinder;

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
                ArrayList<Ingredient> ingredients = RecipeJsonUtils.cursorToIngredients(data);
                mIngredientsListAdapter.setIngredientData(ingredients);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            mIngredientsListAdapter.setIngredientData(null);
        }
    };

    public IngredientsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        LinearLayoutManager ingredientsLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRvIngredients = rootView.findViewById(R.id.rv_ingredients);
        mRvIngredients.setLayoutManager(ingredientsLayoutManager);
        mIngredientsListAdapter = new IngredientsListAdapter(mContext);
        mRvIngredients.setAdapter(mIngredientsListAdapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // get recipe id and start the loader to load the ingredients of that recipe
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mRecipeId = sharedPreferences.getInt(Constants.INT_RECIPE, 0);
        getLoaderManager().initLoader(Constants.INGREDIENTS_LOADER, null, ingredientsLoader);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}