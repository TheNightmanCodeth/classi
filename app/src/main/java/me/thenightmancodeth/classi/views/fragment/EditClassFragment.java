package me.thenightmancodeth.classi.views.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import io.realm.Realm;
import me.thenightmancodeth.classi.R;
import me.thenightmancodeth.classi.models.data.Class;
import me.thenightmancodeth.classi.views.ClassRecycleAdapter;

/**
 * Created by thenightman on 1/17/17.
 **/

public class EditClassFragment extends Fragment {
    @BindView(R.id.new_class_name)
    EditText name;
    @BindView(R.id.new_class_prof)           EditText prof;
    @BindView(R.id.new_class_building)       EditText building;
    @BindView(R.id.new_class_from_time_hour) EditText fromHour;
    @BindView(R.id.new_class_from_time_min)  EditText fromMin;
    @BindView(R.id.new_class_to_time_hour)   EditText toHour;
    @BindView(R.id.new_class_to_time_min)    EditText toMin;
    @BindView(R.id.new_class_from_am_pm)
    Spinner fromAMPMSpinner;
    @BindView(R.id.new_class_to_am_pm)       Spinner toAMPMSpinner;
    @BindViews({R.id.check_sun, R.id.check_mon, R.id.check_tue, R.id.check_wed, R.id.check_thu, R.id.check_fri, R.id.check_sat})
    List<CheckBox> checkBoxes;

    Realm realm;
    Class thisClass;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_class_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter
                .createFromResource(getContext(),
                        R.array.ampm, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromAMPMSpinner.setAdapter(spinnerAdapter);
        toAMPMSpinner.setAdapter(spinnerAdapter);

        Realm.init(getContext());
        realm = Realm.getDefaultInstance();

        String className = getArguments().getString("class_name", null);

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
    }

    public void pushChangesToRealm() {
        Log.i("Realm", "pushing changes to realm...");
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
}
