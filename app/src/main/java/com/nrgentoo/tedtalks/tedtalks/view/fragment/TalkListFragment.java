package com.nrgentoo.tedtalks.tedtalks.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nrgentoo.tedtalks.tedtalks.R;
import com.nrgentoo.tedtalks.tedtalks.model.Talk;
import com.nrgentoo.tedtalks.tedtalks.view.adapter.TalksAdapter;
import com.nrgentoo.tedtalks.tedtalks.view.components.DividerItemDecoration;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import icepick.Icicle;

/**
 * Talk list fragment
 */
public class TalkListFragment extends Fragment {

    private static final String ARGUMENT_TALKS = TalkListFragment.class.getPackage() + "ARGUMENT_TALKS";

    public static Fragment newInstance(ArrayList<Talk> talks) {
        Bundle args = new Bundle();
        args.putSerializable(ARGUMENT_TALKS, talks);

        TalkListFragment fragment = new TalkListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    // --------------------------------------------------------------------------------------------
    //      FIELDS
    // --------------------------------------------------------------------------------------------

    @Icicle
    ArrayList<Talk> talks;

    private TalksAdapter adapter;

    // --------------------------------------------------------------------------------------------
    //      UI REFERENCES
    // --------------------------------------------------------------------------------------------

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.rv_talks)
    RecyclerView rvTalks;

    // --------------------------------------------------------------------------------------------
    //      LIFECYCLE
    // --------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            talks = (ArrayList<Talk>) getArguments().getSerializable(ARGUMENT_TALKS);
        } else {
            talks = new ArrayList<>();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talk_list, container, false);
        ButterKnife.inject(this, view);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // set actionbar
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }

        // set layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvTalks.setLayoutManager(mLayoutManager);

        // create adapter
        adapter = new TalksAdapter(talks);
        rvTalks.setAdapter(adapter);

        // set divider
        int margin = getResources().getDimensionPixelSize(R.dimen.margin_2x);
        rvTalks.addItemDecoration(new DividerItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL, margin, margin));
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.setClickListener((v, position) -> onItemClicked(v, position));
    }

    @Override
    public void onPause() {
        adapter.setClickListener(null);
        super.onPause();
    }

    // --------------------------------------------------------------------------------------------
    //      UI METHODS
    // --------------------------------------------------------------------------------------------

    private void onItemClicked(View view, int position) {
        Talk clickedTalk = adapter.getItem(position);
        if (clickedTalk != null) {
            // TODO: show fragment with talk details
        }
    }
}
