package com.nrgentoo.tedtalks.tedtalks.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.nrgentoo.tedtalks.tedtalks.R;
import com.nrgentoo.tedtalks.tedtalks.model.Talk;
import com.nrgentoo.tedtalks.tedtalks.view.fragment.TalkListFragment;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Ted Talks Activity
 */
public class TedTalksActivity extends AppCompatActivity {

    private static final String EXTRA_TALKS = TedTalksActivity.class.getPackage() + "EXTRA_TALKS";

    public static Intent getCallingIntent(Context context, ArrayList<Talk> talks) {
        Intent intent = new Intent(context, TedTalksActivity.class);
        intent.putExtra(EXTRA_TALKS, talks);

        return intent;
    }

    // --------------------------------------------------------------------------------------------
    //      UI REFERENCES
    // --------------------------------------------------------------------------------------------

    @InjectView(R.id.container)
    FrameLayout container;

    // --------------------------------------------------------------------------------------------
    //      LIFECYCLE
    // --------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ted_talks);
        ButterKnife.inject(this);

        ArrayList<Talk> talks;
        if (getIntent().hasExtra(EXTRA_TALKS)) {
            talks = (ArrayList<Talk>) getIntent().getSerializableExtra(EXTRA_TALKS);
        } else {
            talks = (ArrayList<Talk>) Collections.EMPTY_LIST;
        }

        if (getSupportFragmentManager().findFragmentById(R.id.container) == null) {
            // add talk list fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, TalkListFragment.newInstance(talks))
                    .commit();
        }
    }
}
