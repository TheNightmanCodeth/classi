package me.thenightmancodeth.classi.views.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import me.thenightmancodeth.classi.R;
import me.thenightmancodeth.classi.models.data.Class;
import me.thenightmancodeth.classi.models.data.Grade;
import me.thenightmancodeth.classi.models.data.GradeType;
import me.thenightmancodeth.classi.views.ClassView;
import me.thenightmancodeth.classi.views.MainActivity;
import me.thenightmancodeth.classi.views.fragment.GradesListFragment;

/**
 * Created by thenightman on 1/18/17.
 **/

public class GradeDialog extends DialogFragment {
    @BindView(R.id.dialog_grade_name) EditText nameET;
    @BindView(R.id.dialog_grade_desc) EditText descET;
    @BindView(R.id.dialog_grade_date_m) EditText dueMonthET;
    @BindView(R.id.dialog_grade_date_d) EditText dueDayET;
    @BindView(R.id.dialog_grade_date_y) EditText dueYearET;
    @BindView(R.id.dialog_grade_time_h) EditText dueHourET;
    @BindView(R.id.dialog_grade_time_m) EditText dueMinuteET;
    @BindView(R.id.dialog_grade_types) Spinner typeSpinner;
    @BindView(R.id.dialog_grade_ampm) Spinner ampmSpinner;

    @BindString(R.string.grade_dialog_title) String title;
    @BindString(R.string.grade_dialog_submit) String submit;

    Class thisClass;
    List<GradeType> gradeTypes = new ArrayList<>();
    Realm realm;

    public static GradeDialog newInstance() {
        return new GradeDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams")
        View dialogView = inflater.inflate(R.layout.grade_dialog, null);
        ButterKnife.bind(this, dialogView);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.ampm, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ampmSpinner.setAdapter(spinnerAdapter);

        Realm.init(getContext());
        realm = Realm.getDefaultInstance();

        Context ctx = getActivity().getApplicationContext();
        String className = getArguments().getString("class");
        thisClass = realm.where(Class.class).equalTo("name", className).findFirst();

        Log.i("types", "start");
        for (GradeType gt : thisClass.getTypes()) {
            gradeTypes.add(gt);
        }

        List<String> typeNames = new ArrayList<>();
        for (GradeType gt : gradeTypes) {
            typeNames.add(gt.getType() +" (" +gt.getPercent() +"%)");
        }
        //Type spinner
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, typeNames);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        builder.setView(dialogView);
        builder.setTitle(title);
        builder.setPositiveButton(submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Grade newGrade = new Grade();

                //Init realm
                Realm.init(getActivity().getApplicationContext());
                Realm realm = Realm.getDefaultInstance();

                //Get data from input
                String nameString = nameET.getText().toString();
                String descString = descET.getText().toString();
                String dueDateString = dueMonthET.getText().toString() +'/'
                        +dueDayET.getText().toString() +'/'
                        +dueYearET.getText().toString();
                String dueTimeString = dueHourET.getText().toString() +':'
                        +dueMinuteET.getText().toString() +" "
                        +ampmSpinner.getSelectedItem().toString();
                GradeType type = gradeTypes.get(typeSpinner.getSelectedItemPosition());

                //Create new grade from input
                newGrade.setName(nameString);
                newGrade.setDescription(descString);
                newGrade.setDueDate(dueDateString);
                newGrade.setDueTime(dueTimeString);
                newGrade.setType(type);
                Class toRealm = thisClass;

                //Commit to realm db
                realm.beginTransaction();
                thisClass.getGrades().add(newGrade);
                realm.commitTransaction();

                //Create alarm
                ((MainActivity)getActivity()).createAlarmForGrade(getActivity(), newGrade);

                ((ClassView)getActivity()).refreshGrades();
            }
        });

        return builder.create();
    }

    public static int charToDay(char d) {
        switch (d) {
            case 'S':
                return Calendar.SUNDAY;
            case 'M':
                return Calendar.MONDAY;
            case 'T':
                return Calendar.TUESDAY;
            case 'W':
                return Calendar.WEDNESDAY;
            case 'R':
                return Calendar.THURSDAY;
            case 'F':
                return Calendar.FRIDAY;
            case 'A':
                return Calendar.SATURDAY;
            default:
                return 0;
        }
    }
}
