package com.nrgentoo.tedtalks.tedtalks.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.IBinder;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import com.nrgentoo.tedtalks.tedtalks.R;
import com.nrgentoo.tedtalks.tedtalks.Service.VideoPlaybackService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import icepick.Icicle;

/**
 * player activity
 */
public class PlayerActivity extends AppCompatActivity {

    private static final String EXTRA_VIDEO_URL = PlayerActivity.class.getPackage() + ".EXTRA_VIDEO_URL";

    public static Intent getCallingIntent(Context context, String url) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(EXTRA_VIDEO_URL, url);

        return intent;
    }

    // --------------------------------------------------------------------------------------------
    //      FIELDS
    // --------------------------------------------------------------------------------------------

    @Icicle
    String url;

    // --------------------------------------------------------------------------------------------
    //      UI REFERENCES
    // --------------------------------------------------------------------------------------------

    @InjectView(R.id.videoView)
    VideoView videoView;

    // --------------------------------------------------------------------------------------------
    //      LIFECYCLE
    // --------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.inject(this);

        getWindow().clearFlags(WindowManager
                .LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (getIntent().hasExtra(EXTRA_VIDEO_URL)) {
            url = getIntent().getStringExtra(EXTRA_VIDEO_URL);
        } else {
            throw new RuntimeException("Video url was not provided in calling intent. " +
                    "Put url String with key " + EXTRA_VIDEO_URL);
        }

        Uri videoUri = Uri.parse(url);

        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(videoUri);
        videoView.start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
