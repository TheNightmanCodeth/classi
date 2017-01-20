package me.thenightmancodeth.classi.controllers.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
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
        String title = intent.getExtras().getString("title");
        String location = "";
        if (intent.getExtras().getString("location") != null) {
            location = intent.getExtras().getString("location");
        } else if (intent.getExtras().getString("due_date") != null) {
            location = intent.getExtras().getString("due_in");
        }

        Log.i("Notification", "alarm received from: " +title);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_noti_icon)
                .setContentTitle(title)
                .setContentText(location)
                .setVibrate(new long[] {1000, 1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        int notifyID = 1;

        NotificationManager nm = (NotificationManager)context.
                getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(notifyID, builder.build());
    }
}
