package me.thenightmancodeth.classi.views.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import me.thenightmancodeth.classi.R;
import me.thenightmancodeth.classi.models.data.Class;
import me.thenightmancodeth.classi.models.data.Grade;
import me.thenightmancodeth.classi.views.MainActivity;

/**
 * Created by thenightman on 1/9/17.
 **/

public class ClassDialog extends DialogFragment {
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

    @BindString(R.string.dialog_class_title) String title;
    @BindString(R.string.dialog_submit) String submit;

    public static ClassDialog newInstance() {
        return new ClassDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.new_class_dialog, null);
        ButterKnife.bind(this, dialogView);
        Context ctx = getActivity().getApplicationContext();
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(ctx,
                R.array.ampm, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromAMPMSpinner.setAdapter(spinnerAdapter);
        toAMPMSpinner.setAdapter(spinnerAdapter);

        builder.setView(dialogView);
        builder.setTitle(title);
        builder.setPositiveButton(submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Class newClass = new Class();

                //Init realm
                Realm.init(getActivity().getApplicationContext());
                Realm realm = Realm.getDefaultInstance();

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

                //Create class object from input
                newClass.setBuilding(builString);
                newClass.setDays(days);
                newClass.setGrades(new RealmList<Grade>());
                newClass.setName(nameString);
                newClass.setProfessor(profString);
                newClass.setTimeFromH(Integer.valueOf(fromHour.getText().toString()));
                newClass.setTimeFromM(Integer.valueOf(fromMin.getText().toString()));
                newClass.setFromAMPM(fromAMPMSpinner.getSelectedItem().toString());
                newClass.setTimeToH(Integer.valueOf(toHour.getText().toString()));
                newClass.setTimeToM(Integer.valueOf(toMin.getText().toString()));
                newClass.setToAMPM(toAMPMSpinner.getSelectedItem().toString());

                //Commit to realm db
                realm.beginTransaction();
                realm.copyToRealm(newClass);
                realm.commitTransaction();

                ((MainActivity)getActivity()).refreshClasses();
            }
        });

        return builder.create();
    }
}
