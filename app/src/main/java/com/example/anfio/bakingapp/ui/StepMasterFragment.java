package com.example.anfio.bakingapp.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
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

import java.util.ArrayList;

public class StepMasterFragment extends Fragment {

    private Context mContext;
    private int mRecipeId;
    private RecyclerView mRvIngredients;
//    private RecyclerView mRvSteps;
    private IngredientsListAdapter mIngredientsListAdapter;

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

    private ArrayList<Ingredient> cursorToIngredients(Cursor cursor) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        while (cursor.moveToNext()) {
            double quantity = cursor.getDouble(cursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_INGREDIENT_QUANTITY));
            String measure = cursor.getString(cursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_INGREDIENT_MEASURE));
            String name = cursor.getString(cursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_INGREDIENT_NAME));
            ingredients.add(new Ingredient(quantity, measure, name));
        }
        return ingredients;
    }

    public StepMasterFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_step_master, container, false);
        mContext = getContext();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRvIngredients = rootView.findViewById(R.id.rv_ingredients);
        mRvIngredients.setLayoutManager(linearLayoutManager);
//        mRvSteps = rootView.findViewById(R.id.rv_steps);
        mIngredientsListAdapter = new IngredientsListAdapter(mContext);
        mRvIngredients.setAdapter(mIngredientsListAdapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(Constants.INGREDIENTS_LOADER, null, ingredientsLoader);
        super.onActivityCreated(savedInstanceState);
    }

    public void setRecipeId(int index) {
        mRecipeId = index;
    }
}
