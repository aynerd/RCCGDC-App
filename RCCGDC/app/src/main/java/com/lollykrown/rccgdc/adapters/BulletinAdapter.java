package com.lollykrown.rccgdc.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lollykrown.rccgdc.R;
import com.lollykrown.rccgdc.model.Bulletin;

import java.util.List;

public class BulletinAdapter extends RecyclerView.Adapter<BulletinAdapter.BulletinViewHolder>{

    final private BulletinAdapter.BullListItemClickListener mBullOnClickListener;
    Context mContext;

    List<Bulletin> mBull;

    public interface BullListItemClickListener {
        void onBullListItemClick(int clickedItemIndex);
    }

    public BulletinAdapter(Context context, List<Bulletin> bull, BulletinAdapter.BullListItemClickListener listener) {
        mContext = context;
        mBull = bull;
        mBullOnClickListener = listener;
    }

    @Override
    public BulletinAdapter.BulletinViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.bulletin_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new BulletinAdapter.BulletinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BulletinAdapter.BulletinViewHolder holder, int i) {

        Bulletin a = mBull.get(i);
        holder.bullMonth.setText(a.getBullMonth());
        Glide.with(mContext)
                .load(a.getBullImgUrl())
                .placeholder(R.drawable.loading_img)
                .into(holder.bullImage);
    }

    @Override
    public int getItemCount() {
        return mBull.size();
    }

    class BulletinViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView bullMonth;
        ImageView bullImage;

        public BulletinViewHolder(View itemView) {
            super(itemView);

            bullImage = itemView.findViewById(R.id.bull_image);
            bullMonth = itemView.findViewById(R.id.bull_month);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mBullOnClickListener.onBullListItemClick(clickedPosition);
        }
    }
}
