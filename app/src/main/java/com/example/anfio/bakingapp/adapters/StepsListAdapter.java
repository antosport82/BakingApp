package com.example.anfio.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anfio.bakingapp.R;
import com.example.anfio.bakingapp.models.Step;

import java.util.ArrayList;

public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.StepsViewHolder> {

    private Context mContext;
    private ArrayList<Step> mSteps;
    //on-click handler defined to make it easy for an Activity to interface with
    private final StepAdapterOnClickHandler mClickHandler;

    // The interface that receives onClick messages.
    public interface StepAdapterOnClickHandler {
        void onClick(int id);
    }

    public StepsListAdapter(Context context, StepAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }
    @NonNull
    @Override
    public StepsListAdapter.StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutForStep = R.layout.steps_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutForStep, parent, false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsListAdapter.StepsViewHolder holder, int position) {
        String shortDescription = mSteps.get(position).getShortDescription();
        holder.mStepsShortDescription.setText(shortDescription);
    }

    @Override
    public int getItemCount() {
        if (mSteps == null) {
            return 0;
        } else {
            return mSteps.size();
        }
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView mStepsShortDescription;

        StepsViewHolder(View itemView) {
            super(itemView);
            mStepsShortDescription = itemView.findViewById(R.id.tv_step_short_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            int id = mSteps.get(adapterPosition).getId();
            mClickHandler.onClick(id);
        }
    }

    public void setStepData(ArrayList<Step> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }
}
