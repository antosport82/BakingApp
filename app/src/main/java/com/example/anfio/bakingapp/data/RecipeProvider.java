package com.example.anfio.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class RecipeProvider extends ContentProvider {

    private static final int CODE_RECIPES = 100;
    private static final int CODE_RECIPES_WITH_ID = 101;
    private static final int CODE_INGREDIENTS = 200;
    private static final int CODE_INGREDIENTS_WITH_ID = 201;
    private static final int CODE_STEPS = 300;
    private static final int CODE_STEPS_WITH_ID = 301;

    private RecipeDbHelper mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RecipeContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        // This URI is content://com.example.anfio.bakingapp/recipes/ */
        matcher.addURI(authority, RecipeContract.PATH_RECIPES, CODE_RECIPES);
        matcher.addURI(authority, RecipeContract.PATH_INGREDIENTS, CODE_INGREDIENTS);
        matcher.addURI(authority, RecipeContract.PATH_STEPS, CODE_STEPS);

        /*
         * This URI would look something like content://com.example.anfio.bakingapp/recipes/3
         * The "/#" signifies to the UriMatcher that if PATH_RECIPES is followed by ANY number,
         * that it should return the CODE_RECIPES_WITH_ID code
         */
        matcher.addURI(authority, RecipeContract.PATH_RECIPES + "/#", CODE_RECIPES_WITH_ID);
        matcher.addURI(authority, RecipeContract.PATH_INGREDIENTS + "/#", CODE_INGREDIENTS_WITH_ID);
        matcher.addURI(authority, RecipeContract.PATH_STEPS + "/#", CODE_STEPS_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mOpenHelper = new RecipeDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;
        /*
         * Here's the switch statement that, given a URI, will determine what kind of request is
         * being made and query the database accordingly.
         */
        switch (sUriMatcher.match(uri)) {

            case CODE_RECIPES:
                cursor = mOpenHelper.getReadableDatabase().query(
                        RecipeContract.RecipeEntry.TABLE_NAME_RECIPES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case CODE_INGREDIENTS:
                cursor = mOpenHelper.getReadableDatabase().query(
                        RecipeContract.IngredientEntry.TABLE_NAME_INGREDIENTS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case CODE_STEPS:
                cursor = mOpenHelper.getReadableDatabase().query(
                        RecipeContract.StepEntry.TABLE_NAME_STEPS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;


            case CODE_RECIPES_WITH_ID:

                String idRecipe = uri.getLastPathSegment();
                String[] selectionArgumentsRecipe = new String[]{idRecipe};

                cursor = mOpenHelper.getReadableDatabase().query(
                        RecipeContract.RecipeEntry.TABLE_NAME_RECIPES,
                        projection,
                        RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " = ? ",
                        selectionArgumentsRecipe,
                        null,
                        null,
                        sortOrder
                );
                break;

            case CODE_INGREDIENTS_WITH_ID:

                String idIngredient = uri.getLastPathSegment();
                String[] selectionArgumentsIngredient = new String[]{idIngredient};

                cursor = mOpenHelper.getReadableDatabase().query(
                        RecipeContract.IngredientEntry.TABLE_NAME_INGREDIENTS,
                        projection,
                        RecipeContract.IngredientEntry._ID + " = ? ",
                        selectionArgumentsIngredient,
                        null,
                        null,
                        sortOrder
                );
                break;

            case CODE_STEPS_WITH_ID:

                String idStep = uri.getLastPathSegment();
                String[] selectionArgumentsStep = new String[]{idStep};

                cursor = mOpenHelper.getReadableDatabase().query(
                        RecipeContract.IngredientEntry.TABLE_NAME_INGREDIENTS,
                        projection,
                        RecipeContract.StepEntry._ID + " = ? ",
                        selectionArgumentsStep,
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the movies directory
        int match = sUriMatcher.match(uri);
        String tableName;
        switch (match) {
            case CODE_RECIPES:
                tableName = RecipeContract.RecipeEntry.TABLE_NAME_RECIPES;
                break;
            case CODE_INGREDIENTS:
                tableName = RecipeContract.IngredientEntry.TABLE_NAME_INGREDIENTS;
                break;
            case CODE_STEPS:
                tableName = RecipeContract.StepEntry.TABLE_NAME_STEPS;
                break;
            default:
                return super.bulkInsert(uri, values);
        }

        // allows for multiple transactions
        db.beginTransaction();

        // keep track of successful inserts
        int numInserted = 0;

        try {
            for (ContentValues value : values) {
                if (value == null) {
                    throw new IllegalArgumentException("Cannot have null content values");
                }
                long _id = -1;

                try {
                    _id = db.insert(tableName,
                            null, value);
                } catch (SQLiteConstraintException e) {
                    e.printStackTrace();
                }
                if (_id != -1) {
                    numInserted++;
                }
            }
            if (numInserted > 0) {
                // If no errors, declare a successful transaction.
                // database will not populate if this is not called
                db.setTransactionSuccessful();
            }
        } finally {
            // all transactions occur at once
            db.endTransaction();
        }
        if (numInserted > 0) {
            // if there was successful insertion, notify the content resolver that there
            // was a change
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numInserted;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}