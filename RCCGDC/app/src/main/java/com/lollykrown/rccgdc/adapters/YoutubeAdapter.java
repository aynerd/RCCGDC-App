package com.lollykrown.rccgdc.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.lollykrown.rccgdc.utils.AppController;
import com.lollykrown.rccgdc.R;
import com.lollykrown.rccgdc.activity.VideoActivity;
import com.lollykrown.rccgdc.model.Video;

import java.util.ArrayList;

public class YoutubeAdapter extends BaseAdapter {
    Activity activity;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private LayoutInflater inflater;
    ArrayList<Video> singletons;


    public YoutubeAdapter(Activity activity, ArrayList<Video> singletons) {
        this.activity = activity;
        this.singletons = singletons;
    }

    public int getCount() {
        return this.singletons.size();
    }

    public Object getItem(int i) {
        return this.singletons.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (this.inflater == null) {
            this.inflater = (LayoutInflater) this.activity.getLayoutInflater();
        }
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.videolist, null);
        }
        if (this.imageLoader == null) {
            this.imageLoader = AppController.getInstance().getImageLoader();
        }
        NetworkImageView networkImageView =convertView.findViewById(R.id.video_image);
        final TextView imgtitle = convertView.findViewById(R.id.video_title);
        final TextView imgdesc = convertView.findViewById(R.id.video_description);
        //final TextView tvURL = convertView.findViewById(R.id.tv_url);
        final  TextView tvVideoID = convertView.findViewById(R.id.tv_videoId);
        final  TextView tvVChannelDesc=convertView.findViewById(R.id.channel_desc);


       ((LinearLayout) convertView.findViewById(R.id.ll)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), VideoActivity.class);
                intent.putExtra("videoId",tvVideoID.getText().toString());
                view.getContext().startActivity(intent);

            }
        });


        Video singleton = (Video) this.singletons.get(i);
        networkImageView.setImageUrl(singleton.getURL(), this.imageLoader);
        tvVideoID.setText(singleton.getVideoId());
        imgtitle.setText(singleton.getVideoName());
        imgdesc.setText(singleton.getVideoDesc());
        //tvURL.setText(singleton.getURL());
        tvVChannelDesc.setText(singleton.getChannelTitle());
        return convertView;
    }
}
