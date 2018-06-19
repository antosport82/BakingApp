package com.example.anfio.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.anfio.bakingapp.R;
import com.example.anfio.bakingapp.data.RecipeContract;
import com.example.anfio.bakingapp.utilities.Constants;

public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Cursor mCursor;

    public ListRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) mCursor.close();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        int mRecipeId = sharedPreferences.getInt(Constants.INT_RECIPE, 0);
        String selection = "recipe_id = ?";
        String[] selectionArgs = new String[]{String.valueOf(mRecipeId)};
        mCursor = mContext.getContentResolver().query(
                RecipeContract.IngredientEntry.CONTENT_URI_INGREDIENT,
                null,
                selection,
                selectionArgs,
                null
        );
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) mCursor.close();
    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null || mCursor.getCount() == 0) return null;
        mCursor.moveToPosition(position);
        double quantity = mCursor.getDouble(mCursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_INGREDIENT_QUANTITY));
        String measure = mCursor.getString(mCursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_INGREDIENT_MEASURE));
        String name = mCursor.getString(mCursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_INGREDIENT_NAME));

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.baking_app_widget_provider);
        views.setTextViewText(R.id.widget_ingredient_quantity, String.valueOf(quantity));
        views.setTextViewText(R.id.widget_ingredient_measure, measure);
        views.setTextViewText(R.id.widget_ingredient_name, name);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}