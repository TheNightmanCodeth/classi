package me.thenightmancodeth.classi.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import me.thenightmancodeth.classi.R;
import me.thenightmancodeth.classi.models.data.Grade;
import me.thenightmancodeth.classi.views.custom.CircleView;
import me.thenightmancodeth.classi.models.data.Class;

/**
 * Created by thenightman on 1/8/17.
 **/

class ClassRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Class> classes;
    private Context ctx;
    String TODAY_SEPERATOR_NAME = "THENIGHTMANCOMETHBUTTHEDAYMANISNEIGH";
    String SEPERATOR_NAME = "OHDAMNYOUJUSTLOSTTHEGAME";
    String SEPERATOR_TIME = "SEPERATOR";
    private int SEPERATOR = 0;
    private int WEEK_SEPERATOR = 1;
    private int NOT_SEPERATOR = 2;

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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Class thisOne = classes.get(position);
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
                classViewHolder.bg.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Class toDelete = classes.get(position);
                        Realm r = Realm.getDefaultInstance();
                        r.beginTransaction();
                        toDelete.deleteFromRealm();
                        r.commitTransaction();
                        notifyDataSetChanged();
                        return false;
                    }
                });
        }
    }

    private int genColor(int grade) {
        if (grade >= 90) {
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
        sortedClasses.add(seperator);
        Class prev = null;
        for (Class c : classes) {
            if (today != null) {
                if (c.getDays().contains(today)) {
                    if (prev == null) {
                        prev = c;
                    } else {
                        int thisHr = c.getTimeFromH() == 12 ? 0 : c.getTimeFromH();
                        int prevHr = prev.getTimeFromH() == 12 ? 0 : prev.getTimeFromH();
                        if (thisHr < prevHr) {
                            sortedClasses.add(c);
                        } else if (thisHr > prevHr) {
                            sortedClasses.add(prev);
                            prev = c;
                        } else if (thisHr == prevHr){
                            //Compare minutes?
                            if (c.getTimeFromM() < prev.getTimeFromM()) {
                                sortedClasses.add(c);
                            } else if (c.getTimeFromM() > prev.getTimeFromM()) {
                                sortedClasses.add(prev);
                                prev = c;
                            }
                        }
                    }
                }
            }
        }
        if (prev != null) {
            sortedClasses.add(prev);
        }
        Class weekSeperator = new Class();
        weekSeperator.setName(SEPERATOR_NAME);
        weekSeperator.setDays(SEPERATOR_TIME);
        sortedClasses.add(weekSeperator);
        for (Class c : classes) {
            if (today != null) {
                if (!c.getDays().contains(today)) {
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

    @Override
    public int getItemCount() {
        return classes.size();
    }
}
