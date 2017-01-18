package me.thenightmancodeth.classi.controllers.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;
import me.thenightmancodeth.classi.controllers.receivers.AlarmBootReceiver;

import me.thenightmancodeth.classi.controllers.receivers.AlarmReceiver;
import me.thenightmancodeth.classi.models.data.Class;
import me.thenightmancodeth.classi.views.MainActivity;
import me.thenightmancodeth.classi.views.dialog.ClassDialog;

/**
 * Created by thenightman on 1/12/17.
 **/

public class AlarmBootService extends IntentService {

    private Handler handler;
    public AlarmBootService() {
        super("AlarmBootService");
    }

    @Override
    public void onCreate() {
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("BootReceiver", "Boot received 2");
        //Toast.makeText(getBaseContext(), "Booted", Toast.LENGTH_LONG).show();
        //Get list of classes from realm and set up alarms for each
        Realm.init(getBaseContext());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Class> classes = realm.where(Class.class).findAll();
        for (final Class c : classes) {
            for (final char day : c.getDays().toCharArray()) {
                int d = ClassDialog.charToDay(day);
                createWeeklyAlarmForDay(getApplicationContext(), d, c.getName(), c.getBuilding(),
                        c.getFromAMPM().equals("AM") ? Calendar.AM : Calendar.PM,
                        c.getTimeFromH(), c.getTimeFromM());
            }
        }
        AlarmBootReceiver.completeWakefulIntent(intent);
    }

    public void createWeeklyAlarmForDay(Context c, int d, String name, String building, int AMPM, int hr, int min) {
        AlarmManager alarm = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();
        //Number of days to the day of alarm
        int days = d + (7 - cal.get(Calendar.DAY_OF_WEEK));
        //One hour before class starts
        cal.add(Calendar.DATE, days);
        cal.set(Calendar.HOUR, hr - 1);
        cal.set(Calendar.MINUTE, min - 1);
        cal.set(Calendar.AM_PM, AMPM);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.putExtra("title", name);
        alarmIntent.putExtra("location", building);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, alarmIntent,
                PendingIntent.FLAG_ONE_SHOT);
        //alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
        //      AlarmManager.INTERVAL_DAY * 7, pi);
        alarm.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
        Log.i("Created alarm", "Day: " +d +", Time: " +cal.get(Calendar.HOUR) +":" +cal.get(Calendar.MINUTE) +" " +cal.get(Calendar.AM_PM));
    }
}
