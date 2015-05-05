package com.nrgentoo.tedtalks.tedtalks.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nrgentoo.tedtalks.tedtalks.R;
import com.nrgentoo.tedtalks.tedtalks.model.Talk;
import com.nrgentoo.tedtalks.tedtalks.network.TedRssService;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;
import me.tatarka.rxloader.RxLoaderObserver;
import rx.Observable;


public class SplashActivity extends AppCompatActivity {

    // --------------------------------------------------------------------------------------------
    //      FIELDS
    // --------------------------------------------------------------------------------------------
    private RxLoaderManager mLoaderManager;

    // --------------------------------------------------------------------------------------------
    //      UI REFERENCES
    // --------------------------------------------------------------------------------------------
    @InjectView(R.id.pb_loading)
    ProgressBar pbLoading;

    // --------------------------------------------------------------------------------------------
    //      LIFECYCLE
    // --------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);

        // init loaderManager
        mLoaderManager = RxLoaderManagerCompat.get(this);

        // create loader
        mLoaderManager.create(
                loadRssFeed(),
                new RxLoaderObserver<List<Talk>>() {
                    @Override
                    public void onNext(List<Talk> value) {
                        // TODO: finish activity and supply result to the TalkList Activity
                        if (value != null && !value.isEmpty()) {
                            Toast.makeText(SplashActivity.this, "Ok",
                                    Toast.LENGTH_SHORT).show();
                            Log.d("Load Rss", "talks size: " + value.size());
                            Log.d("Load Rss", "first item id: " + value.get(0).getTalkId());
                            Log.d("Load Rss", "first item: " + value.get(0).getTitle());
                            Log.d("Load Rss", "first item url: " + value.get(0).getFileUrl());
                            Log.d("Load Rss", "first item image: " + value.get(0).getImage());
                            Log.d("Load Rss", "first item thumbnail: " + value.get(0).getThumbnail());
                            Log.d("Load Rss", "second item id: " + value.get(1).getTalkId());
                            Log.d("Load Rss", "second item: " + value.get(1).getTitle());
                            Log.d("Load Rss", "second item url: " + value.get(1).getFileUrl());
                            Log.d("Load Rss", "second item image: " + value.get(1).getImage());
                            Log.d("Load Rss", "second item thumbnail: " + value.get(1).getThumbnail());

                            startActivity(TedTalksActivity.getCallingIntent(SplashActivity.this, (ArrayList<Talk>) value));
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    }

    // --------------------------------------------------------------------------------------------
    //      API REQUESTS
    // --------------------------------------------------------------------------------------------
    private Observable<List<Talk>> loadRssFeed() {
        // load talks from rss feed
        return TedRssService.INSTANCE.getTalks();
    }
}
