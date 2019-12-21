package com.softwarica.printstation;

import android.content.Intent;

import com.softwarica.printstation.api.API;
import com.softwarica.printstation.notification.NotificationUtil;
import com.softwarica.printstation.service.ShakeService;
import com.softwarica.printstation.storage.PrefManager;

public class PrintStationApplication extends android.app.Application {

    private NotificationUtil notificationUtil;

    private PrefManager prefManager;

    @Override
    public void onCreate() {
        super.onCreate();
        this.notificationUtil = new NotificationUtil(this);
        this.prefManager = new PrefManager(this);

        PrefManager.initService(this);
        API.initService(this);

        Intent intent = new Intent(this, ShakeService.class);
        //Start Service
        startService(intent);
    }

    public NotificationUtil getNotificationUtil() {
        return notificationUtil;
    }

    public PrefManager getPrefManager() {
        return this.prefManager;
    }
}
