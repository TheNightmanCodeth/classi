package me.thenightmancodeth.classi.views;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import me.thenightmancodeth.classi.R;
import me.thenightmancodeth.classi.models.data.Grade;
import me.thenightmancodeth.classi.models.data.GradeType;
import me.thenightmancodeth.classi.views.custom.CircleView;
import me.thenightmancodeth.classi.views.dialog.GradeDialog;

/**
 * Created by thenightman on 1/8/17.
 **/

public class GradeRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Grade> grades;
    private Context ctx;
    private String SEPERATOR_TYPE = "THENIGHTMANCOMETHBUTTHEDAYMANISNEIGH";
    private String SEPERATOR_NAME = "OHDAMNYOUJUSTLOSTTHEGAME";
    private String SEPERATOR_DESC = "SEPERATOR";

    private final int SEPERATOR_24 = 0;
    private final int SEPERATOR_WEEK = 1;
    private final int SEPERATOR_FIN = 2;
    private final int GRADE = 3;

    private Realm realm;

    @BindArray(R.array.grade_edit) String[] gradeEdit;

    static class GradeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.grade) CircleView grade;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.date_time) TextView description;
        @BindView(R.id.location_prof) TextView due;
        @BindView(R.id.list_bg) CardView bg;
        GradeViewHolder(View listItemView) {
            super(listItemView);
            ButterKnife.bind(this, listItemView);
        }
    }

    static class SeparatorViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title) TextView title;
        SeparatorViewHolder(View sepListView) {
            super(sepListView);
            ButterKnife.bind(this, sepListView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (grades.get(position).getDescription().equals(SEPERATOR_DESC)) {
            if (grades.get(position).getName().equals(SEPERATOR_NAME) && grades.get(position).getType().getType().equals(SEPERATOR_TYPE)) {
                return SEPERATOR_FIN;
            } else if (grades.get(position).getName().equals(SEPERATOR_NAME)) {
                return SEPERATOR_WEEK;
            } else if (grades.get(position).getType().getType().equals(SEPERATOR_TYPE)) {
                return SEPERATOR_24;
            }
        }
        return GRADE;
    }

    public GradeRecycleAdapter(List<Grade> grades, Context ctx) {
        this.grades = grades;
        this.ctx = ctx;
        Realm.init(ctx);
        realm = Realm.getDefaultInstance();
        classifyGrades();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View sep = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.class_seperator, parent, false);
        View cla = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.class_list_item, parent, false);
        switch (viewType) {
            case SEPERATOR_24:
                //Today
                return new SeparatorViewHolder(sep);
            case SEPERATOR_WEEK:
                //This week
                return new SeparatorViewHolder(sep);
            case SEPERATOR_FIN:
                //Finished
                return new SeparatorViewHolder(sep);
            case GRADE:
                //Grade
                return new GradeViewHolder(cla);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Grade thisOne = grades.get(position);
        switch (holder.getItemViewType()) {
            case SEPERATOR_24:
                //Due today
                SeparatorViewHolder separatorViewHolder = (SeparatorViewHolder)holder;
                separatorViewHolder.title.setText(R.string.twentyfour_hour_seperator);
                break;
            case SEPERATOR_WEEK:
                //Due this week
                SeparatorViewHolder separatorViewHolder1 = (SeparatorViewHolder)holder;
                separatorViewHolder1.title.setText(R.string.week_separator);
                break;
            case SEPERATOR_FIN:
                //Finished
                SeparatorViewHolder separatorViewHolder2 = (SeparatorViewHolder)holder;
                separatorViewHolder2.title.setText(R.string.finished_seperator);
                break;
            case GRADE:
                GradeViewHolder gradeViewHolder = (GradeViewHolder) holder;
                final int grade = (int)thisOne.getGrade();
                gradeViewHolder.grade.setText(String.valueOf(grade));
                gradeViewHolder.grade.setBackgroundColor(genColor(grade));
                gradeViewHolder.title.setText(thisOne.getName());
                gradeViewHolder.description.setText(thisOne.getDescription());
                gradeViewHolder.due.setText(thisOne.getDueDate());

                gradeViewHolder.bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                        builder.setItems(R.array.grade_edit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        //Edit
                                        //Display grade dialog with information filled in
                                        DialogFragment gradeDialogFrag = GradeDialog.newInstance();
                                        Bundle dialogArg = new Bundle();
                                        String className = ((ClassView)ctx).className;
                                        dialogArg.putString("class", className);
                                        dialogArg.putString("grade", thisOne.getName());
                                        gradeDialogFrag.setArguments(dialogArg);
                                        gradeDialogFrag.show(((AppCompatActivity)ctx)
                                                .getFragmentManager(), "gradedialog");
                                        break;
                                    case 1:
                                        //Complete
                                        AlertDialog.Builder gradeBuilder = new
                                                AlertDialog.Builder(ctx);
                                        View alert = ((AppCompatActivity) ctx).getLayoutInflater()
                                                .inflate(R.layout.alert_complete_grade, null);
                                        final EditText gradeET = (EditText)alert
                                                .findViewById(R.id.assign_grade_grade);
                                        gradeBuilder.setTitle("Complete Grade")
                                                .setView(alert)
                                                .setPositiveButton("GRADE", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        realm.beginTransaction();
                                                        thisOne.setGrade(Double.valueOf(gradeET
                                                                .getText().toString()));
                                                        thisOne.setFinished(true);
                                                        realm.commitTransaction();
                                                        notifyDataSetChanged();
                                                    }
                                                });
                                        gradeBuilder.setNegativeButton("CANCEL", null);
                                        gradeBuilder.create().show();
                                        break;
                                    case 2:
                                        //Delete
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
                                        builder1.setTitle("Delete " +thisOne.getName());
                                        builder1.setMessage("Are you sure you want to delete "
                                                +thisOne.getName() +"?");
                                        builder1.setPositiveButton("DELETE",
                                                new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                realm.beginTransaction();
                                                thisOne.deleteFromRealm();
                                                grades.remove(position);
                                                realm.commitTransaction();
                                                notifyDataSetChanged();
                                            }
                                        });
                                        builder1.setNegativeButton("CANCEL", null);
                                        builder1.create().show();
                                        break;
                                }
                            }
                        });
                        builder.create().show();
                    }
                });
        }
    }

    private int genColor(int grade) {
        if (grade == 0) {
            return ContextCompat.getColor(ctx, R.color.none);
        } else if (grade >= 90) {
            return ContextCompat.getColor(ctx, R.color.a);
        } else if (grade >= 80) {
            return ContextCompat.getColor(ctx, R.color.b);
        } else if (grade >= 70) {
            return ContextCompat.getColor(ctx, R.color.c);
        } else if (grade >= 60) {
            return ContextCompat.getColor(ctx, R.color.d);
        } else {
            return ContextCompat.getColor(ctx, R.color.f);
        }
    }

    /*
    Sorts classes by day.
    24hrs, this week
     */
    private void classifyGrades() {
        List<Grade> sortedGrades = new ArrayList<>();
        //Finished - DESC/NAME/TYPE
        Grade finSeperator = new Grade();
        GradeType type = new GradeType();
        type.setType(SEPERATOR_TYPE);
        finSeperator.setDescription(SEPERATOR_DESC);
        finSeperator.setName(SEPERATOR_NAME);
        finSeperator.setType(type);
        boolean sepF = true;
        //Add all finished grades
        for (Grade g : grades) {
            if (g.finished) {
                if (sepF) {
                    sortedGrades.add(finSeperator);
                    sepF = false;
                }
                sortedGrades.add(g);
            }
        }

        //24hrs - DESC/TYPE
        Grade seperator24 = new Grade();
        seperator24.setType(type);
        seperator24.setDescription(SEPERATOR_DESC);
        seperator24.setName("");
        boolean sep24 = true;
        //Add all grades due in 24 hours or less
        for (Grade g : grades) {
            if (getPeriodTo(g.getDueDate(), g.getDueTime()).getDays() < 1
                    && getPeriodTo(g.getDueDate(), g.getDueTime()).getHours() < 25 && !g.finished) {
                if (sep24) {
                    sortedGrades.add(seperator24);
                    sep24 = false;
                }
                sortedGrades.add(g);
            }
        }

        //Week - DESC/NAME
        Grade weekSeperator = new Grade();
        weekSeperator.setName(SEPERATOR_NAME);
        weekSeperator.setDescription(SEPERATOR_DESC);
        GradeType gt = new GradeType();
        gt.setPercent(0);
        gt.setType("0");
        weekSeperator.setType(gt);
        boolean sepW = true;
        //Add all grades due in over 24 hours
        for (Grade g : grades) {
            if (getPeriodTo(g.getDueDate(), g.getDueTime()).getDays() > 0 && !g.finished) {
                if (sepW) {
                    sortedGrades.add(weekSeperator);
                    sepW = false;
                }
                sortedGrades.add(g);
            }
        }

        grades = sortedGrades;
    }

    private double average(List<Grade> grades) {
        double sum = 0;
        int size = 0;
        for (Grade g: grades) {
            sum += g.getGrade() * g.getType().getPercent()/100;
            size++;
        }
        return sum/size;
    }

    public static int amPmIntFrom(String ampm) {
        switch (ampm) {
            case "AM":
                return 0;
            case "PM":
                return 1;
        }
        return 0;
    }

    private Period getPeriodTo(String date, String time) {
        DateTime dueDateTime = DateTime.parse(String.format("%s %s", date, time),
                DateTimeFormat.forPattern("MM/dd/yyyy hh:mm aa"));
        DateTime now = DateTime.now();
        Period test = new Period(now, dueDateTime);
        Log.i("test", test.getHours() +"");
        return new Period(now, dueDateTime);
    }

    @Override
    public int getItemCount() {
        return grades.size();
    }
}
