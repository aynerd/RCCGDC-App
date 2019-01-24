package com.lollykrown.rccgdc.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lollykrown.rccgdc.R;
import com.lollykrown.rccgdc.activity.EventDetailActivity;
import com.lollykrown.rccgdc.model.Notifications;
import com.lollykrown.rccgdc.model.NotificationsDao;
import com.lollykrown.rccgdc.model.NotificationsDatabase;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String ADMIN_CHANNEL_ID = "admin_channel";
    private static final String NOTIFICATION_ID_EXTRA = "notificationId";
    private static final String IMAGE_URL_EXTRA = "image_url";
    private static final String TITLE_EXTRA = "title";
    private static final String DATE_EXTRA = "date";
    private static final String TIME_EXTRA = "time";
    NotificationsDatabase db;

    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String title = "";
        String imageUrl = "";
        String date = "";
        String time = "";
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            title = remoteMessage.getData().get("title");
            imageUrl = remoteMessage.getData().get("url");
            date = remoteMessage.getData().get("date");
            time = remoteMessage.getData().get("time");
            String click_action = remoteMessage.getNotification().getClickAction();

            sendNotification(remoteMessage.getNotification().getBody(), title, imageUrl, date, time, click_action);

            Notifications n = new Notifications(title, date, time, imageUrl, 0);

            db = NotificationsDatabase.getInMemoryDatabase(getApplicationContext());
            db.notificationsDao().addNotifications(n);


        }
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
        }
    }
    private void sendNotification(String messageBody, String title, String imageUrl, String date, String time, String click_action) {
        Intent i = new Intent(click_action);
        i.putExtra(IMAGE_URL_EXTRA, imageUrl);
        i.putExtra(TITLE_EXTRA, title);
        i.putExtra(DATE_EXTRA, date);
        i.putExtra(TIME_EXTRA, time);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, i,
                PendingIntent.FLAG_ONE_SHOT);

        Bitmap bitmap = getBitmapfromUrl(imageUrl);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.rccgdc)
                .setLargeIcon(bitmap)
                .setContentTitle(title)
                .setContentText(title)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}