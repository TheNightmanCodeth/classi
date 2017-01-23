package me.thenightmancodeth.classi.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
        //Returns viewType for the item at position
        if (classes.get(postition).getDays().equals(SEPERATOR_TIME)) {
            if (classes.get(postition).getName().equals(TODAY_SEPERATOR_NAME)) {
                //It's the 'today' seperator
                return 0;
            } else if (classes.get(postition).getName().equals(SEPERATOR_NAME)) {
                //It's the 'upcoming' seperator
                return 1;
            }
        }
        //It's a class viewholder
        return 2;
    }

    ClassRecycleAdapter(List<Class> classes, Context ctx) {
        this.classes = classes;
        this.ctx = ctx;
        //Sort class list into today & upcoming
        classifyDays();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Create view instances for seperators and classes
        View sep = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.class_seperator, parent, false);
        View cla = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.class_list_item, parent, false);
        switch (viewType) {
            case 0:
                //Today seperator
                return new SeparatorViewHolder(sep);
            case 1:
                //Upcoming seperator
                return new SeparatorViewHolder(sep);
            case 2:
                //Class
                return new ClassViewHolder(cla);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        //Get the class from arraylist
        final Class thisOne = classes.get(position);
        switch (holder.getItemViewType()) {
            case 0:
                //Set the viewholder text to today_separator string
                SeparatorViewHolder separatorViewHolder = (SeparatorViewHolder)holder;
                separatorViewHolder.title.setText(R.string.today_separator);
                break;
            case 1:
                //Set the viewholder title text to wee_separator
                SeparatorViewHolder separatorViewHolder1 = (SeparatorViewHolder)holder;
                separatorViewHolder1.title.setText(R.string.week_separator);
                break;
            case 2:
                //Create the viewholder
                ClassViewHolder classViewHolder = (ClassViewHolder)holder;
                //Calculate the average
                int grade = (int)average(thisOne.getGrades());
                //Set title & grade textviews
                classViewHolder.grade.setText(String.valueOf(grade));
                classViewHolder.grade.setBackgroundColor(genColor(grade));
                classViewHolder.title.setText(thisOne.getName());

                //Make sure time is formatted correctly
                String fromM, toM, fromH, toH;
                fromM = thisOne.getTimeFromM() < 10 ? "0" +thisOne.getTimeFromM() :
                        String.valueOf(thisOne.getTimeFromM());
                toM   = thisOne.getTimeToM() < 10 ? "0" +thisOne.getTimeToM() :
                        String.valueOf(thisOne.getTimeToM());
                fromH = thisOne.getTimeFromH() < 10 ? "0" +thisOne.getTimeFromH() :
                        String.valueOf(thisOne.getTimeFromH());
                toH   = thisOne.getTimeToH() < 10 ? "0" +thisOne.getTimeToH() :
                        String.valueOf(thisOne.getTimeToH());

                //Set text values of viewholder items
                String time = fromH +":" +fromM +" - " +toH +":" +toM;
                String timeDayString = time +" • " +thisOne.getDays();
                String buildingProfessorString = thisOne.getBuilding() +" • "
                        +thisOne.getProfessor();
                classViewHolder.timeDays.setText(timeDayString);
                classViewHolder.buildingProfessor.setText(buildingProfessorString);
                //When an item is clicked, launch classview & put extras
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

    /**
     * Generates background color for grade icon on list item
     * A(90+)   - Green
     * B(80-89) - Blue
     * C(70-79) - Yellow
     * D(60-69) - Purple
     * F(50-)   - Red
     * @param grade the grade of the class
     * @return int value of grade color
     */
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
        //Get todays day (SMTWRFA)
        String today = getDayCode();
        //Create empty arraylist to store sorted list in
        List<Class> sortedClasses = new ArrayList<>();
        //Make today separator
        Class seperator = new Class();
        seperator.setName(TODAY_SEPERATOR_NAME);
        seperator.setDays(SEPERATOR_TIME);
        //Don't show the separator if there's no classes today
        boolean sep = true;
        //For each class in class list
        for (Class c : classes) {
            //If we got the today code from getDayCode()
            if (today != null) {
                //If today is in the days string of the class
                if (c.getDays().contains(today)) {
                    //If we can show the separator
                    if (sep) {
                        //Add the separator
                        sortedClasses.add(seperator);
                        //Don't add it next time
                        sep = false;
                    }
                    //Add this class to the list
                    sortedClasses.add(c);
                }
            }
        }
        //Create the this week separator
        Class weekSeperator = new Class();
        weekSeperator.setName(SEPERATOR_NAME);
        weekSeperator.setDays(SEPERATOR_TIME);
        //Only add the separator if there are classes after today
        boolean wSep = true;
        //For each class in the class list
        for (Class c : classes) {
            //If today contains the day code for today
            if (today != null) {
                //If the days string does not contain today in it
                if (!c.getDays().contains(today)) {
                    //If we can add the separator
                    if (wSep) {
                        //Add the separator
                        sortedClasses.add(weekSeperator);
                        //Don't add it next time
                        wSep = false;
                    }
                    //Add this class to sorted list
                    sortedClasses.add(c);
                }
            }
        }
        //Return the sorted list
        classes = sortedClasses;
    }

    //Turns today int value from Calendar into string
    private String getDayCode() {
        //Get todays DAY_OF_WEEK int value
        int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        //Return the proper string value
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

    //Gets the average of grades in the list provided
    private double average(List<Grade> grades) {
        double sum = 0;
        int size = 0;
        //For each grade in grades
        for (Grade g : grades) {
            //Add this grade (times the type %) to the sum
            sum += g.getGrade() * g.getType().getPercent()/100;
            //Increase the size
            size++;
        }
        //Return the average
        return sum/size;
    }

    //Turns AM/PM string into Calendar.AMPM int
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
