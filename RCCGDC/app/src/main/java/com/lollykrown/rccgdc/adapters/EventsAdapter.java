package com.lollykrown.rccgdc.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lollykrown.rccgdc.R;
import com.lollykrown.rccgdc.activity.EventDetailActivity;
import com.lollykrown.rccgdc.model.Events;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder>{

    Context mContext;
    List<Events> mUpEvents;

    public EventsAdapter(Context context, List<Events> upEvents) {
        mContext = context;
        mUpEvents = upEvents;
    }

    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.events_list_items;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventsViewHolder holder, int i) {

            Events u = mUpEvents.get(i);
            holder.eventTitle.setText(u.getEventTitle());
            holder.eventDate.setText(u.getEventDate());

            Glide.with(mContext)
                    .load(u.getUrl())
                    .error(R.drawable.loading_img)
                    .placeholder(R.drawable.loading_img)
                    .into(holder.eventImage);
    }

    @Override
    public int getItemCount() {
        return mUpEvents.size();
    }

    class EventsViewHolder extends RecyclerView.ViewHolder {

        TextView eventTitle;
        TextView eventDate;
        ImageView eventImage;

        public EventsViewHolder(final View itemView) {
            super(itemView);

            eventImage = itemView.findViewById(R.id.event_image);
            eventTitle = itemView.findViewById(R.id.event_title);
            eventDate = itemView.findViewById(R.id.event_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                int pos = getAdapterPosition();
                                                Intent i = new Intent(itemView.getContext(), EventDetailActivity.class);

                                                Events ev = mUpEvents.get(pos);
                                                i.putExtra("eventTitle", ev.getEventTitle());
                                                i.putExtra("eventDate", ev.getEventDate());
                                                i.putExtra("eventTime", ev.getEventTime());
                                                i.putExtra("eventImgUrl", ev.getUrl());
//                                                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) itemView.getContext(), eventImage, "eventProfile");
//                                                itemView.getContext().startActivity(i, options.toBundle());
                                                itemView.getContext().startActivity(i);

                                            }
            });

        }
    }
}
