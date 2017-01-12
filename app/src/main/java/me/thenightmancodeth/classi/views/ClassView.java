package me.thenightmancodeth.classi.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import io.realm.Realm;
import me.thenightmancodeth.classi.R;
import me.thenightmancodeth.classi.models.data.Class;

public class ClassView extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindColor(R.color.colorPrimary) int green;
    @BindColor(R.color.colorAccent) int blue;

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
    Realm realm;
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter
                .createFromResource(getApplicationContext(),
                R.array.ampm, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromAMPMSpinner.setAdapter(spinnerAdapter);
        toAMPMSpinner.setAdapter(spinnerAdapter);
        fromAMPMSpinner.setEnabled(false);
        toAMPMSpinner.setEnabled(false);

        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();

        thisClass = realm.where(Class.class).equalTo("name", className).findFirst();
        if (thisClass != null) {
            name.setText(thisClass.getName());
            prof.setText(thisClass.getProfessor());
            building.setText(thisClass.getBuilding());
            fromHour.setText(String.valueOf(thisClass.getTimeFromH()));
            fromMin.setText(thisClass.getTimeFromM() == 0 ?
                    "00" : String.valueOf(thisClass.getTimeFromM()));
            toHour.setText(String.valueOf(thisClass.getTimeToH()));
            toMin.setText(thisClass.getTimeToM() == 0 ?
                    "00" : String.valueOf(thisClass.getTimeToM()));
            fromAMPMSpinner.setSelection(ClassRecycleAdapter.amPmIntFrom(thisClass.getFromAMPM()));
            Log.i("int", "" +ClassRecycleAdapter.amPmIntFrom(thisClass.getFromAMPM()));
            toAMPMSpinner.setSelection(ClassRecycleAdapter.amPmIntFrom(thisClass.getToAMPM()));
            for (CheckBox c : checkBoxes) {
                if (thisClass.getDays().contains(c.getText())) {
                    c.setChecked(true);
                }
            }
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!name.isEnabled()) {
                    fab.setImageResource(R.drawable.ic_check_24dp);
                    fab.setBackgroundTintList(ColorStateList.valueOf(green));
                } else {
                    fab.setImageResource(R.drawable.ic_edit_24dp);
                    fab.setBackgroundTintList(ColorStateList.valueOf(blue));

                    //Get data from input
                    String nameString = name.getText().toString();
                    String profString = prof.getText().toString();
                    String builString = building.getText().toString();
                    String days = "";
                    for (CheckBox c : checkBoxes) {
                        if (c.isChecked()) {
                            days += c.getText().toString();
                        }
                    }

                    //Edit object in realm
                    realm.beginTransaction();
                    thisClass.setBuilding(builString);
                    thisClass.setDays(days);
                    thisClass.setName(nameString);
                    thisClass.setProfessor(profString);
                    thisClass.setTimeFromH(Integer.valueOf(fromHour.getText().toString()));
                    thisClass.setTimeFromM(Integer.valueOf(fromMin.getText().toString()));
                    thisClass.setFromAMPM(fromAMPMSpinner.getSelectedItem().toString());
                    thisClass.setTimeToH(Integer.valueOf(toHour.getText().toString()));
                    thisClass.setTimeToM(Integer.valueOf(toMin.getText().toString()));
                    thisClass.setToAMPM(toAMPMSpinner.getSelectedItem().toString());                    
                    realm.commitTransaction();
                }
                name.setEnabled(!name.isEnabled());
                prof.setEnabled(!prof.isEnabled());
                building.setEnabled(!building.isEnabled());
                fromHour.setEnabled(!fromHour.isEnabled());
                fromMin.setEnabled(!fromMin.isEnabled());
                toHour.setEnabled(!toHour.isEnabled());
                toMin.setEnabled(!toMin.isEnabled());
                fromAMPMSpinner.setEnabled(!fromAMPMSpinner.isEnabled());
                toAMPMSpinner.setEnabled(!toAMPMSpinner.isEnabled());
                for (CheckBox c : checkBoxes) {
                    c.setEnabled(!c.isEnabled());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_class_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_class) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.confirm_delete_dialog_title);
            builder.setMessage(R.string.confirm_delete_dialog_body);
            builder.setPositiveButton(R.string.confirm_delete_dialog_positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    realm.beginTransaction();
                    thisClass.deleteFromRealm();
                    realm.commitTransaction();
                    startActivity(new Intent(ClassView.this, MainActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
            });
            builder.setNegativeButton(R.string.confirm_delete_dialog_negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
