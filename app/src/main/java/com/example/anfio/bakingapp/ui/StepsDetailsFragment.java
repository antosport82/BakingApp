package com.example.anfio.bakingapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.anfio.bakingapp.R;
import com.example.anfio.bakingapp.data.RecipeContract;
import com.example.anfio.bakingapp.utilities.Constants;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StepsDetailsFragment extends Fragment {

    private int mStepId;
    private int mRecipeId;
    @BindView(R.id.tv_step_text)
    TextView mStepText;
    @BindView(R.id.player_view)
    SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.tv_no_video)
    TextView mNoVideo;
    @BindView(R.id.button_next)
    Button mBtNext;

    private SimpleExoPlayer player;
    private Context mContext;
    private int mStepsSize;
    private boolean mTwoPane;
    private Unbinder unbinder;

    private final LoaderManager.LoaderCallbacks<Cursor> stepLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            String selection = "recipe_id = ? and step_id = ?";
            String[] selectionArgs = new String[]{String.valueOf(mRecipeId), String.valueOf(mStepId)};
            return new CursorLoader(mContext, RecipeContract.StepEntry.CONTENT_URI_STEP, null, selection, selectionArgs, null);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            String mUrlVideo;
            if (data.getCount() != 0) {
                data.moveToFirst();
                mUrlVideo = data.getString(data.getColumnIndex(RecipeContract.StepEntry.COLUMN_STEP_VIDEO_URL));
                mStepText.setText(data.getString(data.getColumnIndex(RecipeContract.StepEntry.COLUMN_STEP_DESCRIPTION)));
                if (mUrlVideo.length() > 0) {
                    mNoVideo.setVisibility(View.GONE);
                    simpleExoPlayerView.setVisibility(View.VISIBLE);
                    initializePlayer(mUrlVideo);
                } else {
                    simpleExoPlayerView.setVisibility(View.GONE);
                    mNoVideo.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        }
    };

    public StepsDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mStepText = rootView.findViewById(R.id.tv_step_text);
        simpleExoPlayerView = rootView.findViewById(R.id.player_view);
        mNoVideo = rootView.findViewById(R.id.tv_no_video);
        mBtNext = rootView.findViewById(R.id.button_next);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getContext();
        if (mTwoPane) {
            mBtNext.setVisibility(View.GONE);
        } else {
            mBtNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences;
                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                    mStepsSize = sharedPreferences.getInt(String.valueOf(mRecipeId), 0);
                    if (mStepId < (mStepsSize - 1)) {
                        mStepId++;
                    } else {
                        mStepId = 0;
                    }
                    getLoaderManager().restartLoader(Constants.STEPS_DETAIL_LOADER, null, stepLoader);
                }
            });
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mRecipeId = sharedPreferences.getInt(Constants.INT_RECIPE, 0);
        getLoaderManager().initLoader(Constants.STEPS_DETAIL_LOADER, null, stepLoader);

    }

    public void setStep(int step) {
        mStepId = step;
    }

    public void setPane(boolean twoPane) {
        mTwoPane = twoPane;
    }

    private void initializePlayer(String mUrlVideo) {
        Uri video = Uri.parse(mUrlVideo);
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
        simpleExoPlayerView.setPlayer(player);
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter defaultbandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                Util.getUserAgent(mContext, "yourApplicationName"), defaultbandwidthMeter);
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(video);
        // Prepare the player with the source.
        player.prepare(videoSource);
        player.setPlayWhenReady(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        player.stop();
        player.release();
        player = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}