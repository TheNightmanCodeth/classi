package me.thenightmancodeth.classi.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import me.thenightmancodeth.classi.R;
import me.thenightmancodeth.classi.models.data.Class;

public class ClassView extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.new_class_name)           EditText name;
    @BindView(R.id.new_class_prof)           EditText prof;
    @BindView(R.id.new_class_building)       EditText building;
    @BindView(R.id.new_class_from_time_hour) EditText fromHour;
    @BindView(R.id.new_class_from_time_min)  EditText fromMin;
    @BindView(R.id.new_class_to_time_hour)   EditText toHour;
    @BindView(R.id.new_class_to_time_min)    EditText toMin;
    @BindView(R.id.new_class_from_am_pm)     Spinner fromAMPMSpinner;
    @BindView(R.id.new_class_to_am_pm)       Spinner toAMPMSpinner;
    @BindViews({R.id.check_sun, R.id.check_mon, R.id.check_tue, R.id.check_wed, R.id.check_thu, R.id.check_fri, R.id.check_sat})
    List<CheckBox> checkBoxes;

    String className;
    Class thisClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_view);
        ButterKnife.bind(this);

        Bundle args = getIntent().getExtras();
        className = args.getString(MainActivity.CLASS_NAME_EXTRA);
        toolbar.setTitle(className);
        setSupportActionBar(toolbar);

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Class> thisClassFromRealm = realm.where(Class.class)
                .equalTo("name", className).findAll();
        thisClass = thisClassFromRealm.get(0);

        if (thisClass != null) {
            name.setText(thisClass.getName());
            prof.setText(thisClass.getProfessor());
            building.setText(thisClass.getBuilding());
            fromHour.setText(String.valueOf(thisClass.getTimeFromH()));
            fromMin.setText(String.valueOf(thisClass.getTimeFromM()));
            toHour.setText(String.valueOf(thisClass.getTimeToH()));
            toMin.setText(String.valueOf(thisClass.getTimeToM()));
            fromAMPMSpinner.setSelection(ClassRecycleAdapter.amPmIntFrom(thisClass.getFromAMPM()) +1);
            Log.i("int", "" +ClassRecycleAdapter.amPmIntFrom(thisClass.getFromAMPM()));
            toAMPMSpinner.setSelection(ClassRecycleAdapter.amPmIntFrom(thisClass.getToAMPM()));
            for (CheckBox c : checkBoxes) {
                if (thisClass.getDays().contains(c.getText())) {
                    c.setChecked(true);
                }
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
