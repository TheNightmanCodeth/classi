package me.thenightmancodeth.classi.views.dialog;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

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

    @BindView(R.id.dialog_grade_date_picker) ImageButton datePicker;
    @BindView(R.id.dialog_grade_time_picker) ImageButton timePicker;

    @BindString(R.string.grade_dialog_title) String title;
    @BindString(R.string.grade_dialog_submit) String submit;
    @BindString(R.string.action_edit_class) String edit;

    Class thisClass;
    Grade thisGrade;
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

        if (getArguments().getString("class") != null) {
            String className = getArguments().getString("class");
            thisClass = realm.where(Class.class).equalTo("name", className).findFirst();
        }

        if (getArguments().getString("grade") != null) {
            String gradeName = getArguments().getString("grade");
            thisGrade = realm.where(Grade.class).equalTo("name", gradeName).findFirst();
        }

        if (thisClass != null) {
            Log.i("types", "start");
            for (GradeType gt : thisClass.getTypes()) {
                gradeTypes.add(gt);
            }
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

        int index = 0;
        if (thisGrade != null) {
            for (String s : typeNames) {
                if (s.equals(thisGrade.getType().getType())) {
                    typeSpinner.setSelection(index);
                } else {
                    index++;
                }
            }

            nameET.setText(thisGrade.getName());
            descET.setText(thisGrade.getDescription());
        }

        final int year = thisGrade != null ? Integer.valueOf(thisGrade.getDueDate().substring(6,10)) :
                Calendar.getInstance().get(Calendar.YEAR);
        final int month = thisGrade != null ? Integer.valueOf(thisGrade.getDueDate().substring(0,2)) :
                Calendar.getInstance().get(Calendar.MONTH);
        final int day = thisGrade != null ? Integer.valueOf(thisGrade.getDueDate().substring(3,5)) :
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        if (thisGrade != null) {
            String dayStr = day < 10 ? "0" + day : String.valueOf(day);
            String monthStr = month < 10 ? "0" + month : String.valueOf(month);
            dueDayET.setText(dayStr);
            dueMonthET.setText(monthStr);
            dueYearET.setText(String.valueOf(year));
        }

        //Date picker
        datePicker.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if (thisGrade == null) month++;
                        String d = dayOfMonth < 10 ? "0" +dayOfMonth : String.valueOf(dayOfMonth);
                        String m = month < 10 ? "0" +month : String.valueOf(month);

                        dueDayET.setText(d);
                        dueDayET.setEnabled(false);
                        dueMonthET.setText(m);
                        dueMonthET.setEnabled(false);
                        dueYearET.setText(String.valueOf(year));
                        dueYearET.setEnabled(false);
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });
        if (thisGrade != null) Log.i("Time", thisGrade.getDueTime());
        int hr = thisGrade != null ? Integer.valueOf(thisGrade.getDueTime().substring(0,2)) :
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        final int m = thisGrade != null ? Integer.valueOf(thisGrade.getDueTime().substring(3,5)) :
                Calendar.getInstance().get(Calendar.MINUTE);

        int ampm = 0;
        if (thisGrade != null) {
            String ap = thisGrade.getDueTime().substring(6, 8);
            if (ap.equals("PM")) {
                ampm = 1;
            }

            String hrStr = hr < 10 ? "0" +hr : String.valueOf(hr);
            String mnStr = m < 10 ? "0" +m : String.valueOf(m);
            dueHourET.setText(hrStr);
            dueMinuteET.setText(mnStr);
            ampmSpinner.setSelection(ampm);
        }

        final int h = ampm == 1 ? hr+12 : hr;
        //Time picker
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ampm = 0;
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                int ampm;
                                String hour = "", min = "";
                                if (hourOfDay > 12) {
                                    int diff = hourOfDay - 12;
                                    hour = diff < 10 ? "0" +diff :
                                           String.valueOf(diff);
                                    min = minute < 10 ? "0" + minute : String.valueOf(minute);
                                    ampm = 1;
                                } else if (hourOfDay == 0) {
                                    hour = "12";
                                    min = minute < 10 ? "0" + minute : String.valueOf(minute);
                                    ampm = 0;
                                } else {
                                    hour = hourOfDay < 10 ? "0" +hourOfDay :
                                           String.valueOf(hourOfDay);
                                    min = minute < 10 ? "0" + minute : String.valueOf(minute);
                                    ampm = 0;
                                }
                                dueHourET.setText(String.valueOf(hour));
                                dueHourET.setEnabled(false);
                                dueMinuteET.setText(String.valueOf(min));
                                dueMinuteET.setEnabled(false);
                                ampmSpinner.setSelection(ampm);
                                ampmSpinner.setEnabled(false);
                            }
                        }, h, m, false);

                timePickerDialog.show();
            }
        });

        builder.setView(dialogView);
        builder.setTitle(title);
        String button = thisGrade != null ? edit : submit;
        builder.setPositiveButton(button, new DialogInterface.OnClickListener() {
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

                if (thisGrade != null) {
                    //Edit in realm
                    realm.beginTransaction();
                    thisGrade.setName(nameString);
                    thisGrade.setDescription(descString);
                    thisGrade.setDueDate(dueDateString);
                    thisGrade.setDueTime(dueTimeString);
                    thisGrade.setType(type);
                    realm.commitTransaction();
                } else {
                    //Commit to realm db
                    realm.beginTransaction();
                    thisClass.getGrades().add(newGrade);
                    realm.commitTransaction();
                }

                //Create alarm
                ((ClassView)getActivity()).createAlarmForGrade(newGrade);

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
