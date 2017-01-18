package me.thenightmancodeth.classi.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import me.thenightmancodeth.classi.R;
import me.thenightmancodeth.classi.models.data.Class;
import me.thenightmancodeth.classi.views.dialog.GradeDialog;
import me.thenightmancodeth.classi.views.fragment.EditClassFragment;
import me.thenightmancodeth.classi.views.fragment.GradesListFragment;

public class ClassView extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindColor(R.color.colorPrimary) int green;
    @BindColor(R.color.colorAccent) int blue;

    Realm realm;
    String className;
    Class thisClass;

    View.OnClickListener newGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_view);
        ButterKnife.bind(this);

        Realm.init(this);
        realm = Realm.getDefaultInstance();


        Bundle args = getIntent().getExtras();
        className = args.getString(MainActivity.CLASS_NAME_EXTRA);
        toolbar.setTitle(className);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        newGrade = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment gradeDialogFrag = GradeDialog.newInstance();
                Bundle dialogArg = new Bundle();
                dialogArg.putString("class", className);
                gradeDialogFrag.setArguments(dialogArg);
                gradeDialogFrag.show(getFragmentManager(), "gradedialog");
            }
        };

        fab.setOnClickListener(newGrade);

        GradesListFragment ecf = new GradesListFragment();
        Bundle fragArgs = new Bundle();
        fragArgs.putString("class_name", className);
        ecf.setArguments(fragArgs);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.grade_list_fragment, ecf);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_class_view, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getSupportFragmentManager().findFragmentById(R.id.grade_list_fragment) instanceof EditClassFragment) {
            submitChanges();
        }
    }

    private void submitChanges() {
        EditClassFragment f = (EditClassFragment) getSupportFragmentManager().findFragmentById(R.id.grade_list_fragment);
        f.pushChangesToRealm();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_delete_class:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.confirm_delete_dialog_title);
                builder.setMessage(R.string.confirm_delete_dialog_body);
                builder.setPositiveButton(R.string.confirm_delete_dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        thisClass = realm.where(Class.class).equalTo("name", className).findFirst();
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
            case R.id.action_edit_class:
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.grade_list_fragment);
                if (currentFragment instanceof GradesListFragment) {
                    //Make fab check icon
                    fab.setImageResource(R.drawable.ic_check_24dp);
                    fab.setBackgroundTintList(ColorStateList.valueOf(green));
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Change FAB back to new grade
                            fab.setImageResource(R.drawable.ic_class_white_24dp);
                            fab.setBackgroundTintList(ColorStateList.valueOf(blue));
                            fab.setOnClickListener(newGrade);
                            //Push changes to realm
                            submitChanges();
                            //Switch to grades fragment
                            GradesListFragment ecf = new GradesListFragment();
                            //Keep current class name in fragment args
                            Bundle fragArgs = new Bundle();
                            fragArgs.putString("class_name", className);
                            ecf.setArguments(fragArgs);
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.grade_list_fragment, ecf);
                            ft.commit();
                        }
                    });
                    //Switch to edit fragment
                    EditClassFragment ecf = new EditClassFragment();
                    Bundle fragArgs = new Bundle();
                    fragArgs.putString("class_name", className);
                    ecf.setArguments(fragArgs);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.grade_list_fragment, ecf);
                    ft.commit();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshGrades() {
        GradesListFragment glf = (GradesListFragment)getSupportFragmentManager()
                .findFragmentById(R.id.grade_list_fragment);
        glf.refreshGrades();
    }
}
