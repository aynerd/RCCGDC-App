package com.lollykrown.rccgdc.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lollykrown.rccgdc.R;
import com.lollykrown.rccgdc.activity.PhotoDetailsActivity;
import com.lollykrown.rccgdc.model.Photo;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>  {

    @Override
    public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.photos_list_item, parent, false);
        ImageGalleryAdapter.MyViewHolder viewHolder = new ImageGalleryAdapter.MyViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageGalleryAdapter.MyViewHolder holder, int position) {

        Photo photo = mPhotos[position];
        ImageView imageView = holder.mPhotoImageView;

        Glide.with(mContext)
                .load(photo.getUrl())
                .placeholder(R.drawable.loading_img)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return (mPhotos.length);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mPhotoImageView;

        public MyViewHolder(View itemView) {

            super(itemView);
            mPhotoImageView = (ImageView) itemView.findViewById(R.id.iv_photo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                Photo photo = mPhotos[position];
                Intent intent = new Intent(mContext, PhotoDetailsActivity.class);
                intent.putExtra(PhotoDetailsActivity.EXTRA_SPACE_PHOTO, photo);
                mContext.startActivity(intent);
            }
        }
    }

    private Photo[] mPhotos;
    private Context mContext;

    public ImageGalleryAdapter(Context context, Photo[] photos) {
        mContext = context;
        mPhotos = photos;
    }
}
