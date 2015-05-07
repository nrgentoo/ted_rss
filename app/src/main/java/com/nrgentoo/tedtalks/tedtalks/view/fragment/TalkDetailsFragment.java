package com.nrgentoo.tedtalks.tedtalks.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nrgentoo.tedtalks.tedtalks.R;
import com.nrgentoo.tedtalks.tedtalks.model.Talk;
import com.nrgentoo.tedtalks.tedtalks.view.activity.PlayerActivity;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import icepick.Icicle;

/**
 * Talk details fragment
 */
public class TalkDetailsFragment extends Fragment {

    private static final String ARGUMENT_TALK = TalkDetailsFragment.class.getPackage() + ".ARGUMENT_TALK";

    public static TalkDetailsFragment newInstance(Talk talk) {
        Bundle args = new Bundle();
        args.putSerializable(ARGUMENT_TALK, talk);

        TalkDetailsFragment fragment = new TalkDetailsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    // --------------------------------------------------------------------------------------------
    //      FIELDS
    // --------------------------------------------------------------------------------------------

    @Icicle
    Talk mTalk;

    // --------------------------------------------------------------------------------------------
    //      UI REFERENCES
    // --------------------------------------------------------------------------------------------

    @InjectView(R.id.iv_image)
    ImageView ivImage;
    @InjectView(R.id.tv_duration)
    TextView tvDuration;
    @InjectView(R.id.tv_category)
    TextView tvCategory;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_description)
    TextView tvDescription;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    // --------------------------------------------------------------------------------------------
    //      LIFECYCLE
    // --------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTalk = (Talk) getArguments().getSerializable(ARGUMENT_TALK);
        } else {
            throw new RuntimeException("Talk object was not provided through the newInstance() method");
        }

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talk_details, container, false);
        ButterKnife.inject(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // set actionbar
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // render contents
        Picasso.with(getActivity())
                .load(mTalk.getImage())
                .fit()
                .centerCrop()
                .into(ivImage);
        tvDuration.setText(mTalk.getDuration());
        tvDescription.setText(mTalk.getDescription());
        tvTitle.setText(mTalk.getTitle());
        StringBuilder category = new StringBuilder();
        category.append(getResources().getString(R.string.category))
                .append(": ")
                .append(mTalk.getCategory());
        tvCategory.setText(category.toString());
        getActivity().setTitle(mTalk.getAuthor());
    }

    // --------------------------------------------------------------------------------------------
    //      MENU
    // --------------------------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // go to talk list
                getFragmentManager().popBackStack();
                return true;
        }

        return false;
    }

    // --------------------------------------------------------------------------------------------
    //      UI METHODS
    // --------------------------------------------------------------------------------------------

    @OnClick(R.id.iv_play)
    void onPlayClick() {
        // show activity with media player
        startActivity(PlayerActivity.getCallingIntent(getActivity(), mTalk.getFileUrl()));
    }
}
