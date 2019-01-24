package com.lollykrown.rccgdc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lollykrown.rccgdc.R;

public  class EventDetailActivity extends AppCompatActivity{

    private ImageView eventImgTv;
    private TextView eventTitleTv;
    private TextView eventDateTv;
    private TextView eventTimeTv;
    private TextView eventVenueTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details_activity);

        //getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.slide));

        Toolbar toolbar = findViewById(R.id.ev_toolbar);
        toolbar.setTitle(R.string.programme_details);
        toolbar.setLogo(R.drawable.rccg);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));

        eventImgTv = findViewById(R.id.ev_image);
        eventTitleTv = findViewById(R.id.ev_title);
        eventDateTv = findViewById(R.id.ev_date);
        eventTimeTv = findViewById(R.id.ev_time);
        eventVenueTv = findViewById(R.id.ev_venue);

        Intent i = getIntent();

//        String eventTitle = i.getStringExtra("title");
//        String eventDate = i.getStringExtra("date");
//        String eventTime = i.getStringExtra("time");
//        String eventImageUrl = i.getStringExtra("url");

        String eventTitle = i.getStringExtra("eventTitle");
        String eventDate = i.getStringExtra("eventDate");
        String eventTime = i.getStringExtra("eventTime");
        String eventImageUrl = i.getStringExtra("eventImgUrl");
        String eventVenue = i.getStringExtra("eventVenue");

        Glide.with(this)
                .load(eventImageUrl)
                .asBitmap()
                .error(R.drawable.loading_img)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(eventImgTv);
        //eventImgTv.setTransitionName("thumbnailTransition");

        eventTitleTv.setText(eventTitle);
        eventDateTv.setText(eventDate);
        eventTimeTv.setText(eventTime);
    }
}
