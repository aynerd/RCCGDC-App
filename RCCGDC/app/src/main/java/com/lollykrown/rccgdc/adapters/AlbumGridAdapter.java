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
import com.lollykrown.rccgdc.activity.AlbumActivity;
import com.lollykrown.rccgdc.model.Album;

public class AlbumGridAdapter extends RecyclerView.Adapter<AlbumGridAdapter.MyViewHolder> {

    private Album[] mPhotos;
    private Context mContext;

    public AlbumGridAdapter(Context context, Album[] photos) {
        mContext = context;
        mPhotos = photos;
    }

    @Override
    public AlbumGridAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.album_list_item, parent, false);
        AlbumGridAdapter.MyViewHolder viewHolder = new AlbumGridAdapter.MyViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AlbumGridAdapter.MyViewHolder holder, int position) {

        Album alb = mPhotos[position];
        ImageView imageView = holder.mPhotoImageView;

        String s = String.valueOf(alb.getTotal()) + " pictures";
        holder.mTitleTv.setText(alb.getTitle());
        holder.mTotalTv.setText(s);

        Glide.with(mContext)
                .load(alb.getUrl())
                .error(R.drawable.loading_img)
                .placeholder(R.drawable.loading_img)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return (mPhotos.length);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mPhotoImageView;
        private TextView mTitleTv;
        private TextView mTotalTv;

        public MyViewHolder(View itemView) {

            super(itemView);
            mPhotoImageView = itemView.findViewById(R.id.ab_photo);
            mTitleTv = itemView.findViewById(R.id.album_tag);
            mTotalTv = itemView.findViewById(R.id.no_of_pictures);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(itemView.getContext(), AlbumActivity.class);
                mContext.startActivity(intent);
            }
        }
    }
}
