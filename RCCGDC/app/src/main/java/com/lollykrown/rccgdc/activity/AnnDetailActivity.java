package com.lollykrown.rccgdc.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lollykrown.rccgdc.R;

public class AnnDetailActivity extends AppCompatActivity {

    private ImageView annImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ann_detail);

        Toolbar toolbar = findViewById(R.id.ev_toolbar);
        toolbar.setTitle(R.string.announcements);
        toolbar.setLogo(R.drawable.rccg);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));

        annImage = findViewById(R.id.an_image);

        Intent i = getIntent();
        String annImageId = i.getStringExtra("annImg");


        Glide.with(this)
                .load(annImageId)
                .asBitmap()
                .error(R.drawable.loading_img)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(annImage);
    }
}
