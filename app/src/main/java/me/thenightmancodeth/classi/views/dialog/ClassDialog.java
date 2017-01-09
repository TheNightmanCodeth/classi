package me.thenightmancodeth.classi.views.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

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
    @BindView(R.id.new_class_name) EditText name;
    @BindView(R.id.new_class_prof) EditText prof;
    @BindView(R.id.new_class_building) EditText building;
    @BindView(R.id.new_class_from_time_hour) EditText fromHour;
    @BindView(R.id.new_class_from_time_min)  EditText fromMin;
    @BindView(R.id.new_class_to_time_hour)   EditText toHour;
    @BindView(R.id.new_class_to_time_min)  EditText toMin;
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

        builder.setView(dialogView);
        builder.setTitle(title);
        builder.setPositiveButton(submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Init realm
                Realm.init(getActivity().getApplicationContext());
                Realm realm = Realm.getDefaultInstance();

                //Get data from input
                String nameString = name.getText().toString();
                String profString = prof.getText().toString();
                String builString = building.getText().toString();
                String timeString = fromHour.getText().toString() +":" +fromMin.getText().toString()
                        +" - " +toHour.getText().toString() +":" +toMin.getText().toString();
                String days = "";
                for (CheckBox c : checkBoxes) {
                    if (c.isChecked()) {
                        days += c.getText().toString();
                    }
                }

                //Create class object from input
                Class newClass = new Class();
                newClass.setBuilding(builString);
                newClass.setDays(days);
                newClass.setGrades(new RealmList<Grade>());
                newClass.setName(nameString);
                newClass.setProfessor(profString);
                newClass.setTime(timeString);

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
