package me.thenightmancodeth.classi.controllers.receivers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import me.thenightmancodeth.classi.controllers.services.AlarmBootService;

/**
 * Created by thenightman on 1/12/17.
 **/

public class AlarmBootReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("BootReceiver", "Boot received");
        Intent alarmBootServiceIntent = new Intent(context, AlarmBootService.class);
        startWakefulService(context, alarmBootServiceIntent);
    }
}
