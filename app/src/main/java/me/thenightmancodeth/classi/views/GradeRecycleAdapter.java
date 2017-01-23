package me.thenightmancodeth.classi.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.thenightmancodeth.classi.R;
import me.thenightmancodeth.classi.models.data.Grade;
import me.thenightmancodeth.classi.models.data.GradeType;
import me.thenightmancodeth.classi.views.custom.CircleView;

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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
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
                int grade = (int)thisOne.getGrade();
                gradeViewHolder.grade.setText(String.valueOf(grade));
                gradeViewHolder.grade.setBackgroundColor(genColor(grade));
                gradeViewHolder.title.setText(thisOne.getName());
                gradeViewHolder.description.setText(thisOne.getDescription());
                gradeViewHolder.due.setText(thisOne.getDueDate());

                gradeViewHolder.bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO: Launch activity for displaying grade
                    }
                });

                gradeViewHolder.grade.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO: Show alert for entering grade
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
        //24hrs - DESC/TYPE
        Grade seperator24 = new Grade();
        GradeType type = new GradeType();
        type.setType(SEPERATOR_TYPE);
        seperator24.setType(type);
        seperator24.setDescription(SEPERATOR_DESC);
        seperator24.setName("");
        boolean sep24 = true;
        //Add all grades due in 24 hours or less
        for (Grade g : grades) {
            if (getPeriodTo(g.getDueDate(), g.getDueTime()).getDays() < 1
                    && getPeriodTo(g.getDueDate(), g.getDueTime()).getHours() < 25) {
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
            if (getPeriodTo(g.getDueDate(), g.getDueTime()).getDays() > 0) {
                if (sepW) {
                    sortedGrades.add(weekSeperator);
                    sepW = false;
                }
                sortedGrades.add(g);
            }
        }

        //Finished - DESC/NAME/TYPE
        Grade finSeperator = new Grade();
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
