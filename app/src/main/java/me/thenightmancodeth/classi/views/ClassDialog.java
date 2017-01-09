package me.thenightmancodeth.classi.views;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.thenightmancodeth.classi.R;

/**
 * Created by thenightman on 1/9/17.
 **/

public class ClassDialog extends DialogFragment {
    @BindView(R.id.new_class_name) EditText name;
    @BindView(R.id.new_class_prof) EditText prof;
    @BindView(R.id.new_class_building) EditText building;
    

    public static ClassDialog newInstance(int title) {
        ClassDialog cd = new ClassDialog();
        Bundle args = new Bundle();
        args.putInt("title", title);
        cd.setArguments(args);
        return cd;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.new_class_dialog, null);
        ButterKnife.bind(getActivity(), dialogView);
        builder.setView(dialogView);

    }
}
