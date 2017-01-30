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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import me.thenightmancodeth.classi.R;
import me.thenightmancodeth.classi.models.data.Class;
import me.thenightmancodeth.classi.models.data.Grade;
import me.thenightmancodeth.classi.models.data.GradeType;

/**
 * Created by J Diggity on 1/29/2017.
 */

public class TypesDialog extends DialogFragment {
    @BindView(R.id.types_dialog_spinner) Spinner typesSpinner;

    Realm realm;
    Class thisClass;

    public static TypesDialog newInstance() {
        return new TypesDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams")
        View dialogView = inflater.inflate(R.layout.types_dialog, null);
        ButterKnife.bind(this, dialogView);

        Realm.init(getContext());
        realm = Realm.getDefaultInstance();

        Context ctx = getActivity().getApplicationContext();
        String className = getArguments().getString("class");
        thisClass = realm.where(Class.class).equalTo("name", className).findFirst();

        List<String> types = new ArrayList<>();
        for (GradeType t : thisClass.getTypes()) {
            types.add(t.getType());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(ctx,
                android.R.layout.simple_spinner_dropdown_item, types);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typesSpinner.setAdapter(spinnerAdapter);

        builder.setView(dialogView);
        builder.setTitle("Types");
        builder.setPositiveButton("New Type...", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                @SuppressLint("InflateParams")
                View dialogView = inflater.inflate(R.layout.new_type_dialog, null);

                final EditText name = (EditText)dialogView.findViewById(R.id.type_title);
                final EditText perc = (EditText)dialogView.findViewById(R.id.type_percent);

                builder.setView(dialogView);
                builder.setTitle("New Type");
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        realm.beginTransaction();
                        GradeType gt = new GradeType();
                        gt.setType(name.getText().toString());
                        gt.setPercent(Integer.valueOf(perc.getText().toString()));
                        thisClass.getTypes().add(gt);
                        realm.commitTransaction();
                    }
                });
                builder.create().show();
            }
        });
        builder.setNeutralButton("Edit Type...", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                @SuppressLint("InflateParams")
                View dialogView = inflater.inflate(R.layout.new_type_dialog, null);

                final EditText name = (EditText)dialogView.findViewById(R.id.type_title);
                final EditText perc = (EditText)dialogView.findViewById(R.id.type_percent);

                GradeType holder = new GradeType();
                for (GradeType t : thisClass.getTypes()) {
                    if (t.getType().equals(typesSpinner.getSelectedItem())) {
                        holder = t;
                    }
                }
                final GradeType type = holder;

                name.setText(type.getType());
                perc.setText(String.valueOf(type.getPercent()));

                builder.setView(dialogView);
                builder.setTitle("Edit Type");
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        realm.beginTransaction();
                        for (GradeType t : thisClass.getTypes()) {
                            if (t.getType().equals(type.getType())) {
                                t.setPercent(Integer.valueOf(perc.getText().toString()));
                                t.setType(name.getText().toString());
                            }
                        }
                        realm.commitTransaction();
                    }
                });
                builder.create().show();
            }
        });
        return builder.create();
    }
}
