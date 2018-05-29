package com.example.anfio.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 1;

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_RECIPES_TABLE =
                "CREATE TABLE " + RecipeContract.RecipeEntry.TABLE_NAME_RECIPES + " (" +
                        RecipeContract.RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                        RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                        RecipeContract.RecipeEntry.COLUMN_SERVINGS + " INTEGER NOT NULL, " +
                        RecipeContract.RecipeEntry.COLUMN_IMAGE + " TEXT, " +
                        " UNIQUE (" + RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + ") ON CONFLICT REPLACE" +
                        ");";

        db.execSQL(SQL_CREATE_RECIPES_TABLE);

        final String SQL_CREATE_INGREDIENTS_TABLE =
                "CREATE TABLE " + RecipeContract.IngredientEntry.TABLE_NAME_INGREDIENTS + " (" +
                        RecipeContract.IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RecipeContract.IngredientEntry.COLUMN_INGREDIENT_QUANTITY + " REAL NOT NULL, " +
                        RecipeContract.IngredientEntry.COLUMN_INGREDIENT_MEASURE + " TEXT NOT NULL, " +
                        RecipeContract.IngredientEntry.COLUMN_INGREDIENT_NAME + " INTEGER NOT NULL, " +
                        RecipeContract.IngredientEntry.COLUMN_RECIPE_ID + " INTEGER, " +
                        " FOREIGN KEY(" + RecipeContract.IngredientEntry.COLUMN_RECIPE_ID + ") REFERENCES " +
                        RecipeContract.RecipeEntry.TABLE_NAME_RECIPES + "(" + RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + ")" +
                        ");";

        db.execSQL(SQL_CREATE_INGREDIENTS_TABLE);

        final String SQL_CREATE_STEPS_TABLE =
                "CREATE TABLE " + RecipeContract.StepEntry.TABLE_NAME_STEPS + " (" +
                        RecipeContract.StepEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RecipeContract.StepEntry.COLUMN_STEP_ID + " INTEGER NOT NULL, " +
                        RecipeContract.StepEntry.COLUMN_STEP_SHORT_DESCRIPTION + " TEXT NOT NULL, " +
                        RecipeContract.StepEntry.COLUMN_STEP_DESCRIPTION + " TEXT NOT NULL, " +
                        RecipeContract.StepEntry.COLUMN_STEP_VIDEO_URL + " TEXT NOT NULL, " +
                        RecipeContract.StepEntry.COLUMN_STEP_THUMBNAIL_URL + " TEXT NOT NULL, " +
                        RecipeContract.StepEntry.COLUMN_RECIPE_ID + " INTEGER, " +
                        " FOREIGN KEY(" + RecipeContract.StepEntry.COLUMN_RECIPE_ID + ") REFERENCES " +
                        RecipeContract.RecipeEntry.TABLE_NAME_RECIPES + "(" + RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + ")" +
                        ");";

        db.execSQL(SQL_CREATE_STEPS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeEntry.TABLE_NAME_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + RecipeContract.IngredientEntry.TABLE_NAME_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + RecipeContract.StepEntry.TABLE_NAME_STEPS);
        onCreate(db);
    }
}