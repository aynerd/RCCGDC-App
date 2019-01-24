package com.lollykrown.rccgdc.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lollykrown.rccgdc.R;
import com.lollykrown.rccgdc.model.Notifications;
import com.lollykrown.rccgdc.model.NotificationsDatabase;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder>{

//    final private NotificationsAdapter.NotificationListItemClickListener mOnClickListener;

    Context mContext;
    List<Notifications> mNotifications;
    NotificationsDatabase db;

//    public interface NotificationListItemClickListener {
//        void onNotificationListItemClick(int clickedItemIndex);
//    }

    public NotificationsAdapter(Context context, List<Notifications> notifications) {
        mContext = context;
        mNotifications = notifications;
        //mOnClickListener = listener;
    }

    @Override
    public NotificationsAdapter.NotificationsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.notifications_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new NotificationsAdapter.NotificationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationsAdapter.NotificationsViewHolder holder, int i) {

        Notifications n = mNotifications.get(i);
        holder.title.setText(n.getTitlee());
        holder.date.setText(n.getDatee());
        holder.time.setText(n.getTimee());

        Glide.with(mContext)
                .load(n.getUrl())
                .error(R.drawable.loading_img)
                .placeholder(R.drawable.loading_img)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    class NotificationsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        TextView title;
        TextView date;
        TextView time;
        ImageView image;

        public NotificationsViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.not_image);
            title = itemView.findViewById(R.id.not_title);
            date = itemView.findViewById(R.id.not_date);
            time = itemView.findViewById(R.id.not_time);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Notifications n = mNotifications.get(position);
            String title = n.getTitlee();
            String date = n.getDatee();
            String time = n.getDatee();
            String url = n.getUrl();
            Notifications noti =  new Notifications(title, date, time, url, 1);

            db = NotificationsDatabase.getInMemoryDatabase(mContext);
            db.notificationsDao().updateNotifications(noti);

        }
    }
//
//    public void setState(){
//        SharedPreferences sharedPref = mContext.getSharedPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString("readState", "read");
//        editor.apply();
//    }
}
