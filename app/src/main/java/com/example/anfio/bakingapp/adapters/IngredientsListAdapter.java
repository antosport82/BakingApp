package com.example.anfio.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anfio.bakingapp.R;
import com.example.anfio.bakingapp.models.Ingredient;

import java.util.ArrayList;

public class IngredientsListAdapter extends RecyclerView.Adapter<IngredientsListAdapter.IngredientsViewHolder> {

    private Context mContext;
    private ArrayList<Ingredient> mIngredients;

    public IngredientsListAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public IngredientsListAdapter.IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutForIngredient = R.layout.ingredients_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutForIngredient, parent, false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsListAdapter.IngredientsViewHolder holder, int position) {
        double quantity = mIngredients.get(position).getQuantity();
        String measure = mIngredients.get(position).getMeasure();
        String name = mIngredients.get(position).getIngredientName();
        holder.mIngredientQuantity.setText(String.valueOf(quantity));
        holder.mIngredientMeasure.setText(measure);
        holder.mIngredientName.setText(name);
    }

    @Override
    public int getItemCount() {
        if (mIngredients == null) {
            return 0;
        } else {
            return mIngredients.size();
        }
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {
        final TextView mIngredientQuantity;
        final TextView mIngredientMeasure;
        final TextView mIngredientName;

        public IngredientsViewHolder(View itemView) {
            super(itemView);
            mIngredientQuantity = itemView.findViewById(R.id.tv_ingredient_quantity);
            mIngredientMeasure = itemView.findViewById(R.id.tv_ingredient_measure);
            mIngredientName = itemView.findViewById(R.id.tv_ingredient_name);
        }
    }

    public void setIngredientData(ArrayList<Ingredient> ingredients) {
        mIngredients = ingredients;
        notifyDataSetChanged();
    }
}