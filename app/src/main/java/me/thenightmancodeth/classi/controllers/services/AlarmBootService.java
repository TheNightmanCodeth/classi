package me.thenightmancodeth.classi.controllers.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;
import me.thenightmancodeth.classi.controllers.receivers.AlarmBootReceiver;

import me.thenightmancodeth.classi.controllers.receivers.AlarmReceiver;
import me.thenightmancodeth.classi.models.data.Class;
import me.thenightmancodeth.classi.models.data.Grade;
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
        super.onCreate();
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

    public void createWeeklyAlarmForDay(Context c, int d, String name, String building, int AMPM,
                                        int hr, int min) {
        AlarmManager alarm = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();
        //Number of days to the day of alarm
        int days = daysTo(d);

        //Get date of alarm (todays date + days)
        int date = cal.get(Calendar.DAY_OF_MONTH) + days; //Add days to get date of alarm
        int month = cal.get(Calendar.MONTH) + 1; //January is 0 so add 1 for joda compatibility
        int year = cal.get(Calendar.YEAR); //Leave the year

        if (date > DateTime.now().dayOfMonth().withMaximumValue().getDayOfMonth()) {
            Log.i("Date", "Too big! Fixing");
            //max: 31, this: 34; excess is 3 (this - max). Date should be next month the third
            int max = DateTime.now().dayOfMonth().withMaximumValue().getDayOfMonth();
            int excess = date - max;
            //Increase month by 1 if it isn't december
            if (month != 12) {
                month++;
            } else {
                month = 1;
            }
            //Set date to the excess
            date = excess;
        }

        //Make string from date. If < 10 add 0 to front of int to match date formatting
        String alarmDay = date < 10 ? "0" +date : String.valueOf(date);
        String alarmMon = month < 10 ? "0" +month : String.valueOf(month);
        String alarmYea = String.valueOf(year);
        String alarmDateString = alarmMon +"/" +alarmDay +"/" +alarmYea;

        //Make string from timt. Again ensure single digit numbers get 0 appended
        String alarmHr = "12";
        if (hr > 1) {
            alarmHr = hr < 10 ? "0" + (hr - 1) : String.valueOf(hr - 1);
        } else if (hr == 1) {
            alarmHr = "12";
        }

        if (hr == 12) {
            Log.i(name, "switching ampm from " +AMPM);
            AMPM = AMPM == 0 ? 1 : 0;
            Log.i(name, "to " +AMPM);
        }
        String alarmMn = min < 10 ? "0" +min : String.valueOf(min);
        String alarmAP = AMPM == 0 ? "AM" : "PM";
        String alarmTimeString = alarmHr +":" +alarmMn +" " +alarmAP;

        //Calculate milliseconds until alarm should sound
        Period untilAlarm = getPeriodTo(alarmDateString, alarmTimeString);
        DateTime start = new DateTime();  //NOW
        DateTime end = start.plus(untilAlarm);

        long millis = Calendar.getInstance().getTimeInMillis()
                + (end.getMillis() - start.getMillis());
        Log.i("Millis", "" +millis);
        //Set alarm
        if (millis > Calendar.getInstance().getTimeInMillis()) {
            Intent alarmIntent = new Intent(c, AlarmReceiver.class);
            alarmIntent.putExtra("title", name);
            alarmIntent.putExtra("location", building);
            alarmIntent.setAction(Long.toString(System.currentTimeMillis()));
            PendingIntent pi = PendingIntent.getBroadcast(c, 0, alarmIntent,
                    PendingIntent.FLAG_ONE_SHOT);
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, millis, AlarmManager.INTERVAL_DAY * 7, pi);
            Log.i("Alarm", name + " is set for: "
                    + untilAlarm.getDays() + " days, " + untilAlarm.getHours() + " hours, "
                    + untilAlarm.getMinutes() +
                    " minutes from now. Or, " + (end.getMillis() - start.getMillis())
                    + " milliseconds");
        }
    }

    public void createAlarmForGrade(Grade g, Context c) {
        Grade newGrade = new Grade();
        newGrade.setDueTime(g.getDueTime());
        int hour = Integer.valueOf(newGrade.getDueTime().substring(0, 2));
        if (hour == 12) {
            Log.i(newGrade.getName(), "hour is 12");
            String switchAMPM = newGrade.getDueTime().substring(6,8);
            Log.e("ampm", newGrade.getDueTime().substring(6,8));
            String time = newGrade.getDueTime();
            if (switchAMPM.equals("AM")) {
                String newTime = time.replace("AM", "PM");
                newGrade.setDueTime(newTime);
            } else if (switchAMPM.equals("PM")) {
                String newTime = time.replace("PM", "AM");
                newGrade.setDueTime(newTime);
            }
            Log.e("ampm", newGrade.getDueTime().substring(6,8));
        }

        //Calculate milliseconds until alarm should sound
        Period untilAlarm = getPeriodTo(g.getDueDate(), newGrade.getDueTime());
        Period untilAlarmMinusOne = untilAlarm.minusHours(1);

        DateTime start = new DateTime();  //NOW
        DateTime end = start.plus(untilAlarmMinusOne);
        long millis = Calendar.getInstance().getTimeInMillis()
                + (end.getMillis() - start.getMillis());

        if (millis > Calendar.getInstance().getTimeInMillis()) {
            AlarmManager alarm = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(c, AlarmReceiver.class);
            alarmIntent.putExtra("title", g.getName());
            String h = untilAlarmMinusOne.getHours() > 1 ? " hours" : " hour";
            alarmIntent.putExtra("due_in", "Due in "
                    +String.valueOf(untilAlarmMinusOne.getHours()) +h);
            alarmIntent.setAction(Long.toString(System.currentTimeMillis()));
            PendingIntent pi = PendingIntent.getBroadcast(c, 0, alarmIntent,
                    PendingIntent.FLAG_ONE_SHOT);
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, millis, AlarmManager.INTERVAL_DAY * 7, pi);
            Log.i("Alarm", g.getName() + " is set for: "
                    + untilAlarmMinusOne.getDays() + " days, " + untilAlarmMinusOne.getHours() + " hours, "
                    + untilAlarmMinusOne.getMinutes() +
                    " minutes from now. Or, " + (end.getMillis() - start.getMillis())
                    + " milliseconds");
        }
    }

    private Period getPeriodTo(String date, String time) {
        DateTime dueDateTime = DateTime.parse(String.format("%s %s", date, time),
                DateTimeFormat.forPattern("MM/dd/yyyy hh:mm aa"));
        DateTime now = DateTime.now();
        return new Period(now, dueDateTime);
    }

    private int daysTo(int dayOfWeek) {
        Calendar c = Calendar.getInstance();
        int today = c.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek > today) {
            return dayOfWeek - today;
        } else {
            int daysTo = 0;
            for (int i = today; i <= 7; i++) {
                if (i == 7) {
                    daysTo++;
                    i = 0;
                } else if (i == dayOfWeek) {
                    break;
                } else {
                    daysTo++;
                }
            }
            return daysTo;
        }
    }
}
