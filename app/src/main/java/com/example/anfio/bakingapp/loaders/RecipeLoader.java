package com.example.anfio.bakingapp.loaders;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.anfio.bakingapp.data.RecipeContract;
import com.example.anfio.bakingapp.models.Ingredient;
import com.example.anfio.bakingapp.models.Recipe;
import com.example.anfio.bakingapp.models.Step;
import com.example.anfio.bakingapp.utilities.NetworkUtils;
import com.example.anfio.bakingapp.utilities.RecipeJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RecipeLoader extends AsyncTaskLoader<ArrayList<Recipe>> {

    private final String mUrl;
    private ArrayList<Recipe> recipeData;

    public RecipeLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (recipeData != null) {
            deliverResult(recipeData);
        } else {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public ArrayList<Recipe> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        try {
            URL recipeRequestUrl = new URL(mUrl);
            // get json response in a string
            String jsonRecipeResponse = NetworkUtils
                    .getResponseFromHttpUrl(recipeRequestUrl);
            // get data and insert it into DB
            recipeData = RecipeJsonUtils.getRecipes(jsonRecipeResponse);
            ArrayList<Ingredient> ingredientData = RecipeJsonUtils.getIngredients(jsonRecipeResponse);
            ArrayList<Step> stepData = RecipeJsonUtils.getSteps(jsonRecipeResponse);
            insertRecipesIntoDb(recipeData);
            insertIngredientsIntoDb(ingredientData);
            insertStepsIntoDb(stepData);
            return RecipeJsonUtils.getRecipes(jsonRecipeResponse);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(@Nullable ArrayList<Recipe> data) {
        super.deliverResult(data);
        recipeData = data;
    }

    private void insertRecipesIntoDb(ArrayList<Recipe> recipes) {
        ContentValues[] cvRecipes = new ContentValues[recipes.size()];
        for (int i = 0; i < recipes.size(); i++) {
            int id = recipes.get(i).getId();
            String name = recipes.get(i).getRecipeName();
            int servings = recipes.get(i).getServings();
            String image = recipes.get(i).getImage();
            cvRecipes[i] = new ContentValues();
            cvRecipes[i].put(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID, id);
            cvRecipes[i].put(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME, name);
            cvRecipes[i].put(RecipeContract.RecipeEntry.COLUMN_SERVINGS, servings);
            cvRecipes[i].put(RecipeContract.RecipeEntry.COLUMN_IMAGE, image);
        }
        // Insert new recipes data via a ContentResolver
        getContext().getContentResolver().bulkInsert(RecipeContract.RecipeEntry.CONTENT_URI_RECIPE, cvRecipes);
    }

    private void insertIngredientsIntoDb(ArrayList<Ingredient> ingredients) {
        ContentValues[] cvIngredients = new ContentValues[ingredients.size()];
        for (int i = 0; i < ingredients.size(); i++) {
            double quantity = ingredients.get(i).getQuantity();
            String measure = ingredients.get(i).getMeasure();
            String ingredientName = ingredients.get(i).getIngredientName();
            int idRecipe = ingredients.get(i).getIdRecipe();
            cvIngredients[i] = new ContentValues();
            cvIngredients[i].put(RecipeContract.IngredientEntry.COLUMN_INGREDIENT_QUANTITY, quantity);
            cvIngredients[i].put(RecipeContract.IngredientEntry.COLUMN_INGREDIENT_MEASURE, measure);
            cvIngredients[i].put(RecipeContract.IngredientEntry.COLUMN_INGREDIENT_NAME, ingredientName);
            cvIngredients[i].put(RecipeContract.IngredientEntry.COLUMN_RECIPE_ID, idRecipe);
        }
        // Insert new ingredients data via a ContentResolver
        getContext().getContentResolver().bulkInsert(RecipeContract.IngredientEntry.CONTENT_URI_INGREDIENT, cvIngredients);
    }

    private void insertStepsIntoDb(ArrayList<Step> steps) {
        ContentValues[] cvSteps = new ContentValues[steps.size()];
        for (int i = 0; i < steps.size(); i++) {
            int id = steps.get(i).getId();
            String shortDescription = steps.get(i).getShortDescription();
            String description = steps.get(i).getDescription();
            String videoUrl = steps.get(i).getVideoUrl();
            String thumbnailUrl = steps.get(i).getThumbnailUrl();
            int idRecipe = steps.get(i).getIdRecipe();
            cvSteps[i] = new ContentValues();
            cvSteps[i].put(RecipeContract.StepEntry.COLUMN_STEP_ID, id);
            cvSteps[i].put(RecipeContract.StepEntry.COLUMN_STEP_SHORT_DESCRIPTION, shortDescription);
            cvSteps[i].put(RecipeContract.StepEntry.COLUMN_STEP_DESCRIPTION, description);
            cvSteps[i].put(RecipeContract.StepEntry.COLUMN_STEP_VIDEO_URL, videoUrl);
            cvSteps[i].put(RecipeContract.StepEntry.COLUMN_STEP_THUMBNAIL_URL, thumbnailUrl);
            cvSteps[i].put(RecipeContract.StepEntry.COLUMN_RECIPE_ID, idRecipe);
        }
        // Insert new steps data via a ContentResolver
        getContext().getContentResolver().bulkInsert(RecipeContract.StepEntry.CONTENT_URI_STEP, cvSteps);
    }
}