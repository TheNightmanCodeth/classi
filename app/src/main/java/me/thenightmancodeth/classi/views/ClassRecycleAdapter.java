package me.thenightmancodeth.classi.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.thenightmancodeth.classi.R;
import me.thenightmancodeth.classi.models.data.Grade;
import me.thenightmancodeth.classi.views.custom.CircleView;
import me.thenightmancodeth.classi.models.data.Class;

/**
 * Created by thenightman on 1/8/17.
 **/

public class ClassRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Class> classes;
    private Context ctx;
    private String TODAY_SEPERATOR_NAME = "THENIGHTMANCOMETHBUTTHEDAYMANISNEIGH";
    private String SEPERATOR_NAME = "OHDAMNYOUJUSTLOSTTHEGAME";
    private String SEPERATOR_TIME = "SEPERATOR";

    static class ClassViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.grade) CircleView grade;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.date_time) TextView timeDays;
        @BindView(R.id.location_prof) TextView buildingProfessor;
        @BindView(R.id.class_item_root) LinearLayout root;
        @BindView(R.id.list_bg) CardView bg;
        ClassViewHolder(View listItemView) {
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
    public int getItemViewType(int postition) {
        if (classes.get(postition).getDays().equals(SEPERATOR_TIME)) {
            if (classes.get(postition).getName().equals(TODAY_SEPERATOR_NAME)) {
                return 0;
            } else if (classes.get(postition).getName().equals(SEPERATOR_NAME)) {
                return 1;
            }
        }
        return 2;
    }

    ClassRecycleAdapter(List<Class> classes, Context ctx) {
        this.classes = classes;
        this.ctx = ctx;
        classifyDays();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View sep = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.class_seperator, parent, false);
        View cla = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.class_list_item, parent, false);
        switch (viewType) {
            case 0:
                return new SeparatorViewHolder(sep);
            case 1:
                return new SeparatorViewHolder(sep);
            case 2:
                return new ClassViewHolder(cla);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Class thisOne = classes.get(position);
        switch (holder.getItemViewType()) {
            case 0:
                SeparatorViewHolder separatorViewHolder = (SeparatorViewHolder)holder;
                separatorViewHolder.title.setText(R.string.today_seperator);
                break;
            case 1:
                SeparatorViewHolder separatorViewHolder1 = (SeparatorViewHolder)holder;
                separatorViewHolder1.title.setText(R.string.week_seperator);
                break;
            case 2:
                ClassViewHolder classViewHolder = (ClassViewHolder)holder;
                int grade = (int)average(thisOne.getGrades());
                classViewHolder.grade.setText(String.valueOf(grade));
                classViewHolder.grade.setBackgroundColor(genColor(grade));
                classViewHolder.title.setText(thisOne.getName());
                String fromM, toM;
                if (thisOne.getTimeFromM() == 0) {
                    fromM = "00";
                } else {
                    fromM = String.valueOf(thisOne.getTimeFromM());
                }

                if (thisOne.getTimeToM() == 0) {
                    toM = "00";
                } else {
                    toM = String.valueOf(thisOne.getTimeToM());
                }
                String time = thisOne.getTimeFromH() +":" +fromM +" - " +thisOne.getTimeToH() +":" +toM;
                String timeDayString = time +" • " +thisOne.getDays();
                String buildingProfessorString = thisOne.getBuilding() +" • " +thisOne.getProfessor();
                classViewHolder.timeDays.setText(timeDayString);
                classViewHolder.buildingProfessor.setText(buildingProfessorString);
                classViewHolder.bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ctx, ClassView.class);
                        Bundle passArgs = new Bundle();
                        passArgs.putString(MainActivity.CLASS_NAME_EXTRA, thisOne.getName());
                        intent.putExtras(passArgs);
                        v.getContext().startActivity(intent);
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
    Today, tomorrow, this week
     */
    private void classifyDays() {
        String today = getDayCode();
        List<Class> sortedClasses = new ArrayList<>();
        Class seperator = new Class();
        seperator.setName(TODAY_SEPERATOR_NAME);
        seperator.setDays(SEPERATOR_TIME);
        boolean sep = true;

        for (Class c : classes) {
            if (today != null) {
                if (c.getDays().contains(today)) {
                    if (sep) {
                        sortedClasses.add(seperator);
                        sep = false;
                    }
                    sortedClasses.add(c);
                }
            }
        }

        Class weekSeperator = new Class();
        weekSeperator.setName(SEPERATOR_NAME);
        weekSeperator.setDays(SEPERATOR_TIME);
        boolean wSep = true;

        for (Class c : classes) {
            if (today != null) {
                if (!c.getDays().contains(today)) {
                    if (wSep) {
                        sortedClasses.add(weekSeperator);
                        wSep = false;
                    }
                    sortedClasses.add(c);
                }
            }
        }
        classes = sortedClasses;
    }

    private String getDayCode() {
        int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        switch (today) {
            case Calendar.SUNDAY:
                return "S";
            case Calendar.MONDAY:
                return "M";
            case Calendar.TUESDAY:
                return "T";
            case Calendar.WEDNESDAY:
                return "W";
            case Calendar.THURSDAY:
                return "R";
            case Calendar.FRIDAY:
                return "F";
            case Calendar.SATURDAY:
                return "A";
            default:
                return null;
        }
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

    @Override
    public int getItemCount() {
        return classes.size();
    }
}
