package me.thenightmancodeth.classi.controllers.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import me.thenightmancodeth.classi.R;

/**
 * Created by thenightman on 1/12/17.
 **/

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Notification", "alarm received");
        String title = intent.getExtras().getString("title");
        String location = intent.getExtras().getString("location");
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_icon_circle)
                .setContentTitle(title)
                .setContentText(location);
        int notifyID = 001;
        NotificationManager nm = (NotificationManager)context.
                getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(notifyID, builder.build());
    }
}
