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

public class BullDetailsActivity extends AppCompatActivity {

    ImageView img;
    TextView bullMonth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bull_details);

        img = findViewById(R.id.bu_image);
        bullMonth = findViewById(R.id.bu_month);

        Intent i = getIntent();
        String  bullImageUrl = i.getStringExtra("bullImgUrl");

        Glide.with(this)
                .load(bullImageUrl)
                .asBitmap()
                .error(R.drawable.loading_img)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(img);

        String bullM = i.getStringExtra("bullMonth");
        //String eventVenue = i.getStringExtra("eventVenue");

        //img.setImageResource(R.drawable.bulletin);
        bullMonth.setText(bullM);
    }
}
