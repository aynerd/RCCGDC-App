package com.lollykrown.rccgdc.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lollykrown.rccgdc.R;
import com.lollykrown.rccgdc.activity.AnnDetailActivity;
import com.lollykrown.rccgdc.model.Announcements;

import java.util.List;

public class AnnouncementsAdapter extends RecyclerView.Adapter<AnnouncementsAdapter.AnnouncementsViewHolder>{

    Context mContext;
    List<Announcements> mAnn;

    public AnnouncementsAdapter(Context context, List<Announcements> ann) {
        mContext = context;
        mAnn = ann;
    }

    @Override
    public AnnouncementsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.ann_list_items;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new AnnouncementsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnnouncementsViewHolder holder, int i) {

        Announcements a = mAnn.get(i);

        Glide.with(mContext)
                .load(a.getUrl())
                .placeholder(R.drawable.loading_img)
                .into(holder.annImage);
    }

    @Override
    public int getItemCount() {
        return mAnn.size();
    }

    class AnnouncementsViewHolder extends RecyclerView.ViewHolder {

        ImageView annImage;

        public AnnouncementsViewHolder(final View itemView) {
            super(itemView);

            annImage = itemView.findViewById(R.id.ann_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Announcements an = mAnn.get(pos);

                    Intent i = new Intent(itemView.getContext(), AnnDetailActivity.class);
                    i.putExtra("annImg", an.getUrl());
                    itemView.getContext().startActivity(i);
                }
            });
        }
    }
}
