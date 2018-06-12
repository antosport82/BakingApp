package com.example.anfio.bakingapp.utilities;

import android.database.Cursor;

import com.example.anfio.bakingapp.data.RecipeContract;
import com.example.anfio.bakingapp.models.Ingredient;
import com.example.anfio.bakingapp.models.Recipe;
import com.example.anfio.bakingapp.models.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeJsonUtils {

    public static ArrayList<Recipe> getRecipes(String recipeJsonResponse) throws JSONException {
        ArrayList<Recipe> recipes = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(recipeJsonResponse);
        int recipeSize = jsonArray.length();
        for (int i = 0; i < recipeSize; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int recipeId = jsonObject.optInt("id");
            String recipeName = jsonObject.optString("name");
            int servings = jsonObject.optInt("servings");
            String image;
            switch (recipeId) {
                case 1:
                    image = "nutella_pie";
                    break;
                case 2:
                    image = "brownies";
                    break;
                case 3:
                    image = "yellow_cake";
                    break;
                case 4:
                    image = "cheesecake";
                    break;
                default:
                    image = "nutella_pie";
                    break;
            }
            recipes.add(new Recipe(recipeId, recipeName, servings, image));
        }
        return recipes;
    }

    public static ArrayList<Ingredient> getIngredients(String recipeJsonResponse) throws JSONException {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(recipeJsonResponse);
        int recipeSize = jsonArray.length();
        for (int i = 0; i < recipeSize; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int recipeId = jsonObject.optInt("id");
            JSONArray ingredientsArray = jsonObject.getJSONArray("ingredients");
            int ingredientsSize = ingredientsArray.length();
            for (int j = 0; j < ingredientsSize; j++) {
                JSONObject ingredientsObject = ingredientsArray.getJSONObject(j);
                double quantity = ingredientsObject.optDouble("quantity");
                String measure = ingredientsObject.optString("measure");
                String ingredient = ingredientsObject.optString("ingredient");
                ingredients.add(new Ingredient(quantity, measure, ingredient, recipeId));
            }
        }
        return ingredients;
    }

    public static ArrayList<Step> getSteps(String recipeJsonResponse) throws JSONException {
        ArrayList<Step> steps = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(recipeJsonResponse);
        int recipeSize = jsonArray.length();
        for (int i = 0; i < recipeSize; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int recipeId = jsonObject.optInt("id");
            JSONArray stepsArray = jsonObject.getJSONArray("steps");
            int stepsSize = stepsArray.length();
            for (int k = 0; k < stepsSize; k++) {
                JSONObject stepsObject = stepsArray.getJSONObject(k);
                int id = stepsObject.optInt("id");
                String shortDescription = stepsObject.optString("shortDescription");
                String description = stepsObject.optString("description");
                String videoUrl = stepsObject.optString("videoURL");
                String thumbnailUrl = stepsObject.optString("thumbnailURL");
                steps.add(new Step(id, shortDescription, description, videoUrl, thumbnailUrl, recipeId));
            }
        }
        return steps;
    }

    public static ArrayList<Recipe> cursorToRecipes(Cursor cursor) {
        ArrayList<Recipe> recipes = new ArrayList<>();
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID));
            String name = cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME));
            String image = cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_IMAGE));
            recipes.add(new Recipe(id, name, image));
        }
        return recipes;
    }

    public static ArrayList<Ingredient> cursorToIngredients(Cursor cursor) {
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

    public static ArrayList<Step> cursorToSteps(Cursor cursor) {
        ArrayList<Step> steps = new ArrayList<>();
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            int stepId = cursor.getInt(cursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_STEP_ID));
            String shortDescription = cursor.getString(cursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_STEP_SHORT_DESCRIPTION));
            steps.add(new Step(stepId, shortDescription));
        }
        return steps;
    }
}