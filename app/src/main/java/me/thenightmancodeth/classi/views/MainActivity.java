package me.thenightmancodeth.classi.views;

import android.app.DialogFragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
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
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import me.thenightmancodeth.classi.R;
import me.thenightmancodeth.classi.models.Api;
import me.thenightmancodeth.classi.models.data.Class;
import me.thenightmancodeth.classi.models.data.Grade;
import me.thenightmancodeth.classi.models.data.GradeType;
import me.thenightmancodeth.classi.views.dialog.ClassDialog;

/**
 * Created by thenightman on 1/8/17.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.class_recycler) RecyclerView classesRecycler;

    Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        api = new Api(realm);

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

    private void makeFakeData(List<Class> classes) {
        for (int i = 0; i <= 8; i++) {
            Class cl = new Class();
            cl.setBuilding("Building" +i);
            cl.setDays("MWF"+i);
            RealmList<Grade> grades = new RealmList<>();
            for (int j = 0; j <=5; j++) {
                Grade gr = new Grade();
                gr.setDescription("Desc"+i);
                gr.setGrade(60+(5*i));
                GradeType gt = new GradeType();
                gt.setPercent(100);
                gt.setType("Game"+i);
                gr.setType(gt);
                gr.setName("Name"+i);
                grades.add(gr);
            }
            cl.setGrades(grades);
            cl.setName("Class"+i);
            cl.setProfessor("Professor"+i);
            cl.setTime("8:00"+i);
            classes.add(cl);
        }
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
        DividerItemDecoration did = new DividerItemDecoration(classesRecycler.getContext(),
                    DividerItemDecoration.VERTICAL);
        classesRecycler.addItemDecoration(did);

        refreshClasses();
    }

    public void refreshClasses() {
        ClassRecycleAdapter adapter = new ClassRecycleAdapter(api.getClassesFromRealm(), getApplicationContext());
        classesRecycler.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshClasses();
    }
}
