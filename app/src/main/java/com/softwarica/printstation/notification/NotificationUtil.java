package com.softwarica.printstation.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.softwarica.printstation.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationUtil {
    public static final String DEFAULT_CHANNEL = "Default";

    private final Context context;

    private final NotificationManagerCompat notificationManagerCompat;

    private final List<Integer> notificationIds;

    public NotificationUtil(Context context){
        this.context = context;
        this.notificationManagerCompat = NotificationManagerCompat.from(context);
        this.notificationIds = new ArrayList<>();

    }
    public void showNotification(String title, String body) {
        Notification notification = new NotificationCompat
                .Builder(this.context, DEFAULT_CHANNEL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        int notificationId = (int) Math.random();
        notificationManagerCompat.notify(notificationId, notification);
        notificationIds.add(notificationId);
    }

    public void clearNotification(){
        this.notificationManagerCompat.cancelAll();
    }


}
