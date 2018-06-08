package com.example.anfio.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anfio.bakingapp.R;
import com.example.anfio.bakingapp.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipesListAdapter extends RecyclerView.Adapter<RecipesListAdapter.RecipesViewHolder> {

    private Context mContext;
    private ArrayList<Recipe> mRecipes;

    //on-click handler defined to make it easy for an Activity to interface with
    private final RecipeAdapterOnClickHandler mClickHandler;

    // The interface that receives onClick messages.
    public interface RecipeAdapterOnClickHandler {
        void onClick(int id);
    }

    public RecipesListAdapter(Context context, RecipeAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public RecipesListAdapter.RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutForRecipe = R.layout.recipes_list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(layoutForRecipe, parent, false);
        return new RecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesListAdapter.RecipesViewHolder holder, int position) {
        String recipeName = mRecipes.get(position).getRecipeName();
        String image = mRecipes.get(position).getImage();
        holder.mTextView.setText(recipeName);
        int drawable = getDrawable(mContext, image);
        Picasso.with(holder.mImageView.getContext())
                .load(drawable)
                .placeholder(R.drawable.loading)
                .error(R.drawable.ic_error_outline_black_24dp)
                .into(holder.mImageView);
    }

    private int getDrawable(Context context, String image) {
        return context.getResources().getIdentifier(image,
                "drawable", context.getPackageName());
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) {
            return 0;
        } else {
            return mRecipes.size();
        }
    }

    public class RecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView mImageView;
        final TextView mTextView;

        RecipesViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_recipe_image);
            mTextView = itemView.findViewById(R.id.tv_recipe_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            // get recipe id at the current position
            int id = mRecipes.get(adapterPosition).getId();
            // call onClick and pass the recipe id
            mClickHandler.onClick(id);
        }
    }

    public void setRecipeData(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }
}