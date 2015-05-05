package com.nrgentoo.tedtalks.tedtalks.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nrgentoo.tedtalks.tedtalks.R;
import com.nrgentoo.tedtalks.tedtalks.model.Talk;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Talks adapter
 */
public class TalksAdapter extends RecyclerView.Adapter<TalksAdapter.TalkHolder> {

    // --------------------------------------------------------------------------------------------
    //      FIELDS
    // --------------------------------------------------------------------------------------------

    private List<Talk> mTalks;

    // --------------------------------------------------------------------------------------------
    //      CONSTRUCTOR
    // --------------------------------------------------------------------------------------------

    public TalksAdapter(List<Talk> talks) {
        this.mTalks = talks;
    }

    @Override
    public TalkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_talk, parent, false);

        TalkHolder talkHolder = new TalkHolder(view);
        return talkHolder;
    }

    @Override
    public void onBindViewHolder(TalkHolder holder, int position) {
        Talk talk = mTalks.get(position);
        holder.tvTitle.setText(talk.getTitle());
        holder.tvDuration.setText(talk.getDuration());
        holder.tvPubDate.setText(talk.getShortPubDate());
        Picasso.with(holder.itemView.getContext())
                .load(talk.getThumbnail())
                .fit()
                .centerCrop()
                .into(holder.ivThumbnail);
    }

    @Override
    public int getItemCount() {
        return mTalks.size();
    }

    // --------------------------------------------------------------------------------------------
    //      VIEW HOLDER
    // --------------------------------------------------------------------------------------------
    public static class TalkHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.tv_title)
        TextView tvTitle;
        @InjectView(R.id.tv_pub_date)
        TextView tvPubDate;
        @InjectView(R.id.iv_thumbnail)
        ImageView ivThumbnail;
        @InjectView(R.id.tv_duration)
        TextView tvDuration;

        public TalkHolder(View itemView) {
            super(itemView);

            ButterKnife.inject(this, itemView);
        }
    }
}
