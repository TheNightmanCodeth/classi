package me.thenightmancodeth.classi.views;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import me.thenightmancodeth.classi.R;
import me.thenightmancodeth.classi.controllers.receivers.AlarmReceiver;
import me.thenightmancodeth.classi.models.Api;
import me.thenightmancodeth.classi.models.data.Class;
import me.thenightmancodeth.classi.models.data.GradeType;
import me.thenightmancodeth.classi.views.dialog.ClassDialog;

import static me.thenightmancodeth.classi.views.dialog.ClassDialog.charToDay;

/**
 * Created by thenightman on 1/8/17.
 **/

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.class_recycler) RecyclerView classesRecycler;
    public static final String CLASS_NAME_EXTRA = "class_to_pass";
    Api api;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();

        api = new Api(realm);
        makeAlarms();
        initUI();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            makeClasses();
            refreshClasses();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initUI() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment classDialogFrag = ClassDialog.newInstance();
                classDialogFrag.show(getFragmentManager(), "classdialog");
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        classesRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        classesRecycler.setLayoutManager(layoutManager);

        refreshClasses();
    }

    public void refreshClasses() {
        ClassRecycleAdapter adapter = new ClassRecycleAdapter(api.getClassesFromRealm(),
                getApplicationContext());
        classesRecycler.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshClasses();
        makeAlarms();
    }

    private void makeAlarms() {
        for (Class c : api.getClassesFromRealm()) {
            for (char d : c.getDays().toCharArray()) {
                createWeeklyAlarmForDay(getApplicationContext(),
                        charToDay(d),
                        c.getName(), c.getBuilding(),
                        c.getFromAMPM().equals("AM") ? Calendar.AM : Calendar.PM,
                        c.getTimeFromH(), c.getTimeFromM());
            }
        }
    }

    protected void makeClasses() {
        List<Class> classes = new ArrayList<>();
        Class CSE = new Class();
        CSE.setName("Professional Practices and Ethics");
        CSE.setBuilding("Academic 200");
        CSE.setProfessor("Chavoshi, Manijeh");
        CSE.setTimeFromH(2);
        CSE.setTimeFromM(0);
        CSE.setFromAMPM("PM");
        CSE.setTimeToH(2);
        CSE.setTimeToM(50);
        CSE.setToAMPM("PM");
        CSE.setDays("MW");
        RealmList<GradeType> types = new RealmList<>();
        for (int i = 1; i < 11; i++) {
            GradeType type = new GradeType();
            type.setType("Type " +i);
            type.setPercent(10 * i);
            types.add(type);
        }
        CSE.setTypes(types);
        classes.add(CSE);
        Class ECON = new Class();
        ECON.setName("Contemporary Economic Issues");
        ECON.setBuilding("Engineering Technology Center 201");
        ECON.setProfessor("Patrono, Michael");
        ECON.setTimeFromH(12);
        ECON.setTimeFromM(30);
        ECON.setFromAMPM("PM");
        ECON.setTimeToH(1);
        ECON.setTimeToM(45);
        ECON.setToAMPM("PM");
        ECON.setDays("MW");
        RealmList<GradeType> types2 = new RealmList<>();
        for (int i = 1; i < 11; i++) {
            GradeType type = new GradeType();
            type.setType("Type " +i);
            type.setPercent(10 * i);
            types2.add(type);
        }
        ECON.setTypes(types);
        classes.add(ECON);
        Class HIST = new Class();
        HIST.setName("Modern World History");
        HIST.setBuilding("Textiles 100");
        HIST.setProfessor("Perrin, Charles");
        HIST.setTimeFromH(5);
        HIST.setTimeFromM(0);
        HIST.setFromAMPM("PM");
        HIST.setTimeToH(6);
        HIST.setTimeToM(15);
        HIST.setToAMPM("PM");
        HIST.setDays("MW");
        RealmList<GradeType> types3 = new RealmList<>();
        for (int i = 1; i < 11; i++) {
            GradeType type = new GradeType();
            type.setType("Type " +i);
            type.setPercent(10 * i);
            types3.add(type);
        }
        HIST.setTypes(types);
        classes.add(HIST);
        Class SWE = new Class();
        SWE.setName("Intro to Software Engineering");
        SWE.setBuilding("Atrium 264");
        SWE.setProfessor("Lartigue, Jonathan");
        SWE.setTimeFromH(3);
        SWE.setTimeFromM(30);
        SWE.setFromAMPM("PM");
        SWE.setTimeToH(4);
        SWE.setTimeToM(45);
        SWE.setToAMPM("PM");
        SWE.setDays("TR");
        RealmList<GradeType> types4 = new RealmList<>();
        for (int i = 1; i < 11; i++) {
            GradeType type = new GradeType();
            type.setType("Type " +i);
            type.setPercent(10 * i);
            types4.add(type);
        }
        SWE.setTypes(types);
        classes.add(SWE);
        Class ENGL = new Class();
        ENGL.setName("Early World Literature");
        ENGL.setBuilding("Atrium 133");
        ENGL.setProfessor("Sledd, Erin");
        ENGL.setTimeFromH(5);
        ENGL.setTimeFromM(0);
        ENGL.setFromAMPM("PM");
        ENGL.setTimeToH(6);
        ENGL.setTimeToM(15);
        ENGL.setToAMPM("PM");
        ENGL.setDays("TR");
        RealmList<GradeType> types5 = new RealmList<>();
        for (int i = 1; i < 11; i++) {
            GradeType type = new GradeType();
            type.setType("Type " +i);
            type.setPercent(10 * i);
            types5.add(type);
        }
        ENGL.setTypes(types);
        classes.add(ENGL);
        for (Class c : classes) {
            realm.beginTransaction();
            realm.copyToRealm(c);
            realm.commitTransaction();
        }
    }

    public void createWeeklyAlarmForDay(Context c, int d, String name, String building, int AMPM, int hr, int min) {
        AlarmManager alarm = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();
        //One hour before class starts
        cal.set(Calendar.DAY_OF_WEEK, d);
        cal.set(Calendar.HOUR, hr - 1);
        cal.set(Calendar.MINUTE, min - 1);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.AM_PM, AMPM);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.putExtra("title", name);
        alarmIntent.putExtra("location", building);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, alarmIntent,
                PendingIntent.FLAG_ONE_SHOT);
        //alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
        //      AlarmManager.INTERVAL_DAY * 7, pi);
        alarm.set(AlarmManager.RTC_WAKEUP, 1000, pi);
        Log.i("Created alarm", name +" Day: " +cal.get(Calendar.DAY_OF_WEEK) +", Time: " +cal.get(Calendar.HOUR) +":" +cal.get(Calendar.MINUTE) +" " +cal.get(Calendar.AM_PM));
    }
}
