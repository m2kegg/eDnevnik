package com.example.ednevnik.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.ednevnik.R;

public class Utils extends ContextWrapper{
    private NotificationManager _notificationManager;
    private Context _context;

    public Utils(Context base)
    {
        super(base);
        _context = base;
        createChannel();
    }

    public NotificationCompat.Builder setNotification(String title, String body)
    {
        return new NotificationCompat.Builder(this, getString(R.string.notification_channel))
                .setSmallIcon(R.drawable.rotate_to_button_selector)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    private void createChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(getString(R.string.notification_channel), getString(R.string.timeline), NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(channel);
        }
    }

    public NotificationManager getManager()
    {
        if(_notificationManager == null)
        {
            _notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return _notificationManager;
    }

    public void setReminder(long timeInMillis)
    {
        Intent _intent = new Intent(_context, ReBroadcast.class);
        PendingIntent _pendingIntent = PendingIntent.getBroadcast(_context, 0, _intent, 0);

        AlarmManager _alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        _alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, _pendingIntent);
    }
}
