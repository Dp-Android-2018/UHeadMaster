package com.dp.uheadmaster.jwplayer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.AnnounceMentAct;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.util.PlayerControl;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.c.m;
import com.longtailvideo.jwplayer.cast.CastManager;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.captions.Caption;
import com.longtailvideo.jwplayer.media.playlists.MediaSource;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.dmoral.toasty.Toasty;

public class JWPlayerViewExample extends AppCompatActivity {

    /**
     * Reference to the {@link JWPlayerView}
     */
    private JWPlayerView mPlayerView;

    /**
     * An instance of our event handling class
     */
    private JWEventHandler mEventHandler;

    /**
     * Reference to the {@link CastManager}
     */
    private CastManager mCastManager;

    /**
     * Stored instance of CoordinatorLayout
     * http://developer.android.com/reference/android/support/design/widget/CoordinatorLayout.html
     */
    private CoordinatorLayout mCoordinatorLayout;

    List<MediaSource> mediaSources;
    private SharedPrefManager sharedPrefManager;
    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    private BandwidthMeter bandwidthMeter;
    private DefaultTrackSelector trackSelector;
    private  com.google.android.exoplayer2.source.MediaSource subtitleSource;
    private ProgressDialog progressBar;
    Uri videoURI = null;
    private ImageView ivSubtitle;
    private   com.google.android.exoplayer2.source.MediaSource mediaSource;
    private  Format subtitleFormat;
    private  MergingMediaSource mergedSource;
    private Uri subtitleUri;
    private  DefaultHttpDataSourceFactory dataSourceFactory;
    private Long seekingval;
    private PopupMenu popup;
    private  HashMap<String, String> subtitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        exoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exo_player_view);
        exoPlayerView.requestFocus();
        if (getIntent()!=null) {
            if ((HashMap<String, String>) getIntent().getSerializableExtra("SubTiltes") != null) {
                subtitle = (HashMap<String, String>) getIntent().getSerializableExtra("SubTiltes");
            }
        }

        try {


            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            ivSubtitle = (ImageView) findViewById(R.id.exo_subtiltle);
            ivSubtitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        popup = new PopupMenu(JWPlayerViewExample.this, v,Gravity.CENTER);
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                if (!item.getTitle().equals(getApplicationContext().getString(R.string.video_subtitle))) {
                                    seekingval = exoPlayer.getCurrentPosition();
                                    setupMerge(item.getTitle().toString());
                                    exoPlayer.seekTo(seekingval);
                                }
                                    return false;

                            }
                        });

                        if (subtitle!=null) {
                            Set<String> keys = subtitle.keySet();
                            Menu menu = popup.getMenu();
                            menu.add(Menu.NONE, 0, 0, R.string.video_subtitle);
                            for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
                                String key = it.next();
                                menu.add(key);
                                menu.setGroupCheckable(1, true, true);
                                popup.show();
                            }
                        }

                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            exoPlayer.addListener(new ExoPlayer.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playbackState == ExoPlayer.STATE_BUFFERING) {
                        //progressBar.setVisibility(View.VISIBLE);
                        progressBar = ConfigurationFile.showDialog(JWPlayerViewExample.this);
                    } else {
                        ConfigurationFile.hideDialog(progressBar);
                    }
                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }

                @Override
                public void onPositionDiscontinuity() {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }
            });


            Intent intent = getIntent();
            if (intent != null) {
                if ((HashMap<String, String>) intent.getSerializableExtra("Links") != null) {
                    HashMap<String, String> hashMap = (HashMap<String, String>) intent.getSerializableExtra("Links");
                    Set<String> keys = hashMap.keySet();
                    for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
                        String key = it.next();
                        //   Toast.makeText(this, "key:"+key, Toast.LENGTH_SHORT).show();
                        MediaSource mediaSource = new MediaSource();
                        if (!hashMap.get(key).equals("")) {
                            videoURI = Uri.parse(hashMap.get(key));
                        }
                    }
                }else if (intent.getStringExtra("Course_Video") != null && !intent.getStringExtra("Course_Video").equals("")) {
                    MediaSource mediaSource = new MediaSource();
                    videoURI = Uri.parse(intent.getStringExtra("Course_Video"));
                }
            }


          dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            dataSourceFactory.setDefaultRequestProperty("Key", ConfigurationFile.ConnectionUrls.HEAD_KEY);
            dataSourceFactory.setDefaultRequestProperty("Authorization", sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN));
            dataSourceFactory.setDefaultRequestProperty("mobile", "Yarb Tshtghl");
            dataSourceFactory.setDefaultRequestProperty("Lang", ConfigurationFile.GlobalVariables.APP_LANGAUGE);
            dataSourceFactory.setDefaultRequestProperty("Id", String.valueOf(sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID)));

            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        } catch (Exception e) {
            Log.e("MainAcvtivity", " exoplayer error " + e.toString());
        }
    }

    public void setupMerge(String title){
        subtitleUri = Uri.parse("http://uheadmaster.com" + subtitle.get(title));
        subtitleFormat = Format.createTextSampleFormat(
                null, // An identifier for the track. May be null.
                MimeTypes.APPLICATION_SUBRIP, // The mime type. Must be set correctly.
                null,
                Format.NO_VALUE,
                Format.NO_VALUE,
                "en",
                null); // The subtitle language. May be null.

        subtitleSource = new SingleSampleMediaSource(subtitleUri, dataSourceFactory, subtitleFormat, C.TIME_UNSET);
        mergedSource = new MergingMediaSource(mediaSource, subtitleSource);
        exoPlayer.prepare(mergedSource);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exoPlayer.stop();
        finish();
    }
}
