package com.example.anfio.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class RecipeContract {

    public static final String CONTENT_AUTHORITY = "com.example.anfio.bakingapp";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_RECIPES = "recipes";
    public static final String PATH_INGREDIENTS = "ingredients";
    public static final String PATH_STEPS = "steps";

    public static final class RecipeEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the Recipes table from the content provider */

        public static final Uri CONTENT_URI_RECIPE = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_RECIPES)
                .build();

        public static final String TABLE_NAME_RECIPES = "recipes";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_RECIPE_NAME = "recipe_name";
        public static final String COLUMN_SERVINGS = "servings";
        public static final String COLUMN_IMAGE = "image";

        /**
         * Builds a URI that adds the recipe id to the end of the recipe content URI path.
         * This is used to query details about a single recipe entry by id.
         *
         * @param id Id of the recipe
         * @return Uri to query details about a single recipe entry
         */

        public static Uri buildRecipeUriWithId(int id) {
            return CONTENT_URI_RECIPE.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }
    }

    public static final class IngredientEntry implements BaseColumns {

        public static final Uri CONTENT_URI_INGREDIENT = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_INGREDIENTS)
                .build();

        public static final String TABLE_NAME_INGREDIENTS = "ingredients";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_INGREDIENT_QUANTITY = "ingredient_quantity";
        public static final String COLUMN_INGREDIENT_MEASURE = "ingredient_measure";
        public static final String COLUMN_INGREDIENT_NAME = "ingredient_name";
        public static final String COLUMN_RECIPE_ID = "recipe_id";

        /**
         * Builds a URI that adds the ingredient id to the end of the ingredient content URI path.
         * This is used to query details about a single ingredient entry by id.
         *
         * @param id Id of the ingredient
         * @return Uri to query details about a single ingredient entry
         */

        public static Uri buildIngredientUriWithId(int id) {
            return CONTENT_URI_INGREDIENT.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }
    }

    public static final class StepEntry implements BaseColumns {

        public static final Uri CONTENT_URI_STEP = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_STEPS)
                .build();

        public static final String TABLE_NAME_STEPS = "steps";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_STEP_ID = "step_id";
        public static final String COLUMN_STEP_SHORT_DESCRIPTION = "step_short_description";
        public static final String COLUMN_STEP_DESCRIPTION = "step_description";
        public static final String COLUMN_STEP_VIDEO_URL = "step_video_url";
        public static final String COLUMN_STEP_THUMBNAIL_URL = "step_image_url";
        public static final String COLUMN_RECIPE_ID = "recipe_id";

        /**
         * Builds a URI that adds the step id to the end of the step content URI path.
         * This is used to query details about a single step entry by id.
         *
         * @param id Id of the step
         * @return Uri to query details about a single step entry
         */

        public static Uri buildStepUriWithId(int id) {
            return CONTENT_URI_STEP.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }
    }
}