package com.example.ednevnik.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class ReBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Utils _notificationUtils = new Utils(context);
        NotificationCompat.Builder _builder = _notificationUtils.setNotification("Testing", "Testing notification system");
        _notificationUtils.getManager().notify(101, _builder.build());
    }
}
