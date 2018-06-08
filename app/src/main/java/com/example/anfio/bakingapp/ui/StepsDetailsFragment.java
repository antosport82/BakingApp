package com.example.anfio.bakingapp.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

public class StepsDetailsFragment extends Fragment {

    private int mStepId;
    private int mRecipeId;
    private TextView mText;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private Button mBtNext;
    private TextView mNoVideo;
    private String mUrlVideo;
    private Context mContext;

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
            if (data.getCount() != 0) {
                data.moveToFirst();
                mUrlVideo = data.getString(data.getColumnIndex(RecipeContract.StepEntry.COLUMN_STEP_VIDEO_URL));
                mText.setText(data.getString(data.getColumnIndex(RecipeContract.StepEntry.COLUMN_STEP_DESCRIPTION)));
                if (mUrlVideo.length() > 0){
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
        View rootView = inflater.inflate(R.layout.activity_step_details, container, false);
        mText = rootView.findViewById(R.id.tv_step_text);
        simpleExoPlayerView = rootView.findViewById(R.id.player_view);
        mNoVideo = rootView.findViewById(R.id.tv_no_video);
        mBtNext = rootView.findViewById(R.id.button_next);
        mRecipeId = 1;
        mStepId = 0;
        mContext = this.getActivity().getApplicationContext();
        return rootView;
    }

    private void initializePlayer(String mUrlVideo) {
        Uri video = Uri.parse(mUrlVideo);
        Handler mainHandler = new Handler();
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
}